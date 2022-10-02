package `00-base`

data class User(
  val userId: Long,
  val username: String
) {
  companion object {
    fun newUser(userId: Long) = User(
      userId = userId,
      username = FakerUtils.getFullName()
    )
  }
}