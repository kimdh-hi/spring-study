package com.toy.jpacustomgenerator.common

class SliceResponseVO<T>(
  val lastId: String,
  val hasNext: Boolean,
  val content: List<T>
)