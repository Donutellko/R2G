package ga.patrick.r2g.controller

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import ga.patrick.r2g.BaseWebIntergationTest
import ga.patrick.r2g.service.RequestProcessService
import ga.patrick.r2g.toJson
import org.junit.jupiter.api.Test
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@AutoConfigureWireMock(port = 0)
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
        val response = """{ "def" : "ghi" }"""

        stubPost(uri, body, response, verifyBody = false)

        mockMvc
                .post(uri) {
                    content = body
                    headers { set("headers-name", "header-value") }
                }
                .andDo {
                    print()
                    log()
                }.andExpect {
                    status { isOk }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        string(response)
                    }
                }

        WireMock.verify(postRequestedFor(urlEqualTo(uri)).withRequestBody(equalTo(body)))
    }

    companion object {
        val someObject: Any = mapOf("key" to "value")
        val someOkResponse: ResponseEntity<Any> = ResponseEntity.ok(someObject)

        val defaultEndpoint = "https://postman-echo.com/path/"
        val fakeQlAccountsEndpoint = "https://fakeql.com/graphql/cbc8a521a34c299f030c7f6df523591f"
    }

}