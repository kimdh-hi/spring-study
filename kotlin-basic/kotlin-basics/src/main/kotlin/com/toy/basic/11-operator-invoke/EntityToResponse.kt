package com.toy.basic.`11-operator-invoke`

data class InvokeTestEntity(
  val id: String,
  val data: String
)

data class InvokeTestEntityResponse(
  val id: String,
  val data: String
) {
  companion object {
    fun of(entity: InvokeTestEntity) =
      with(entity) {
        InvokeTestEntityResponse(id = id, data = data)
      }

    operator fun invoke(entity: InvokeTestEntity) =
      with(entity) {
        InvokeTestEntityResponse(id = id, data = data)
      }
  }
}

fun main() {
  val entity1 = InvokeTestEntity(id = "id1", data = "data1")
  val response1 = InvokeTestEntityResponse.of(entity1)
  println(response1)

  val entity2 = InvokeTestEntity(id = "id2", data = "data2")
  val response2 = InvokeTestEntityResponse(entity2) // operator - invoke
  println(response2)
}