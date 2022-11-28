package com.toy.springgraphqldemo.component.fake

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.toy.springgraphqldemo.datasource.fake.FakeHelloDataSource
import com.toy.springgraphqldemo.generated.types.Hello
import java.util.concurrent.ThreadLocalRandom

@DgsComponent
class FakeHelloDataResolver {

  /**
   * 쿼리 메서드 이름은 스키마의 이름과 동일해야 함
   */

  @DgsQuery
  fun allHellos(): List<Hello> {
    return FakeHelloDataSource.HELLO_LIST.toList()
  }

  @DgsQuery
  fun oneHello(): Hello {
    return FakeHelloDataSource.HELLO_LIST.get(
      ThreadLocalRandom.current().nextInt(FakeHelloDataSource.HELLO_LIST.size)
    )
  }
}