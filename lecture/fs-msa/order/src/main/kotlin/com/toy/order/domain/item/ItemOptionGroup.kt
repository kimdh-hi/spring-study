package com.toy.order.domain.item

import com.toy.order.common.exception.BaseException
import com.toy.order.common.exception.ErrorCodes
import com.toy.order.domain.AbstractEntity
import org.apache.commons.lang3.StringUtils
import javax.persistence.*

@Entity
@Table(name = "item_option_groups")
class ItemOptionGroup(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  @ManyToOne
  @JoinColumn(name = "item_id")
  val item: Item,

  val ordering: Int,
  val itemOptionGroupName: String,

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemOption", cascade = [CascadeType.PERSIST])
  val itemOptions: MutableList<ItemOption> = mutableListOf()
): AbstractEntity() {

  companion object {
    fun new(item: Item, ordering: Int, itemOptionGroupName: String): ItemOptionGroup {
      if (StringUtils.isEmpty(itemOptionGroupName)) throw BaseException(ErrorCodes.INVALID_PARAMETER)

      return ItemOptionGroup(
        item = item,
        ordering = ordering,
        itemOptionGroupName = itemOptionGroupName
      )
    }
  }
}