package com.study.springdownload.controller

import com.study.springdownload.repository.AttachFileRepository
import com.study.springdownload.service.AttachFileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class AttachFileController(
    private val attachFileService: AttachFileService,
    private val attachFileRepository: AttachFileRepository) {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/home")
    fun home(model: Model) : String{
        val files = attachFileRepository.findAll()
        model.addAttribute("files", files)
        return "home"
    }

    @PostMapping("/upload")
    fun upload(file: List<MultipartFile>, request: HttpServletRequest): String {
        attachFileService.upload(file, request)

        return "redirect:/home"
    }

    @GetMapping("/download/{id}")
    fun download(@PathVariable id: Long, response: HttpServletResponse) {
        log.info("download id: {}", id)
        attachFileService.download(id, response)
    }
}