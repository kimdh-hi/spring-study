package com.example.swaggerrestdocs.ui

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails
import com.epages.restdocs.apispec.ResourceSnippetDetails
import com.epages.restdocs.apispec.Schema
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.restdocs.test.autoconfigure.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.restdocs.snippet.Snippet
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserControllerTest(
    @Autowired private val mockMvc: MockMvc,
) {
    private val userRequestFields = requestFields(
        fieldWithPath("name").description("User name"),
        fieldWithPath("email").description("User email"),
    )
    private val userResponseFields = responseFields(
        fieldWithPath("id").description("User id"),
        fieldWithPath("name").description("User name"),
        fieldWithPath("email").description("User email"),
    )
    private val usersResponseFields = responseFields(
        fieldWithPath("[].id").description("User id"),
        fieldWithPath("[].name").description("User name"),
        fieldWithPath("[].email").description("User email"),
    )
    private val userIdPath = pathParameters(parameterWithName("id").description("User id"))

    private fun users(summary: String) = resourceDetails().tag("Users").summary(summary)

    // Generates both REST Docs snippets and the OpenAPI resource snippet from the same descriptors
    private fun document(identifier: String, details: ResourceSnippetDetails, vararg snippets: Snippet) =
        MockMvcRestDocumentationWrapper.document(identifier, details, snippets = snippets)

    @Test
    fun crud() {
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Kim","email":"kim@example.com"}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andDo(
                document(
                    "users-create",
                    users("Create user").requestSchema(Schema("UserRequest")).responseSchema(Schema("UserResponse")),
                    userRequestFields,
                    userResponseFields,
                ),
            )

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].email").value("kim@example.com"))
            .andDo(document("users-find-all", users("Find all users"), usersResponseFields))

        mockMvc.perform(get("/api/users/{id}", 1))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Kim"))
            .andDo(
                document(
                    "users-find-by-id",
                    users("Find user").responseSchema(Schema("UserResponse")),
                    userIdPath,
                    userResponseFields,
                ),
            )

        mockMvc.perform(
            put("/api/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Lee","email":"lee@example.com"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("lee@example.com"))
            .andDo(
                document(
                    "users-update",
                    users("Update user").requestSchema(Schema("UserRequest")).responseSchema(Schema("UserResponse")),
                    userIdPath,
                    userRequestFields,
                    userResponseFields,
                ),
            )

        mockMvc.perform(delete("/api/users/{id}", 1))
            .andExpect(status().isNoContent)
            .andDo(document("users-delete", users("Delete user"), userIdPath))

        mockMvc.perform(get("/api/users/{id}", 1))
            .andExpect(status().isNotFound)
    }
}
