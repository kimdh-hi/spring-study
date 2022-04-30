package com.study.springdownload.controller

import com.study.springdownload.config.AttachFileProperties
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart
import java.io.File

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@EnableConfigurationProperties(AttachFileProperties::class)
@TestPropertySource("classpath:application.yml")
internal class AttachFileControllerTest(
    val mockMvc: MockMvc,
    val attachFileProperties: AttachFileProperties
) {

    lateinit var basePath: String

    @BeforeEach
    fun init() {
        basePath = attachFileProperties.path
        val file = File(basePath)
        FileUtils.deleteDirectory(file)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    @AfterEach
    fun clear() {
        FileUtils.deleteDirectory(File(basePath))
    }

    @Test
    fun upload() {
        val file = MockMultipartFile("file", "fileName.txt", "text/plain", "file!!".byteInputStream())

        mockMvc.multipart("/upload") {
            file(file)
        }

        assertEquals(1, File(basePath).listFiles().size)
    }
}