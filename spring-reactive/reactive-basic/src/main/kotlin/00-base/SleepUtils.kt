package `00-base`

object SleepUtils {

  fun sleepForSeconds(seconds: Long) = Thread.sleep(seconds * 1000)
}