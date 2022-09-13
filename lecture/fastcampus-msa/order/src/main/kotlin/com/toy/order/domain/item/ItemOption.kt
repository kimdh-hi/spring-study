package com.toy.order.domain.item

import com.toy.order.common.exception.BaseException
import com.toy.order.common.exception.ErrorCodes
import com.toy.order.domain.AbstractEntity
import org.apache.commons.lang3.StringUtils
import javax.persistence.*

@Entity
@Table(name = "item_options")
class ItemOption(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  val ordering: Int,
  val itemOptionName: String,
  val itemOptionPrice: Long,

  @ManyToOne
  @JoinColumn(name = "item_option_group_id")
  val itemOptionGroup: ItemOptionGroup
): AbstractEntity() {

  companion object {
    fun new(
      itemOptionGroup: ItemOptionGroup,
      ordering: Int,
      itemOptionName: String,
      itemOptionPrice: Long
    ): ItemOption {
      if (StringUtils.isEmpty(itemOptionName)) throw BaseException(ErrorCodes.INVALID_PARAMETER)

      return ItemOption(
        ordering = ordering,
        itemOptionName = itemOptionName,
        itemOptionPrice = itemOptionPrice,
        itemOptionGroup = itemOptionGroup
      )
    }
  }
}