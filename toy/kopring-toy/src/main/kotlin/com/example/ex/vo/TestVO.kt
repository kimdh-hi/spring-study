package com.example.ex.vo

import com.example.ex.common.annotation.NoArg

@NoArg
data class TestRequestVO(
  val data: String
)

@kotlinx.serialization.Serializable
data class TestResponseVO(
  val data: String
)