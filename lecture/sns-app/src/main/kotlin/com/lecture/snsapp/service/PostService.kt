package com.lecture.snsapp.service

import com.lecture.snsapp.domain.*
import com.lecture.snsapp.domain.enums.AlarmType
import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.exception.ErrorCode
import com.lecture.snsapp.repository.*
import com.lecture.snsapp.vo.CommentRequestVO
import com.lecture.snsapp.vo.CommentResponseVO
import com.lecture.snsapp.vo.PostCreateResponseVO
import com.lecture.snsapp.vo.PostResponseVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
  private val postRepository: PostRepository,
  private val userRepository: UserRepository,
  private val likeRepository: LikeRepository,
  private val commentRepository: CommentRepository,
  private val alarmRepository: AlarmRepository
) {

  fun list(pageable: Pageable): Page<PostResponseVO> {
    return postRepository.findAll(pageable).map {
      PostResponseVO.of(it)
    }
  }

  fun myList(username: String, pageable: Pageable): Page<PostResponseVO> {
    val user = userRepository.findByUsername(username)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)
    return postRepository.findAllByUser(user, pageable).map {
      PostResponseVO.of(it)
    }
  }

  @Transactional
  fun create(title: String, body: String, username: String): PostCreateResponseVO {
    val user = userRepository.findByUsername(username)
      ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND)
    val post = Post.of(title, body, user)
    val savedPost = postRepository.save(post)
    return PostCreateResponseVO.of(savedPost)
  }

  @Transactional
  fun modify(postId: String, title: String, body: String, username: String) {
    val user = userRepository.findByUsername(username)
      ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND)
    val post = postRepository.findByIdOrNull(postId)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)
    if(post.user.username != user.username)
      throw ApplicationException(ErrorCode.INVALID_PERMISSION)

    post.title = title
    post.body = body
  }

  @Transactional
  fun delete(postId: String, username: String) {
    val user = userRepository.findByUsername(username)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)
    val post = postRepository.findByIdOrNull(postId)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)
    if(post.user.username != user.username)
      throw ApplicationException(ErrorCode.INVALID_PERMISSION)

    postRepository.delete(post)
  }

  @Transactional
  fun like(postId: String, username: String) {
    val user = userRepository.findByUsername(username)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)
    val post = postRepository.findByIdOrNull(postId)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)

    likeRepository.findByUserAndPost(user, post) ?:
      throw ApplicationException(ErrorCode.ALREADY_LIKED_POST)

    val like = Like.of(user, post)
    likeRepository.save(like)
    val alarm = Alarm.of(post.user, post, AlarmType.NEW_LIKE, AlarmArgs(user.id, post.id))
    alarmRepository.save(alarm)
  }

  fun likeCount(postId: String): Long {
    val post = postRepository.findByIdOrNull(postId)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)
    return likeRepository.countByPost(post)
//    return likeRepository.findAllByPost(post).size
  }

  @Transactional
  fun addComment(postId: String, username:String, requestVO: CommentRequestVO) {
    val user = userRepository.findByUsername(username)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)
    val post = postRepository.findByIdOrNull(postId)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)

    val comment = Comment.of(requestVO.comment, user, post)
    commentRepository.save(comment)
    val alarm = Alarm.of(post.user, post, AlarmType.NEW_COMMENT, AlarmArgs(user.id, post.id))
    alarmRepository.save(alarm)
  }

  fun getComments(id: String, pageable: Pageable): Page<CommentResponseVO> {
    val post = postRepository.findByIdOrNull(id)
      ?: throw ApplicationException(ErrorCode.POST_NOT_FOUND)
    return commentRepository.findAllByPost(post, pageable)
      .map { CommentResponseVO.of(it) }
  }
}