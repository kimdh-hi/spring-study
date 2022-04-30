package com.study.springdownload.controller

import com.study.springdownload.repository.AttachFileRepository
import com.study.springdownload.service.AttachFileService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@Controller
class AttachFileController(
    private val attachFileService: AttachFileService,
    private val attachFileRepository: AttachFileRepository) {

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
}