package com.example.ex.util

import com.example.ex.service.GoogleDriveService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class ExcelToResourcesTest(val googleDriveService: GoogleDriveService) {

    val fileId = "18hro2qcH1RfGJw2K0m9_40ns7HSDZ3ioWkJAWuXxiEs"
    val messagePropertiesBasePath = "src/main/resources/message"

    @Test
    fun exportExcel() {
        googleDriveService.export(fileId, GoogleDriveService.EXPORT_PATH)
    }

    @Test
    fun toResources() {
        val excel = GoogleDriveService.EXPORT_PATH
        ExcelToResources.generate(excel, messagePropertiesBasePath)
    }
}