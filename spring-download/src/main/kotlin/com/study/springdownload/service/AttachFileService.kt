package com.study.springdownload.service

import com.study.springdownload.domain.AttachFile
import com.study.springdownload.repository.AttachFileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID
import javax.servlet.http.HttpServletRequest

@Transactional(readOnly = true)
@Service
class AttachFileService(private val attachFileRepository: AttachFileRepository) {

    private val uploadDir = "/resources/files"

    @Transactional
    fun upload(file: List<MultipartFile>, request: HttpServletRequest) {
        val uploadPath = File(request.servletContext.getRealPath(uploadDir))
        if (!uploadPath.exists()) {
            uploadPath.mkdirs()
        }
        file.forEach { f ->
            val fileName = f.originalFilename
            val saveFileName = fileName?.lastIndexOf(".")?.let {
                UUID.randomUUID().toString() + fileName.substring(it)
            } ?: UUID.randomUUID().toString()

            f.transferTo(File("$uploadDir/$saveFileName"))

            val attachFile = AttachFile.newInstance(saveFileName, "$uploadDir/$saveFileName")
            attachFileRepository.save(attachFile)
        }
    }
}