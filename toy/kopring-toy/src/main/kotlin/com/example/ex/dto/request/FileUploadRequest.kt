package com.example.ex.dto.request

import org.springframework.web.multipart.MultipartFile

data class FileUploadRequest (
    var fileName: String,

    val files: MutableList<MultipartFile>
)