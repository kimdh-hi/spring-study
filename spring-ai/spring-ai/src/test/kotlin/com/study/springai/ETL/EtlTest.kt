package com.study.springai.ETL

import org.junit.jupiter.api.Test
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.reader.pdf.PagePdfDocumentReader
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource

/**
 * ETL (Extract-Transform-Load 추출, 변환, 적재)
 *
 * DocumentReader -> DocumentTransformer -> DocumentWriter
 *
 * KeywordMetadataEnricher
 * - LLM 통해 키워드 추출. 추출된 키워드는 필터링에 사용되어 유사도 검색시 대상을 중여 성능을 개선한다
 * - 키워드 추출시 LLM 이 사용되므로 token 소모 많이됨.
 * - "excerpt_keywords
 *
 * TextReader
 * PagePdfDocumentReader
 * TikaDocumentReader
 * JsoupDocumentReader
 * JsonReader
 */

@SpringBootTest
class EtlTest @Autowired constructor(
  private val chatModel: ChatModel,
  private val vectorStore: PgVectorStore,
) {

  @Test
  fun fromFileSystem() {
    //extract
    val resource = ClassPathResource("storage/constitution.pdf")
    val reader = PagePdfDocumentReader(resource)
    val documents = reader.read()

    //transform-split
    val textSplitter = TokenTextSplitter(500, 200, 5, 1000, false)
    val splitDocuments = textSplitter.apply(documents)
    //transform-add keyword metadata
//    val keywordEnricher = KeywordMetadataEnricher(chatModel, 5)
//    val transformedDocuments = keywordEnricher.apply(splitDocuments)



    //load
    vectorStore.add(splitDocuments)
  }
}
