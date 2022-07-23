package com.example.ex.controller

import com.example.ex.dto.request.FileUploadRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
class AttachFileController {

    @PostMapping("/api/files", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun save(@ModelAttribute fileUploadRequest: FileUploadRequest) : String{

        println("save")

        fileUploadRequest.files.forEach {
            println(it.originalFilename)
        }

        return "ok"
    }
}