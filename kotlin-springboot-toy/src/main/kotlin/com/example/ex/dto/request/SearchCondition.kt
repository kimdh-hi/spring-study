package com.example.ex.dto.request

import org.springframework.data.domain.Pageable

data class SearchCondition (

    var page: Int = 1,
    var size: Int = 10,
    var sort: String? = null,
    var isAsc: Boolean? = null
)