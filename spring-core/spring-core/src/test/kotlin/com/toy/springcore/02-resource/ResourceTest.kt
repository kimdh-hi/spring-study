package com.toy.springcore.`02-resource`

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.UrlResource
import org.springframework.test.context.TestConstructor
import org.springframework.web.context.support.ServletContextResource

/**
 * UrlResource
 * ClassPathResource - classpath:...
 * FileSystemResource - file:...
 * ServletContextResource - 웹 애플리케이션 루트경로부터 상대경로로 리소스를 찾는다?
 */
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ResourceTest(
  private val resourceLoader: ApplicationContext
) {

  @Test
  fun test() {
    println(resourceLoader::class.java) // XXXWebApplicationContext (GenericWebApplicationContext)
    println("==========================================================")
    // resourceLoader 의 타입에 따라 .getResource 시 사용되는 구현체 클래스가 정해진다 (url, file, classPath, servlet...)

    val defaultResource = resourceLoader.getResource("test.txt")
    println(defaultResource::class.java) // prefix 없이 resource 를 찾으면 applicationContext 의 타입을 따라간다. (webApplication -> ServletContextResource -> 웹 애플리케이션 루트패스)
    println(defaultResource.exists())
    println("==========================================================")

    val classPathResource = resourceLoader.getResource("classpath:test.txt")
    println(classPathResource.exists())
    println(classPathResource::class.java)
    println("==========================================================")
    // classpath, file 과 같이 직접 prefix 를 지정하면 resourceLoader 의 구현체를 applicationContext 의 타입과 다르게 할 수 있다.


    // applicationContext 의 타입에 따라 resourceLoader 구현체가 달라질 수 있으므로 왠만하면 prefix(classpath or file...) 를 붙여주자 ..
  }
}