package com.toy.springmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMultipartHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import java.io.File
import java.io.FileInputStream

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractBaseController {

    @Autowired protected lateinit var mockMvc: MockMvc
    @Autowired protected lateinit var objectMapper: ObjectMapper

    protected fun MockMultipartHttpServletRequestDsl.uploadImage(parameterName: String) {
        val fis = FileInputStream(File("src/test/resources/images/cat.jpg"))
        val mockMultipartFile = MockMultipartFile(parameterName, "cat.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, fis)
        file(mockMultipartFile)
    }

}