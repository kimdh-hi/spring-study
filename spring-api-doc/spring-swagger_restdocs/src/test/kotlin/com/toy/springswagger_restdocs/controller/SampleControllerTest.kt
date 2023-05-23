package com.toy.springswagger_restdocs.controller

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.toy.springswagger_restdocs.base.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

class SampleControllerTest : AbstractIntegrationTest() {

  //java.lang.NoSuchMethodError: 'org.springframework.http.HttpStatus org.springframework.restdocs.operation.OperationResponse.getStatus()'
  @Test
  fun getSampleByIdTest() {
    //given
    val sampleId = "sampleId"

    //when
    val result = mockMvc.perform(
      get("/api/samples/{sampleId}", sampleId)
    )
      .andDo(
        MockMvcRestDocumentationWrapper.document("sample",
          preprocessRequest(prettyPrint()),
          preprocessResponse(prettyPrint()),
          resource(
            ResourceSnippetParametersBuilder()
              .tag("Sample")
              .description("Get a sample by id")
              .pathParameters(
                parameterWithName("sampleId").description("the sample id"),
              )
              .responseFields(
                fieldWithPath("sampleId").type(JsonFieldType.STRING).description("The sample identifier."),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The name of sample."),
              )
              .build(),
          )
        ),
      )
  }
}