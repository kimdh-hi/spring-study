package com.example.ex.dto.request.search

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.domain.Pageable
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class SearchRequest (

    val page: Int = 1,
    val size: Int = 10,

    val sortBy: SortProperty? = null,
    val isAsc: Boolean? = null,

    val searchType: SearchType? = null,
    val query: String? = null,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    val startDate: LocalDateTime? = null,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    val endDate: LocalDateTime? = null,
) {
    enum class SearchType {
        TITLE, CONTENT, TITLE_CONTENT, WRITER
    }

    enum class SortProperty {
        TITLE, WRITER, DATE
    }
}