package `00-base`

import reactor.core.publisher.Flux

class UserService {

  fun getUsers(): Flux<User> = Flux.range(1, 2)
    .map { User.newUser(it.toLong()) }
}