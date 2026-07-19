package com.study.cqrs.presentation.command

import com.study.cqrs.application.command.ArticleCommandService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleCommandController(
  private val service: ArticleCommandService,
) {
  @PostMapping("/articles")
  fun writeArticle(
    @RequestHeader("X-User-Id") userId: String,
    @RequestBody req: WriteArticleRequest,
  ): String =
    service.writeArticle(userId, req.title, req.content)

  @PutMapping("/articles/{id}")
  fun editArticle(@PathVariable id: String, @RequestBody req: EditArticleRequest) =
    service.editArticle(id, req.title, req.content)

  @PostMapping("/articles/{id}/comments")
  fun addComment(@PathVariable id: String, @RequestBody req: AddCommentRequest) =
    service.addComment(id, req.content)

  @PostMapping("/articles/{id}/views")
  fun increaseView(@PathVariable id: String) =
    service.increaseView(id)
}

data class WriteArticleRequest(val title: String, val content: String)
data class EditArticleRequest(val title: String, val content: String)
data class AddCommentRequest(val content: String)
