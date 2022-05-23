package com.example.ex.service

import com.example.ex.util.FileUtil
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.Drive.Files
import com.google.api.services.drive.DriveScopes
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

@Service
class GoogleDriveService {

    companion object {
        val CREDENTIALS_FILE_PATH = "src/test/resources/google/credentials.json"
        val TOKEN_FILE_PATH = "src/test/resources/google/token"
        val EXPORT_PATH = "src/test/resources/google/localization-excel.xlsx"

        val JSON_FACTORY = GsonFactory.getDefaultInstance()
        val SCOPES = listOf(SheetsScopes.SPREADSHEETS_READONLY, DriveScopes.DRIVE, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA_READONLY)
        val APPLICATION_NAME = "Google Sheets API"

    }

    private val LOG = LoggerFactory.getLogger(javaClass)

    fun getSpreadSheet(spreadSheetId: String): Spreadsheet {
        return getSheet().spreadsheets()
            .get(spreadSheetId)
            .execute()
    }

    fun export(fileId: String, filePath: String) {
        try {
            FileOutputStream(filePath).use {
                val files = getDrive().files()
                val file = files.get(fileId).execute()
                LOG.debug("export filename: {}", file.name)
                files.export(fileId, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .executeMediaAndDownloadTo(it)
            }
            LOG.debug("export success...")
        } catch (ex: GoogleJsonResponseException) {
            LOG.warn("Token expired...")
            FileUtil.remove("${TOKEN_FILE_PATH}/StoredCredential")
        }
    }

    private fun getSheet(): Sheets {
        val netHttpTransport = getNetHttpTransport()
        return Sheets.Builder(
            netHttpTransport,
            JSON_FACTORY,
            getCredentials(netHttpTransport)
        ).setApplicationName(APPLICATION_NAME).build()
    }

    private fun getDrive(): Drive {
        val netHttpTransport = getNetHttpTransport()
        return Drive.Builder(
            netHttpTransport,
            JSON_FACTORY,
            getCredentials(netHttpTransport)
        ).setApplicationName(APPLICATION_NAME).build()
    }

    fun getCredentials(httpTransport: NetHttpTransport): Credential {
        val clientSecrets = FileInputStream(CREDENTIALS_FILE_PATH).use {
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(it))
        }

        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport,
            JSON_FACTORY,
            clientSecrets,
            SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(File(TOKEN_FILE_PATH)))
            .setAccessType("offline")
            .build()

        val receiver = LocalServerReceiver.Builder()
            .setPort(8888)
            .build()

        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    private fun getNetHttpTransport() = GoogleNetHttpTransport.newTrustedTransport()
}