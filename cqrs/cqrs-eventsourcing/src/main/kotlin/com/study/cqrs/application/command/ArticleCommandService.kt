package com.study.cqrs.application.command

import com.study.cqrs.domain.model.Article
import com.study.cqrs.infra.eventstore.EventStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ArticleCommandService(
  private val eventStore: EventStore,
) {
  fun writeArticle(userId: String, title: String, content: String): String {
    val article = Article.write(title, content, userId)
    eventStore.append(article.pullChanges())
    return article.id
  }

  fun editArticle(articleId: String, title: String, content: String) {
    val article = load(articleId)
    article.edit(title, content)
    eventStore.append(article.pullChanges())
  }

  fun addComment(articleId: String, content: String) {
    val article = load(articleId)
    article.addComment(content)
    eventStore.append(article.pullChanges())
  }

  fun increaseView(articleId: String) {
    val article = load(articleId)
    article.view()
    eventStore.append(article.pullChanges())
  }

  private fun load(articleId: String): Article =
    Article.replay(eventStore.load(articleId))
}
