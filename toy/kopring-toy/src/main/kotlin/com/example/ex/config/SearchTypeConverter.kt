package com.example.ex.config

import com.example.ex.dto.request.search.SearchRequest.SearchType
import org.springframework.core.convert.converter.Converter

class SearchTypeConverter: Converter<String, SearchType> {

    override fun convert(source: String): SearchType {
        return SearchType.valueOf(source.uppercase())
    }
}