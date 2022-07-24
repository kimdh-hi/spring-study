package com.example.ex.service

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.services.sheets.v4.Sheets
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class GoogleDriveServiceTest(val googleDriveService: GoogleDriveService) {

    val SHEET_ID = "18hro2qcH1RfGJw2K0m9_40ns7HSDZ3ioWkJAWuXxiEs"

    @Test
    fun `구글 드라이브에서 엑셀파일 가져오기`() {
        googleDriveService.export(SHEET_ID, GoogleDriveService.EXPORT_PATH)
    }

    @Test
    fun getSpreadSheet() {
        val spreadSheet = googleDriveService.getSpreadSheet(SHEET_ID)

        assertAll({
            assertNotNull(spreadSheet)
            assertEquals(SHEET_ID, spreadSheet.spreadsheetId)
        })
    }
}