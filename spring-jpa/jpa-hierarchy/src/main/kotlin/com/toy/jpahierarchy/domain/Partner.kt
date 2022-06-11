package com.toy.jpahierarchy.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "tb_partner")
class Partner (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  @ManyToOne
  @JoinColumn(name = "parent_partner_id")
  var parentPartner: Partner? = null,

  @JsonIgnore
  @OneToMany(mappedBy = "parentPartner", cascade = [CascadeType.PERSIST])
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