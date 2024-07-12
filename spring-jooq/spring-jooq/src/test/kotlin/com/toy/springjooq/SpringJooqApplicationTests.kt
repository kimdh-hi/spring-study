package com.toy.springjooq

import org.jooq.DSLContext
import org.jooq.generated.tables.JActor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringJooqApplicationTests {

    @Autowired
    lateinit var dslContext: DSLContext

    @Test
    fun test() {
        val actors = dslContext.selectFrom(JActor.ACTOR)
            .limit(10)
            .fetch()

        println(actors)
    }

}
