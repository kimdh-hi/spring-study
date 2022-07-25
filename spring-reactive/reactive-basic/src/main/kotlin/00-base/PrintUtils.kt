package `00-base`

object PrintUtils {

  fun printThreadName(message: String) {
    println("Thread: ${Thread.currentThread().name}, meesage: $message")
  }
}