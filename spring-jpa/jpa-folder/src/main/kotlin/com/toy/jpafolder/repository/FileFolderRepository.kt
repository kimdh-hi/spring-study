package com.toy.jpafolder.repository

import com.toy.jpafolder.domain.FileFolder
import org.springframework.data.jpa.repository.JpaRepository

interface FileFolderRepository: JpaRepository<FileFolder, String> {
}