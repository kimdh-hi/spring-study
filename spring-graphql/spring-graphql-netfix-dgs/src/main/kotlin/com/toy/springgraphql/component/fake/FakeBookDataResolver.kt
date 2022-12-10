package com.toy.springgraphql.component.fake

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.toy.springgraphql.datasource.fake.FakeBookDataSource
import com.toy.springgraphql.generated.DgsConstants
import com.toy.springgraphql.generated.types.Book
import com.toy.springgraphql.generated.types.ReleaseHistory
import com.toy.springgraphql.generated.types.ReleaseHistoryInput
import graphql.schema.DataFetchingEnvironment
import org.apache.commons.lang3.StringUtils

@DgsComponent
class FakeBookDataResolver {

  @DgsQuery(field = "books")
  fun booksWrittenBy(@InputArgument(name = "author") author: String? = null): List<Book> {
    if(author == null || StringUtils.isBlank(author))
      return FakeBookDataSource.BOOK_LIST

    return FakeBookDataSource.BOOK_LIST
      .filter { StringUtils.containsIgnoreCase(it.author.name, author) }
  }

  @DgsData(
    parentType = DgsConstants.QUERY_TYPE,
    field = DgsConstants.QUERY.BooksByReleased
  )
  fun getBooksByReleased(dataFetchingEnvironment: DataFetchingEnvironment): List<Book> {
    val releasedMap = dataFetchingEnvironment.getArgument<Map<String, Any>>("releasedInput")
    val releaseInput = ReleaseHistoryInput(
      printedEdition = releasedMap[DgsConstants.RELEASEHISTORYINPUT.PrintedEdition] as Boolean,
      year = releasedMap[DgsConstants.RELEASEHISTORYINPUT.Year] as Int
    )
    return FakeBookDataSource.BOOK_LIST
      .filter { matchReleaseHistory(releaseInput, it.released) }
  }

  private fun matchReleaseHistory(input: ReleaseHistoryInput, element: ReleaseHistory?): Boolean {
    return (input.printedEdition == element?.printedEdition) && (input.year == element?.year)
  }
}