package com.study.springelasticsearch.product.infra

import com.study.springelasticsearch.product.domain.model.Product
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "products")
class ProductDocument(
  @Id
  val id: String,

  @Field(type = FieldType.Text)
  val name: String,

  @Field(type = FieldType.Text)
  val description: String,

  @Field(type = FieldType.Keyword)
  val category: String,

  @Field(type = FieldType.Integer)
  val price: Int,
) {
  companion object {
    fun from(product: Product) = ProductDocument(
      id = product.id.toString(),
      name = product.name,
      description = product.description,
      category = product.category,
      price = product.price,
    )
  }
}
