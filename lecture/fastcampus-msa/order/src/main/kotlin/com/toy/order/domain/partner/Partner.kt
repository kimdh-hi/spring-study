package com.toy.order.domain.partner

import com.toy.order.common.utils.TokenGenerator
import com.toy.order.domain.AbstractEntity
import org.apache.commons.lang3.StringUtils
import javax.management.monitor.StringMonitor
import javax.persistence.*

@Entity
@Table(name = "partners")
class Partner(

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
  val partnerToken: String,
  val partnerName: String,
  val businessNo: String,
  val email: String,

  @Enumerated(EnumType.STRING)
  var status: Status,
): AbstractEntity() {

  companion object {

    const val PREFIX_PARTNER = "ptn_"

    fun newPartner(partnerName: String, businessNo: String, email: String): Partner {
      if(StringUtils.isEmpty(partnerName)) throw RuntimeException("empty partnerName")
      if(StringUtils.isEmpty(businessNo)) throw RuntimeException("empty partnerName")
      if(StringUtils.isEmpty(email)) throw RuntimeException("empty partnerName")

      return Partner(
        partnerToken = TokenGenerator.randomCharacterWithPrefix(PREFIX_PARTNER),
        partnerName = partnerName,
        businessNo = businessNo,
        email = email,
        status = Status.ENABLE
      )
    }
  }

  enum class Status(private val description: String) {
    ENABLE("활성화"), DISABLE("비활성화");
  }

  fun disable() {
    status = Status.DISABLE
  }

  fun enable() {
    status = Status.ENABLE
  }
}