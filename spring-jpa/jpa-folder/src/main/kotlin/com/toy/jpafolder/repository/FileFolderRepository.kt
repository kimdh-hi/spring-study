package com.toy.jpafolder.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.jpafolder.domain.FileFolder
import com.toy.jpafolder.domain.QFileFolder
import com.toy.jpafolder.domain.QFileFolder.fileFolder
import com.toy.jpafolder.vo.FileFolderResponseVO
import com.toy.jpafolder.vo.QFileFolderResponseVO
import org.springframework.data.jpa.repository.JpaRepository

interface FileFolderRepository: JpaRepository<FileFolder, String>, FileFolderRepositoryCustom

interface FileFolderRepositoryCustom {
  fun getList(): List<FileFolderResponseVO>
}

class FileFolderRepositoryImpl(
  private val query: JPAQueryFactory
): FileFolderRepositoryCustom {
  override fun getList(): List<FileFolderResponseVO> {
    return query.select(
      QFileFolderResponseVO(fileFolder)
    )
      .from(fileFolder)
      .fetch()
  }
}