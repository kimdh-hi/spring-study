package com.toy.jpafolder.vo

import com.querydsl.core.annotations.QueryProjection
import com.toy.jpafolder.domain.FileFolder

data class FileFolderResponseVO(
  val id: String,
  val name: String,
  val childFolders: List<FileFolderResponseVO>
) {
  @QueryProjection constructor(
    fileFolder: FileFolder
  ): this(fileFolder.id!!, fileFolder.name, fileFolder.childFolders.map { of(it) })

  companion object {
    fun of(fileFolder: FileFolder): FileFolderResponseVO = FileFolderResponseVO(
      id = fileFolder.id!!,
      name = fileFolder.name,
      childFolders = fileFolder.childFolders.map { of(it) }
    )
  }
}