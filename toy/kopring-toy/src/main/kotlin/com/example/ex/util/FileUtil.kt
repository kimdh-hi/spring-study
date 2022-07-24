package com.example.ex.util

import org.apache.commons.io.FilenameUtils
import java.io.File

object FileUtil {

    val ALLOW_IMAGE_EXTENSION = listOf("jpg", "jpeg", "gif", "png", "ico")
    val ALLOW_EXCEL_EXTENSION = listOf("xlsx")
    val ALLOW_ETC_EXTENSION = listOf("txt")

    fun remove(filePath: String) {
        remove(File(filePath))
    }

    private fun remove(file: File) {
        if (!file.exists())
            return
        try {
            file.delete()
        } catch (ex: SecurityException) {
            throw RuntimeException(ex)
        }
    }

    fun getFileExtension(filePath: String) = FilenameUtil.getExtension(filePath)


    fun getFileName(filePath: String) = FilenameUtil.getName(filePath)
}

object FilenameUtil {

    fun getName(filename: String): String? {
        val filename = File(filename).name

        if (filename.isBlank())
            return null

        val index = FilenameUtils.indexOfLastSeparator(filename)
        return filename.substring(index + 1)
    }

    fun getExtension(filename: String): String? {
        val filename = File(filename).name

        if (filename.isBlank())
            return null

        val index = FilenameUtils.indexOfExtension(filename)
        return if (index == -1) "" else filename.substring(index + 1)
    }
}