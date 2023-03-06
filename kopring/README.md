## Kotlin + Spring


### Spring plugin
- `@Component`
- `@Transactional`
- `@Async`
- `@Cacheable`
- `@SpringBootTest`

위 어노테이션이 붙은 클래스 혹은 함수를 open 으로 만들어준다.<br/>
open 하는 이유는 프록시 기반을 동작하기 위함이다.<br/>

### Jpa plugin
`Entity`, `MappedSuperClass`, `Embeddable` 클래스에 대해 기본생성자를 자동생성한다.<br/>
jpa 표준에 따르면 위 클래스는 기본 생성자를 필요로 한다.<br/>
기본 생성자가 필요한 이유는 Jpa 가 인스턴스 생성시 리플랙션을 사용하기 때문이다.<br/>


### allopen plugin
`Entity`, `MappedSuperClass`, `Embeddable` 클래스에 대해 상속가능하도록 하기 위함이다.<br/>
open 하는 이유는 프록시 기반으로 동작하기 위함이다.<br/>
프록시 기반으로 `Lazy loading` 등의 기능을 사용하기 위함이다.<br/>

