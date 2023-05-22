package com.toy.springswagger_restdocs.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.toy.springswagger_restdocs.base.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint

class TestControllerTest: AbstractIntegrationTest() {

  @Test
  fun `save test`() {
    //given
    val dto = TestDto("data")

    //when
    val result = mockMvc.perform(post("/api/test")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto))
    )

    //then
    result.andDo {
      document("save",
        preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()),
        resource(
          ResourceSnippetParameters.builder()
            .description("save...")
            .requestFields()
            .build()
        )
      )
    }
  }
}