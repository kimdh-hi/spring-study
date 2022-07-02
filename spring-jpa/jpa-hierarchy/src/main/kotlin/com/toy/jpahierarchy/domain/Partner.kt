package com.toy.jpahierarchy.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_partner")
class Partner (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_partner_id")
  var parentPartner: Partner? = null,

  @OneToMany(mappedBy = "parentPartner", orphanRemoval = true, cascade = [CascadeType.PERSIST])
  var childPartners: MutableList<Partner> = mutableListOf()

) {
  companion object {
    fun newInstance(name: String, parentPartner: Partner? = null): Partner {
      return parentPartner?.let { parent ->
        val partner = Partner(name = name, parentPartner = parent)
        parent.addChildPartner(partner)
        partner
      } ?: run {
        Partner(name = name)
      }
    }
  }

  private fun addChildPartner(childPartner: Partner) {
    this.childPartners.add(childPartner)
  }
}