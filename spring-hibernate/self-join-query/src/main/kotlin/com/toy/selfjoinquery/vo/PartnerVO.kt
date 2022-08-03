package com.toy.selfjoinquery.vo

import com.querydsl.core.annotations.QueryProjection

data class PartnerResponseVO @QueryProjection constructor(
  var id: String,
  var name: String,
  var parentPartnerId: String
)