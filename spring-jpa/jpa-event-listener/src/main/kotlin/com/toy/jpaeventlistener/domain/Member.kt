package com.toy.jpaeventlistener.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "tb_user")
class Member(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,
) {

  @Column(name = "entry_space_id")
  var entrySpaceId: String? = null

  @Enumerated(EnumType.STRING)
  @Column(name = "entry_space_type")
  var entrySpaceType: MemberSpaceType? = null

  @Column(name = "portal_space_id")
  var portalSpaceId: String? = null

  @Enumerated(EnumType.STRING)
  @Column(name = "portal_space_type")
  var portalSpaceType: MemberSpaceType? = null

  fun clearEntrySpace() {
    entrySpaceId = null
    entrySpaceType = null
  }

  fun clearPortalSpace() {
    portalSpaceId = null
    portalSpaceType = null
  }

  override fun toString(): String {
    return "Member(id=$id, name='$name', entrySpaceId=$entrySpaceId, entrySpaceType=$entrySpaceType)"
  }
}