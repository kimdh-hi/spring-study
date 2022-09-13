package com.toy.order.domain.item

import com.toy.order.common.exception.BaseException
import com.toy.order.common.exception.ErrorCodes
import com.toy.order.common.utils.TokenGenerator
import com.toy.order.domain.AbstractEntity
import org.apache.commons.lang3.StringUtils
import javax.persistence.*

@Entity
@Table(name = "items")
data class Item(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  val itemToken: String,
  val partnerId: Long,
  val name: String,
  val price: Long,

  @Enumerated(EnumType.STRING)
  var status: Status,

  @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], mappedBy = "item")
  val itemOptionGroups: MutableList<ItemOptionGroup> = mutableListOf()

): AbstractEntity() {

  companion object {
    private const val PREFIX_PARTNER = "itm_"

    fun new(partnerId: Long, name: String, price: Long): Item {
      if (StringUtils.isEmpty(name)) throw BaseException(ErrorCodes.INVALID_PARAMETER)
      if (price < 0) throw BaseException(ErrorCodes.INVALID_PARAMETER)

      return Item(
        itemToken = TokenGenerator.randomCharacterWithPrefix(PREFIX_PARTNER),
        partnerId = partnerId,
        name = name,
        price = price,
        status = Status.ON_SALES
      )
    }
  }

  fun changePrepare() {
    this.status = Status.PREPARE
  }

  fun changeOnSales() {
    this.status = Status.ON_SALES
  }

  fun changeEndOfSales() {
    this.status = Status.END_OF_SALES
  }

  enum class Status(private val description: String) {
    PREPARE("판매준비중"), ON_SALES("판매중"), END_OF_SALES("판매완료")
  }
}