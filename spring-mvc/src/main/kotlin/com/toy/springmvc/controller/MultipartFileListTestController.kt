package com.toy.springmvc.controller

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

    @InitBinder("multipartFileListTestVO")
    protected fun initBinder(binder: WebDataBinder) {
        binder.addValidators(MultipartFileListTestValidator())
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun test(vo: MultipartFileListTestVO): String {
        vo.files.forEach {
            log.info(it.name)
        }

        return "ok"
    }
}

data class MultipartFileListTestVO(
    val files: List<MultipartFile>,
)

data class MultipartFileListTestInnerVO(
    val file1: MultipartFile,
    val files: List<MultipartFile>
)

class MultipartFileListTestValidator: Validator {
    override fun supports(clazz: Class<*>): Boolean = MultipartFileListTestVO::class.java.isAssignableFrom(clazz)

    override fun validate(target: Any, errors: Errors) {
        val vo = target as MultipartFileListTestVO

    }
}