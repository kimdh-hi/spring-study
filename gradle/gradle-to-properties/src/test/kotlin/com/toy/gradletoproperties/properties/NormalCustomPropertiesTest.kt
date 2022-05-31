package com.toy.gradletoproperties.properties

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@ConfigurationPropertiesScan
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class NormalCustomPropertiesTest(val customProperties: NormalCustomProperties) {

    @Test
    fun getCustomProperties() {
        Assertions.assertAll({
            Assertions.assertNotNull(customProperties.key1)
            Assertions.assertNotNull(customProperties.key2)
        })
    }
}