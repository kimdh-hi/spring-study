package `01-mono`.`08-file-service`

import `00-base`.ConsumerUtils

fun main() {
  val fileService = FileService()

  fileService.fileSave("test3.txt", "test3...")
    .subscribe(ConsumerUtils.onNext(), ConsumerUtils.onError(), ConsumerUtils.onComplete())

  fileService.fileRead("test3.txt")
    .subscribe(ConsumerUtils.onNext(), ConsumerUtils.onError(), ConsumerUtils.onComplete())

  fileService.fileDelete("test3.txt")
    .subscribe(ConsumerUtils.onNext(), ConsumerUtils.onError(), ConsumerUtils.onComplete())
}