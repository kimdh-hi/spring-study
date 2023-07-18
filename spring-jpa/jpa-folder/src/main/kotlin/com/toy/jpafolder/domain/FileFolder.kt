package com.toy.jpafolder.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "tb_file_folder")
class FileFolder(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  @OneToMany(mappedBy = "folder", cascade = [CascadeType.ALL])
  val files: MutableList<File> = mutableListOf(),

  @ManyToOne
  @JoinColumn(name = "parent_folder_id")
  val parentFolder: FileFolder? = null,

  @OneToMany(mappedBy = "parentFolder", cascade = [CascadeType.ALL])
  val childFolders: MutableList<FileFolder> = mutableListOf()
)