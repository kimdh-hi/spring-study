package com.study.gof.`05-prototype`.`02-after`

class GithubRepository(
  var user: String,
  var name: String
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GithubRepository

    if (user != other.user) return false
    if (name != other.name) return false

    return true
  }

  override fun hashCode(): Int {
    var result = user.hashCode()
    result = 31 * result + name.hashCode()
    return result
  }
}

class GithubIssue (
  var id: String,
  var title: String,
  var githubRepository: GithubRepository
): Cloneable {

  fun getUrl() = "https://github.com/${githubRepository.user}/${githubRepository.name}/$id"

  /**
   * default => 얕은 복사
   */
  public override fun clone(): GithubIssue {
    val cloneGithubRepository = GithubRepository(
      user = this.githubRepository.user,
      name = this.githubRepository.name
    )

    return GithubIssue(id = this.id, title = this.title, githubRepository = cloneGithubRepository)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GithubIssue

    if (id != other.id) return false
    if (title != other.title) return false
    if (githubRepository != other.githubRepository) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + title.hashCode()
    result = 31 * result + githubRepository.hashCode()
    return result
  }


}