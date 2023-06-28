package com.toy.springmvc.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/init-binder")
class InitBinderTestController {

    @InitBinder("initBinderTestVO")
    protected fun initBinder(binder: WebDataBinder) {
        binder.addValidators(InitBinderTestValidator())
    }

    @PostMapping
    fun save(@RequestBody vo: InitBinderTestVO): ResponseEntity<InitBinderTestVO> {
        return ResponseEntity.ok(vo)
    }

}

data class InitBinderTestVO(
    @field:NotBlank
    val data: String,
) {
    var someData: String? = null
}

class InitBinderTestValidator: Validator {
    override fun supports(clazz: Class<*>): Boolean = clazz.isAssignableFrom(InitBinderTestVO::class.java)

    override fun validate(target: Any, errors: Errors) {
        val vo = target as InitBinderTestVO
        vo.someData = vo.data
    }
}
