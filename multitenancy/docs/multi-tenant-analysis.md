# Multi-Tenant N개 tenantId 요청 대응 분석

## 1. 현재 구조

### 아키텍처
```
HTTP Request (X-Tenant-Id: tenantA)
  → TenantFilter (헤더 추출)
    → TenantContext (ThreadLocal에 단일 tenantId 저장)
      → TenantIdentifierResolver (Hibernate에 tenantId 전달)
        → @TenantId (모든 쿼리에 WHERE tenant_id = ? 자동 추가)
```

### 핵심 컴포넌트

#### TenantContext
```kotlin
object TenantContext {
    private val currentTenant = ThreadLocal<String?>()  // 단일 값
}
```

#### TenantIdentifierResolver
```kotlin
class TenantIdentifierResolver : CurrentTenantIdentifierResolver<String> {
    override fun resolveCurrentTenantIdentifier(): String {  // 반환 타입이 String (단일)
        return TenantContext.getTenantId() ?: DEFAULT_TENANT
    }
}
```

#### TenantAwareEntity
```kotlin
@MappedSuperclass
abstract class TenantAwareEntity(
    @TenantId  // Hibernate가 자동으로 WHERE tenant_id = ? 추가
    @Column(name = "tenant_id", nullable = false, updatable = false)
    val tenantId: String? = null,
)
```

### 자동 생성되는 쿼리
```sql
-- findAll() 호출 시
SELECT g.id, g.name, g.tenant_id
FROM groups g
WHERE g.tenant_id = ?    -- Hibernate가 자동 추가 (equals, 단일 값)
```

---

## 2. N개 tenantId 지원이 불가능한 이유

### Hibernate API 레벨 제약

| 제약 포인트 | 설명 |
|------------|------|
| `CurrentTenantIdentifierResolver<String>` | 인터페이스 반환 타입이 `String`. `List<String>` 불가 |
| `@TenantId` | 내부적으로 `=` (equals) 조건만 생성. `IN` 절 미지원 |
| Discriminator 전략 | Hibernate의 멀티테넌시 Discriminator 전략 자체가 **단일 테넌트 식별자** 기반 설계 |

즉, `@TenantId`는 Hibernate ORM 프레임워크 레벨에서 **구조적으로 단일 tenantId만 지원**한다.
커스터마이징이나 확장이 아닌, 다른 메커니즘으로의 **전환**이 필요하다.

---

## 3. 대안: Hibernate `@Filter` 방식

### 개요
Hibernate의 `@FilterDef` + `@Filter`는 Session 레벨에서 동적으로 WHERE 조건을 추가하는 기능이다.
`IN` 절을 지원하므로 N개 tenantId 필터링이 가능하다.

### 변경 후 아키텍처
```
HTTP Request (X-Tenant-Id: tenantA,tenantB)
  → TenantFilter (comma-separated 파싱 → List)
    → TenantContext (ThreadLocal<List<String>>에 저장)
      → TenantFilterActivator (매 요청마다 Session Filter 활성화)
        → @Filter (WHERE tenant_id IN (:tenantIds) 추가)
```

### 변경 후 자동 생성되는 쿼리
```sql
-- 단일 tenantId 요청: X-Tenant-Id: tenantA
SELECT g.id, g.name, g.tenant_id
FROM groups g
WHERE g.tenant_id IN (?)    -- tenantA

-- N개 tenantId 요청: X-Tenant-Id: tenantA,tenantB
SELECT g.id, g.name, g.tenant_id
FROM groups g
WHERE g.tenant_id IN (?, ?)  -- tenantA, tenantB
```

---

## 4. 변경 대상 및 상세 구현

### 4.1 TenantAwareEntity.kt

**Before**
```kotlin
@MappedSuperclass
abstract class TenantAwareEntity(
    @TenantId
    @Column(name = "tenant_id", nullable = false, updatable = false)
    val tenantId: String? = null,
)
```

**After**
```kotlin
@MappedSuperclass
@FilterDef(
    name = "tenantFilter",
    parameters = [ParamDef(name = "tenantIds", type = String::class)]
)
@Filter(name = "tenantFilter", condition = "tenant_id IN (:tenantIds)")
abstract class TenantAwareEntity(
    @Column(name = "tenant_id", nullable = false, updatable = false)
    val tenantId: String? = null,
)
```

- `@TenantId` 제거
- `@FilterDef`: 필터 이름과 파라미터 정의
- `@Filter`: 실제 SQL WHERE 조건 정의

### 4.2 TenantContext.kt

**Before**
```kotlin
object TenantContext {
    private val currentTenant = ThreadLocal<String?>()

    fun setTenantId(tenantId: String?) { currentTenant.set(tenantId) }
    fun getTenantId(): String? = currentTenant.get()
    fun clear() { currentTenant.remove() }
}
```

**After**
```kotlin
object TenantContext {
    private val currentTenants = ThreadLocal<List<String>>()

    fun setTenantIds(tenantIds: List<String>) { currentTenants.set(tenantIds) }
    fun getTenantIds(): List<String> = currentTenants.get() ?: emptyList()
    fun clear() { currentTenants.remove() }
}
```

