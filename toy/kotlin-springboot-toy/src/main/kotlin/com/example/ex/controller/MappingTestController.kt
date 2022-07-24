package com.example.ex.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/mapping")
@RestController
class MappingTestController {

    @PostMapping
    fun test(@RequestBody requestDto: RequestDto) : String{

        println("test $requestDto")

        return "ok"
    }
}

data class RequestDto (
    var id: String? = null,
    var name: String? = null,
) {
    init {
        println("init id=$id, name=$name")
    }
}