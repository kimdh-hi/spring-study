# Hibernate Multitenancy

멀티테넌시(Multitenancy)란 하나의 애플리케이션 인스턴스가 여러 테넌트(고객/조직)의 데이터를 격리하여 서비스하는 아키텍처 패턴이다.

Hibernate는 3가지 멀티테넌시 전략을 제공한다.

## 1. Separate Database (데이터베이스 분리)

- 테넌트마다 **별도의 데이터베이스**를 사용한다.
- 가장 높은 수준의 데이터 격리를 제공한다.
- 테넌트별로 완전히 독립된 스키마와 데이터를 가진다.
- 데이터베이스 수가 테넌트 수에 비례하여 증가하므로 운영 비용이 높다.

```
Tenant A → Database A
Tenant B → Database B
Tenant C → Database C
```

## 2. Separate Schema (스키마 분리)

- 하나의 데이터베이스 내에서 테넌트마다 **별도의 스키마**를 사용한다.
- 데이터베이스 분리보다 리소스 효율적이면서도 논리적 격리를 유지한다.
- Hibernate가 `MultiTenantConnectionProvider`를 통해 테넌트별 스키마로 커넥션을 전환한다.

```
Database
  ├── Schema: tenant_a
  ├── Schema: tenant_b
  └── Schema: tenant_c
```

## 3. Partitioned (Discriminator) Data (파티션 데이터)

- 모든 테넌트가 **동일한 데이터베이스, 동일한 스키마, 동일한 테이블**을 공유한다.
- 각 테이블에 `tenant_id` 같은 식별 컬럼(discriminator)을 두어 데이터를 구분한다.
- 리소스 효율이 가장 높지만, 데이터 격리 수준이 가장 낮다.
- `@TenantId` 애노테이션으로 엔티티 필드를 지정하면 Hibernate가 자동으로 쿼리 필터링 및 테넌트 격리를 처리한다.

```
+----+-----------+--------+-------+
| id | tenant_id | name   | email |
+----+-----------+--------+-------+
|  1 | A         | user1  | ...   |
|  2 | B         | user2  | ...   |
|  3 | A         | user3  | ...   |
+----+-----------+--------+-------+
```

## 비교

| 전략 | 데이터 격리 | 리소스 효율 | 운영 복잡도 |
|---|---|---|---|
| Separate Database | 높음 | 낮음 | 높음 |
| Separate Schema | 중간 | 중간 | 중간 |
| Partitioned (Discriminator) Data | 낮음 | 높음 | 낮음 |

## 핵심 인터페이스

- `CurrentTenantIdentifierResolver` - 현재 요청의 테넌트를 식별한다.
- `MultiTenantConnectionProvider` - 테넌트에 맞는 커넥션을 제공한다. (Database/Schema 전략에서 사용)
- `@TenantId` - 엔티티의 테넌트 식별 필드를 지정한다. (Discriminator 전략에서 사용)
