package com.rsupport.xr.api.base

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serial
import java.io.Serializable

@JsonAutoDetect(
  fieldVisibility = JsonAutoDetect.Visibility.ANY,
  getterVisibility = JsonAutoDetect.Visibility.NONE,
  isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
abstract class AbstractVO : Serializable {

  abstract override fun toString(): String

  companion object {
    @Serial
    private const val serialVersionUID: Long = -8311287608450354857L
  }

}

abstract class AbstractRequestVO : AbstractVO() {

  companion object {
    @Serial
    private const val serialVersionUID: Long = 1043868356552407879L
  }

}

abstract class AbstractResponseVO(
  open var message: Any? = null
) : AbstractVO() {

  companion object {
    @Serial
    private const val serialVersionUID: Long = 3962973674652598407L
  }

}

data class ErrorResponseVO(
  var errorCode: String,
  override var message: Any?
) : AbstractResponseVO() {

  companion object {
    @Serial
    private const val serialVersionUID: Long = -6603405402325753950L

    fun error(errorCode: String, message: Any?): ErrorResponseVO {
      return ErrorResponseVO(errorCode, message ?: "Unknown exception..")
    }
  }

}
