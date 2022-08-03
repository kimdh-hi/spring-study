package com.toy.selfjoinquery.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_partner")
class Partner (

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id:String? = null,

  var name: String,

  @ManyToOne
  @JoinColumn(name = "parent_partner_id")
  var parentPartner: Partner? = null,

  @OneToMany(mappedBy = "parentPartner", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
  var childPartners: MutableList<Partner> = mutableListOf()

)