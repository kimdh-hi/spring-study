package com.study.springdownload.controller

import com.study.springdownload.service.AttachFileService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@Controller
class AttachFileController(private val attachFileService: AttachFileService) {

    @GetMapping("/home")
    fun home() = "home"

    @PostMapping("/upload")
    fun upload(file: List<MultipartFile>, request: HttpServletRequest) {
        attachFileService.upload(file, request)
    }
}