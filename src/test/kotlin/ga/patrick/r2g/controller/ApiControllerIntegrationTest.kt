package ga.patrick.r2g.controller

import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import ga.patrick.r2g.BaseWebIntergationTest
import ga.patrick.r2g.service.RequestProcessService
import ga.patrick.r2g.toJson
import org.junit.jupiter.api.Test
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class ApiControllerIntegrationTest : BaseWebIntergationTest() {

    @SpyBean
    lateinit var service: RequestProcessService

    @Test
    fun testGet() {
        val userId = "1"
        val currency = "RUR"
        val uri = "/users/${userId}/cards?currency=${currency}"

        stubGraph(fakeQlAccountsEndpoint, "accounts-rur-by-user")

        mockMvc.get(uri)
                .andDo {
                    print()
                    log()
                }
                .andExpect {
                    status { isOk }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(ApiControllerTest.someObject.toJson())
                    }
                }
    }

    @Test
    fun testDefaultEndpoint() {
        val uri = "/abc/def?ghi=jkl&mno=pqr"
        val body = """{ "abc" : "def" }"""

        stubPost(uri, body, body, verifyBody = false)

        mockMvc.post(uri)
                .andDo {
                    print()
                    log()
                }
                .andExpect {
                    status { isOk }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
//                        body.contentEquals("as")
                    }
                }

        wiremock.verify(postRequestedFor(urlEqualTo(uri)).withRequestBody(equalTo(body)))
    }

    companion object {
        val someObject: Any = mapOf("key" to "value")
        val someOkResponse: ResponseEntity<Any> = ResponseEntity.ok(someObject)

        val defaultEndpoint = "https://postman-echo.com/path/"
        val fakeQlAccountsEndpoint = "https://fakeql.com/graphql/cbc8a521a34c299f030c7f6df523591f"
    }

}