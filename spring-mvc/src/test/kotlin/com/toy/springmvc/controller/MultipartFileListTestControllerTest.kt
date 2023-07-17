package com.toy.springmvc.controller

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.multipart

class MultipartFileListTestControllerTest: AbstractBaseController() {

    @Test
    fun test() {
        mockMvc.multipart("/multipartFile-list") {
            param("data", " ")
            uploadImage("files")
            uploadImage("files")
        }
    }
}

