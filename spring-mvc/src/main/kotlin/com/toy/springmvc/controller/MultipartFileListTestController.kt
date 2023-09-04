package com.toy.springmvc.controller

import com.toy.springmvc.common.MultipartFileConstraint
import com.toy.springmvc.config.NoArg
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

@RestController
@RequestMapping("/multipartFileList")
class MultipartFileListTestController {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun test(@Valid request: EmoticonSaveRequest, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.allErrors.joinToString(",") { it.defaultMessage ?: it.objectName })
        }

        return "ok"
    }
}

data class EmoticonSaveRequest(
    @field:NotEmpty
    val i18ns: MutableList<EmoticonI18nRequest> = mutableListOf()
)

@NoArg
data class EmoticonI18nRequest(
    @field:NotBlank
    var name: String,

    var representativeFile: MultipartFile,

    var emoticonFiles: MutableList<MultipartFile> = mutableListOf()
)
