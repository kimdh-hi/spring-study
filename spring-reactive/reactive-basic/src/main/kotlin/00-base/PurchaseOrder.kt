package `00-base`

data class PurchaseOrder(
  val item: String,
  val price: Double,
  val userId: Int
) {
  companion object {
    fun newOrder(userId: Int) = PurchaseOrder(
      userId = userId,
      item = FakerUtils.FAKER.commerce().productName(),
      price = FakerUtils.FAKER.commerce().price().toDouble()
    )
  }
}