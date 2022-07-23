package com.example.ex.util

import org.apache.commons.configuration.PropertiesConfiguration
import org.apache.commons.lang.StringUtils
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.util.*
import java.util.stream.Collectors.toList
import java.util.stream.IntStream

object ExcelToResources {

    val LOG = LoggerFactory.getLogger(javaClass)

    private const val RESOURCE_FILE_PATH_FORMAT = "%s/%s_%s.properties"
    private const val DEFAULT_RESOURCE_FILE_PATH_FORMAT = "%s/%s.properties"
    private const val HEADER_ROW_NUM = 0
    private const val FIRST_ROWNUM = HEADER_ROW_NUM + 1
    private const val LANGUAGE_KEY_CELL_NUM = 0
    private const val FIRST_CELLNUM = LANGUAGE_KEY_CELL_NUM + 1

    fun generate(sourceExcelFilePath: String, targetDirPath: String) {
        try {
            process(sourceExcelFilePath, targetDirPath)
        } catch (ex: Exception) {
            LOG.error(ex.message)
        }
    }

    private fun process(excelFilePath: String, targetDirPath: String) {
        val wb = XSSFWorkbook(FileInputStream(excelFilePath))
        wb.sheetIterator().forEach { sheet ->
            generateResources(sheet as XSSFSheet, targetDirPath)
        }
    }

    private fun generateResources(sheet: XSSFSheet, targetDirPath: String) {
        val sheetName = sheet.sheetName

        getResourceVOs(sheet).forEach {
            generateDefaultPropertiesResource(it, targetDirPath, sheetName)
            generatePropertiesResource(it, String.format(RESOURCE_FILE_PATH_FORMAT, targetDirPath, sheetName, it.language))
        }
    }

    private fun getResourceVOs(sheet: XSSFSheet): List<ResourceVO> {
        val header = sheet.getRow(HEADER_ROW_NUM)
        val resourceVOs = createResourceVOs(header)

        for (i in FIRST_ROWNUM .. sheet.lastRowNum) {
            val row = sheet.getRow(i)
            storeLanguageResourcesFromCellValues(row, resourceVOs)
        }

        return resourceVOs
    }

    private fun generateDefaultPropertiesResource(resourceVO: ResourceVO, targetDirPath: String, sheetName: String) {
        if (resourceVO.isDefaultLanguage()) {
            generatePropertiesResource(resourceVO, String.format(DEFAULT_RESOURCE_FILE_PATH_FORMAT, targetDirPath, sheetName))
        }
    }

    private fun generatePropertiesResource(resourceVO: ResourceVO, resourceFilePath: String) {
        val config = PropertiesConfiguration()
        config.isDelimiterParsingDisabled = true
        config.encoding = "UTF-8"
        resourceVO.getResourcesEntrySet().forEach { entry ->
            config.addProperty(entry.key, entry.value)
        }
        config.save(resourceFilePath)
    }

    private fun storeLanguageResourcesFromCellValues(row: XSSFRow, resourceVOs: List<ResourceVO>) {
        if(isEmptyLanguageResourceRow(row))
            return

        val key = ExcelUtil.getStringCellValue(row.getCell(LANGUAGE_KEY_CELL_NUM))
        val totalLanguageCount = resourceVOs.size
        for (i in 0 until totalLanguageCount) {
            val cell = row.getCell(i + FIRST_CELLNUM)
            val value = StringUtils.defaultIfBlank(ExcelUtil.getStringCellValue(cell, key), key)

            resourceVOs.get(i)
                .addLanguageResource(key!!, value!!)
        }

    }

    private fun createResourceVOs(header: XSSFRow): List<ResourceVO> {
        return IntStream.range(FIRST_ROWNUM, header.physicalNumberOfCells)
            .mapToObj(header::getCell)
            .map(ExcelUtil::getStringCellValue)
            .filter(StringUtils::isNotBlank)
            .map(ResourceVO.Companion::newInstance)
            .collect(toList())
    }

    private fun isEmptyLanguageResourceRow(row: XSSFRow?): Boolean {
        return row == null || StringUtils.isBlank(ExcelUtil.getStringCellValue(row.getCell(LANGUAGE_KEY_CELL_NUM)))
    }

}

data class ResourceVO (
    val language: String?,
    val resources: SortedMap<String, String>
) {

    init {
        checkLanguage()
    }

    companion object {
        fun newInstance(language: String?): ResourceVO {
            return ResourceVO(language, TreeMap())
        }

        const val DEFAULT_LANGUAGE = "ko"
        private val LANGUAGES = listOf(DEFAULT_LANGUAGE, "ko", "en", "jp", "zh_CN", "zh_TW")
    }

    private fun checkLanguage() {
        val exists = LANGUAGES.any {
            StringUtils.equals(it, language)
        }

        if (!exists)
            throw RuntimeException("$language is not supported language...")
    }

    fun addLanguageResource(key: String, value: String) = resources.put(key, value)

    fun getResourcesEntrySet() = resources.entries

    fun isDefaultLanguage() = StringUtils.equals(language, DEFAULT_LANGUAGE)
}