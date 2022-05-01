package com.study.springdownload.service

import com.study.springdownload.config.AttachFileProperties
import com.study.springdownload.domain.AttachFile
import com.study.springdownload.repository.AttachFileRepository
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URLEncoder
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Transactional(readOnly = true)
@Service
class AttachFileService(
    private val attachFileRepository: AttachFileRepository,
    private val attachFileProperties: AttachFileProperties) {

    val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun upload(file: List<MultipartFile>, request: HttpServletRequest) {
        val basePath = attachFileProperties.path
        val uploadPath = Paths.get(basePath).toAbsolutePath().normalize()
        if (!File(basePath).exists()) {
            File(basePath).mkdirs()
        }
        file.forEach { f ->
            var originalFileName = f.originalFilename
            val saveFileName = originalFileName?.lastIndexOf(".")?.let {
                UUID.randomUUID().toString() + originalFileName?.substring(it)
            } ?: UUID.randomUUID().toString()

            originalFileName = originalFileName ?: saveFileName

            f.transferTo(File("$uploadPath/$saveFileName"))

            val attachFile = AttachFile.newInstance(originalFileName, "$uploadPath/$saveFileName")
            attachFileRepository.save(attachFile)
        }
    }

    fun download(id: Long, response: HttpServletResponse) {
        val attachFile = attachFileRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("존재하지 않는 파일입니다.")
        }

        val file = File(attachFile.savePath)

        response.contentType = "application/octet-stream";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(attachFile.fileName, "UTF-8") + "\";")
        response.setHeader("Content-Transfer-Encoding", "binary");

        val bytes = FileUtils.readFileToByteArray(file)

        response.outputStream.write(bytes)
        response.outputStream.flush()
        response.outputStream.close()
    }
}