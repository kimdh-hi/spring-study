package com.study.springdownload.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart
import java.io.File

@SpringBootTest
@AutoConfigureMockMvc
class AttachFileControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    private val basePath = File("")

//    @BeforeEach
//    fun init() {
//        FileUtils.deleteDirectory(basePath)
//    }
//
//    @AfterEach
//    fun clear() {
//        FileUtils.deleteDirectory(basePath)
//    }

    @Test
    fun upload() {
        val file = MockMultipartFile("file", "fileName.txt", "text/plain", "file!!".byteInputStream())

        mockMvc.multipart("/upload") {
            file(file)
        }
    }
}