package com.toy.gradletoproperties.properties

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@ConfigurationPropertiesScan
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class CustomPropertiesTest(val customProperties: CustomProperties) {

    /**
     * Gradle 로 build,run 되도록 설정 (intellij XX)
     */
    @Test
    fun getCustomProperties() {
        assertAll({
            assertNotNull(customProperties.key1)
            assertEquals("value1", customProperties.key1)

            assertNotNull(customProperties.key2)
            assertEquals("value2", customProperties.key2)
        })
    }
}