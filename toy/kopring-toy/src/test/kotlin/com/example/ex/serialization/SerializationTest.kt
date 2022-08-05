package com.example.ex.serialization

import com.example.ex.vo.TestResponseVO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.ByteArrayOutputStream
import java.io.NotSerializableException
import java.io.ObjectOutputStream

class SerializationTest {

  @Test
  fun test1() {
    val vo = TestResponseVO(data = "data1...")
    assertDoesNotThrow {
        ByteArrayOutputStream().use { baos ->
        ObjectOutputStream(baos).use { oos ->
          oos.writeObject(vo)
        }
      }
    }
  }
}