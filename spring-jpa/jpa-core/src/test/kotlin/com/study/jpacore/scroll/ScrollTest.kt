package com.study.jpacore.scroll

import com.study.jpacore.entity.Post
import com.study.jpacore.repository.PostRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.support.WindowIterator
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ScrollTest @Autowired constructor(
  private val postRepository: PostRepository,
) {

  @BeforeEach
  fun setUp() {
    postRepository.deleteAllInBatch()
    postRepository.saveAll((1..7).map { Post.of("post-$it") })
  }

  @Test
  fun offsetScroll() {
    // limit 3 offset 0
    val first = postRepository.findTop3ByOrderByIdAsc(ScrollPosition.offset())
    assertThat(first.map { it.title }).containsExactly("post-1", "post-2", "post-3")
    assertThat(first.hasNext()).isTrue()

    // limit 3 offset 3
    val second = postRepository.findTop3ByOrderByIdAsc(first.positionAt(first.size() - 1))
    assertThat(second.map { it.title }).containsExactly("post-4", "post-5", "post-6")
  }

  @Test
  fun keysetScroll() {
    // where id > {마지막 id} order by id limit 3
    val first = postRepository.findFirst3ByOrderByIdAsc(ScrollPosition.keyset())
    val second = postRepository.findFirst3ByOrderByIdAsc(first.positionAt(first.size() - 1))
    val last = postRepository.findFirst3ByOrderByIdAsc(second.positionAt(second.size() - 1))

    assertThat(last.map { it.title }).containsExactly("post-7")
    assertThat(last.hasNext()).isFalse()
  }

  @Test
  fun limitParameter() {
    // 같은 메서드에 호출마다 다른 limit 전달
    val first = postRepository.findByOrderByIdAsc(ScrollPosition.keyset(), Limit.of(2))
    val second = postRepository.findByOrderByIdAsc(first.positionAt(first.size() - 1), Limit.of(4))

    assertThat(first.map { it.title }).containsExactly("post-1", "post-2")
    assertThat(second.map { it.title }).containsExactly("post-3", "post-4", "post-5", "post-6")
  }

  @Test
  fun windowIterator() {
    // 다음 position 계산을 WindowIterator 에 위임
    val titles = WindowIterator.of { postRepository.findFirst3ByOrderByIdAsc(it) }
      .startingAt(ScrollPosition.keyset())
      .asSequence()
      .map { it.title }
      .toList()

    assertThat(titles).hasSize(7)
  }
}
