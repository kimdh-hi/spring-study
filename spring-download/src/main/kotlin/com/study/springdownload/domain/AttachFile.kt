package com.study.springdownload.domain

import javax.persistence.*

@Table(name = "tb_attach_file")
@Entity
open class AttachFile (
    fileName: String,
    savePath: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var fileName = fileName
        protected set

    var savePath = savePath
        protected set

    companion object {
        fun newInstance(fileName: String, savePath: String) : AttachFile {
            return AttachFile(
                fileName = fileName,
                savePath = savePath
            )
        }
    }
}