### 4.3 TenantFilter.kt

**Before**
```kotlin
val tenantId = request.getHeader(TENANT_HEADER)
if (tenantId.isNullOrBlank()) { ... }
TenantContext.setTenantId(tenantId)
```

**After**
```kotlin
val tenantHeader = request.getHeader(TENANT_HEADER)
if (tenantHeader.isNullOrBlank()) { ... }
val tenantIds = tenantHeader.split(",").map { it.trim() }.filter { it.isNotBlank() }
TenantContext.setTenantIds(tenantIds)
```

- `X-Tenant-Id: tenantA,tenantB` 형태의 comma-separated 값 지원

### 4.4 TenantIdentifierResolver.kt

**제거** 또는 **단순화**.
`@TenantId`를 더 이상 사용하지 않으므로 `CurrentTenantIdentifierResolver` 구현이 불필요하다.
다만 Hibernate가 내부적으로 tenant resolver를 요구할 수 있으므로, 첫 번째 tenantId를 반환하는 fallback으로 유지할 수 있다.

### 4.5 신규: HibernateFilterActivator (AOP 또는 Interceptor)

매 트랜잭션/요청마다 Hibernate Session에 Filter를 활성화해야 한다.

**방법 A: Spring AOP**
```kotlin
@Aspect
@Component
class TenantFilterAspect(
    private val entityManager: EntityManager,
) {
    @Before("execution(* com.study.multitenancy.domain.repository..*.*(..))")
    fun activateTenantFilter() {
        val tenantIds = TenantContext.getTenantIds()
        if (tenantIds.isNotEmpty()) {
            val session = entityManager.unwrap(Session::class.java)
            session.enableFilter("tenantFilter")
                .setParameterList("tenantIds", tenantIds)
        }
    }
}
```

**방법 B: HandlerInterceptor**
```kotlin
@Component
class TenantFilterInterceptor(
    private val entityManager: EntityManager,
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val tenantIds = TenantContext.getTenantIds()
        if (tenantIds.isNotEmpty()) {
            val session = entityManager.unwrap(Session::class.java)
            session.enableFilter("tenantFilter")
                .setParameterList("tenantIds", tenantIds)
        }
        return true
    }
}
```

### 4.6 INSERT 시 tenantId 자동 할당

`@TenantId`가 제거되면 INSERT 시 tenantId가 자동 세팅되지 않는다.
`@PrePersist` 콜백으로 대체한다.

```kotlin
@MappedSuperclass
abstract class TenantAwareEntity(
    @Column(name = "tenant_id", nullable = false, updatable = false)
    var tenantId: String? = null,  // val → var 변경 필요
) {
    @PrePersist
    fun assignTenantId() {
        if (tenantId == null) {
            val tenantIds = TenantContext.getTenantIds()
            require(tenantIds.size == 1) {
                "INSERT 시에는 정확히 1개의 tenantId가 필요합니다. 현재: $tenantIds"
            }
            tenantId = tenantIds.first()
        }
    }
}
```

---

## 5. @TenantId vs @Filter 비교

| 항목 | @TenantId (현재) | @Filter (변경 후) |
|------|-----------------|-------------------|
| N개 tenantId | 불가 | 가능 (`IN` 절) |
| 쿼리 자동 필터링 | 자동 (프레임워크 레벨) | 반자동 (Session에서 enable 필요) |
| INSERT 시 tenantId | 자동 할당 | 수동 (`@PrePersist` 등) |
| 필터 누락 위험 | 없음 | 있음 (enable 누락 시 전체 데이터 노출) |
| Hibernate 의존도 | 높음 (ORM 전략) | 중간 (필터 기능) |
| 유연성 | 낮음 | 높음 (동적 조건 변경 가능) |

---

## 6. 위험 요소 및 대응

### 6.1 필터 활성화 누락 → 데이터 유출
- **위험**: `session.enableFilter()`를 호출하지 않으면 tenant 필터 없이 전체 데이터가 조회됨
- **대응**: AOP로 Repository 호출 전 자동 활성화. 테스트에서 필터 없는 조회가 불가능하도록 검증

### 6.2 INSERT 시 다중 tenantId 모호성
- **위험**: tenantId가 2개 이상일 때 어떤 tenant로 INSERT할지 불분명
- **대응**: `@PrePersist`에서 tenantId가 1개가 아니면 예외 발생. 쓰기 작업은 반드시 단일 tenant 컨텍스트에서 수행

### 6.3 연관 엔티티의 tenant 불일치
- **위험**: Space(tenantA)가 Group(tenantB)를 참조하는 등 cross-tenant 참조 가능성
- **대응**: 연관 엔티티 저장 시 tenantId 일치 여부 검증 로직 추가

### 6.4 Lazy Loading 시 필터 미적용
- **위험**: Lazy Loading으로 연관 엔티티 조회 시 Session Filter가 적용되지 않을 수 있음
- **대응**: `@Filter`에 `deduceAliasInjectionPoints = false` 옵션 확인, 필요 시 fetch join 사용
