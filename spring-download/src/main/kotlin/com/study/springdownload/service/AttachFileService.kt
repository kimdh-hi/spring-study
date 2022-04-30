package com.study.springdownload.service

import com.study.springdownload.config.AttachFileProperties
import com.study.springdownload.domain.AttachFile
import com.study.springdownload.repository.AttachFileRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Paths
import java.util.UUID
import javax.servlet.http.HttpServletRequest

@Transactional(readOnly = true)
@Service
class AttachFileService(
    private val attachFileRepository: AttachFileRepository,
    private val attachFileProperties: AttachFileProperties) {

    @Transactional
    fun upload(file: List<MultipartFile>, request: HttpServletRequest) {
        val basePath = attachFileProperties.path
        val uploadPath = Paths.get(basePath).toAbsolutePath().normalize()
        if (!File(basePath).exists()) {
            File(basePath).mkdirs()
        }
        file.forEach { f ->
            val fileName = f.originalFilename
            val saveFileName = fileName?.lastIndexOf(".")?.let {
                UUID.randomUUID().toString() + fileName.substring(it)
            } ?: UUID.randomUUID().toString()

            f.transferTo(File("$uploadPath/$saveFileName"))

            val attachFile = AttachFile.newInstance(saveFileName, "$uploadPath/$saveFileName")
            attachFileRepository.save(attachFile)
        }
    }
}