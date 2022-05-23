package com.example.ex.controller.api

import com.example.ex.common.MessageSourceService
import com.example.ex.controller.vo.RequestVO
import com.example.ex.domain.Member
import com.example.ex.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RequestMapping("/api")
@RestController
class ApiTestController(private val memberRepository: MemberRepository): ApiExceptionHandler() {

    private val LOG = LoggerFactory.getLogger(javaClass)

    @GetMapping("/exception")
    fun ex() {
        throw IllegalStateException("ex")
    }

    @PostMapping("/vo")
    fun vo(@RequestBody @Valid requestVO: RequestVO, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            LOG.error("error")
        }
        LOG.info("requestVO = {}", requestVO)
        return "ok"
    }

    @GetMapping("/members")
    fun members(@RequestBody username: String, pageable: Pageable): ResponseEntity<Page<Member>> {
        val list = memberRepository.findAllByUsername(username, pageable)

        return ResponseEntity.ok(list)
    }

    data class MemberVO (val username: String)
}