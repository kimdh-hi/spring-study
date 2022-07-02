package com.example.ex.controller

import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

@Controller
class HomeController {

    @GetMapping("/file-upload")
    fun file() = "index"

    @ResponseBody
    @PostMapping("/file-upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun test(@RequestPart files: List<MultipartFile>) : String {
        files.forEach {
            println(it.originalFilename)
        }

        return "ok"
    }
}