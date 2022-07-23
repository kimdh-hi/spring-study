package com.example.ex.controller.api

import org.slf4j.LoggerFactory
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RequestMapping("/api/files")
@RestController
class FileApiController {

    val log = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun test(): String {

        val path1 = ResourceUtils.getFile("test1.txt").canonicalPath
        val path2 = ResourceUtils.getFile("test2.txt").canonicalPath
        log.info("path1: {}, path2: {}", path1, path2)
        log.info("path-parent1: {}, path-parent2: {}", File(path1).parent, File(path2).parent)


        val file1 = ResourceUtils.getFile("test1.txt").canonicalFile
        val file2 = ResourceUtils.getFile("test2.txt").canonicalFile
        log.info("file1: {}, file2: {}", file1, file2)

        return "test"
    }


}