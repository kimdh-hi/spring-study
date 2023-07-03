package com.toy.springmvc.controller

import com.toy.springmvc.config.NoArg
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/multipartFile-list")
class MultipartFileListTestController {

    private val log = LoggerFactory.getLogger(javaClass)


    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun test(request: EmoticonSaveRequest): String {

        return "ok"
    }
}

data class EmoticonSaveRequest(
    val i18ns: MutableList<EmoticonI18nRequest> = mutableListOf()
)

@NoArg
data class EmoticonI18nRequest(
    var name: String,
    var representativeFile: MultipartFile,
//    val emoticonFiles: List<MultipartFile>
)
