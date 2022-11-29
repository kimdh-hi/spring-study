package com.toy.springgraphql.component.fake

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.InputArgument
import com.toy.springgraphql.datasource.fake.FakeBookDataSource
import com.toy.springgraphqldemo.generated.types.Book
import org.apache.commons.lang3.StringUtils

@DgsComponent
class FakeBookDataResolver {

  @DgsData(parentType = "Query", field = "books")
  fun booksWrittenBy(@InputArgument(name = "author") author: String? = null): List<Book> {
    if(author == null || StringUtils.isBlank(author))
      return FakeBookDataSource.BOOK_LIST

    return FakeBookDataSource.BOOK_LIST
      .filter { StringUtils.containsIgnoreCase(it.author.name, author) }
  }
}