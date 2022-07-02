package com.example.ex.test.properties

import org.apache.commons.configuration.PropertiesConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PropertiesResourceConfigTest {

    @Test
    fun test() {
        val basePath = "src/test/resources/"
        val testPropertyName = "test-property.properties"
        val propertiesConfiguration = PropertiesConfiguration()
        propertiesConfiguration.setProperty("my.name","kim")
        propertiesConfiguration.save("$basePath$testPropertyName")
    }
}