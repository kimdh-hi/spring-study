package `08-file-service`

import reactor.core.publisher.Mono
import java.io.FileReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FileService {

  private val BASE_PATH = Paths.get("src/main/resources/files")

  fun fileRead(fileName: String) = Mono.fromSupplier{ read(fileName) }
  fun fileSave(fileName: String, content: String) = Mono.fromSupplier { save(fileName, content) }
  fun fileDelete(fileName: String) = Mono.fromSupplier{ delete(fileName) }

  private fun read(fileName: String): String {
    try {
      return Files.readString(getResolvedPath(fileName))
    } catch (e: IOException) {
      throw RuntimeException(e.message)
    }
  }

  private fun save(fileName: String, content: String): Path {
    try {
      return Files.writeString(getResolvedPath(fileName), content)
    } catch (e: IOException) {
      throw RuntimeException(e.message)
    }
  }

  private fun delete(fileName: String): Boolean {
    try {
      return Files.deleteIfExists(getResolvedPath(fileName))
    } catch (e: IOException) {
      throw RuntimeException(e.message)
    }
  }

  private fun getResolvedPath(fileName: String) = BASE_PATH.resolve(fileName)
}