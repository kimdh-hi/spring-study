/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our samples at https://docs.gradle.org/7.4.2/samples
 */

/**
 * Task 실행
 * gradle firstTask
 * gradle fT
 */
println("start")
task("firstTask") {
    println("firstTask run")
    doFirst {
        println("doFirst")
    }
    doLast {
        println("doLast")
    }
}
println("end")

task("deployToProd") {
    doLast {
        println("complete deploy to prod.")
    }
}.dependsOn("deployToStage") // deployToProd 테스크가 실행될 때 deployToStage 가 선행된다.
    .finalizedBy("cleanupFiles") // deployToProd 테스크의 실행이 완료되고 cleanupFiles 가 실행된다.

task("deployToStage") {
    doLast {
        println("complete deploy to stage.")
    }
}

task("cleanupFiles") {
    doLast {
        println("complete cleanup files.")
    }
}

defaultTasks("deployToProd") // task 이름없이 gradle 로 실행시 기본으로 실행되는 task 지정