package com.example.ex.util

import com.example.ex.common.PatternConstants
import org.apache.commons.lang.StringUtils
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.IOException
import java.io.OutputStream
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletResponse

object ExcelUtil {


    private fun <E> writeContent(data: List<E>, fields: List<Field>, sheet: Sheet) {
        var rowNum = 1
        data.forEach {
            val row = sheet.createRow(rowNum++)
            writeContentToCell(it, fields, row)
        }
    }

    private fun <E> writeContentToCell(data: E, fields: List<Field>, row: Row) {
        var cellNum = 0
        fields.forEach {
            convertToCellValue(it.get(data))?.let { cellValue ->
                val cell = row.createCell(cellNum++)
                cell.setCellValue(cellValue)
            }
        }
    }

    private fun writeHeader(fields: List<Field>, sheet: Sheet) {
        val headerRow = sheet.createRow(0)
        var cellNum = 0
        fields.forEach {
            val cell = headerRow.createCell(cellNum++)
            cell.setCellValue(it.name)
        }
    }

    private fun <E> getFields(clazz: Class<E>) :List<Field> {
        val declaredFields = clazz.declaredFields
        AccessibleObject.setAccessible(declaredFields, true)
        return declaredFields.asList()
    }

    private fun createSheet(wb: Workbook, sheetName: String) = wb.createSheet(sheetName)

    fun getStringCellValue(cell: Cell?) = getStringCellValue(cell, null)

    fun getStringCellValue(cell: Cell?, key: String?): String? {
        if (cell == null)
            return if(StringUtils.isBlank(key)) null else key

        return when (cell.cellType) {
            CellType.NUMERIC -> cell.numericCellValue.toString()
            CellType.STRING -> cell.stringCellValue
            CellType.FORMULA -> cell.numericCellValue.toString()
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            else -> null
        }
    }

    private fun convertToCellValue(value: Any?) : String? {
        return value?.let {
            when (it) {
                is Boolean -> if (it) "Y" else "N"
                is LocalDateTime -> it.format(DateTimeFormatter.ofPattern(PatternConstants.LOCAL_DATE_TIME_PATTERN))
                is LocalDate -> it.format(DateTimeFormatter.ofPattern(PatternConstants.LOCAL_DATE_PATTERN))
                else -> it.toString()
            }
        }
    }

    private fun download(wb: Workbook, response: HttpServletResponse, fileName: String) {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val saveFileName = String.format("%s_%s.xlsx", fileName, currentDate)

        getDownloadOutputStream(response, saveFileName)?.use {
            try {
                wb.write(it)
                it.flush()
            } catch (ex: IOException) {
                throw RuntimeException("엑셀파일 다운로드 에러: ", ex)
            } finally {
                wb.use {
                    val sxssfWb = it as? SXSSFWorkbook
                    sxssfWb?.dispose()
                }
            }
        }

        wb.use {
            try {
                val sxssWb = it as SXSSFWorkbook
                sxssWb.dispose()
            } catch (ex: IOException) { throw RuntimeException("WorkBook not closed error: ", ex)}
        }
    }

    private fun getDownloadOutputStream(response: HttpServletResponse, fileName: String) : OutputStream? {
        response.contentType = "application/octet-stream; charset=UTF-8"
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=\"" + String(
                fileName.toByteArray(),
                StandardCharsets.ISO_8859_1
            ) + "\";"
        )
        response.setHeader("Content-Transfer-Encoding", "binary")
        return response.outputStream
    }

}