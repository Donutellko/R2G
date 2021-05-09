package ga.patrick.r2g.controller

import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import ga.patrick.r2g.BaseWebIntergationTest
import ga.patrick.r2g.GraphRequestMatcher
import ga.patrick.r2g.StringLoader.fromClasspath
import ga.patrick.r2g.service.RequestProcessService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@AutoConfigureWireMock(port = 0)
class ApiControllerIntegrationTest : BaseWebIntergationTest() {

    @SpyBean
    lateinit var service: RequestProcessService

    @Test
    fun `simple get`() {
        val userId = "1"
        val currency = "RUR"
        val uri = "/users/${userId}/cards?currency=${currency}"
        val fileName = "accounts-rur-by-user"

        val (requestFile, responseFile) = getFilenames(fileName)

        stubGraph(graphqlEndpoint, fileName)

        mockMvc.get(uri)
                .andDo {
                    print()
                    log()
                }
                .andExpect {
                    status { isOk }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(fromClasspath(responseFile))
                    }
                }

        verify(postRequestedFor(urlEqualTo(graphqlEndpoint))
                .withRequestBody(GraphRequestMatcher(fromClasspath(requestFile))))
    }

    @Test
    fun `post with variables`() {
        val userId = "1"
        val uri = "/users/${userId}"
        val fileName = "accounts-update-user-age"

        val (requestFile, _) = getFilenames(fileName)
        val (selfRequestFile, selfResponseFile) = getFilenames(fileName, type = "self")

        stubGraph(graphqlEndpoint, fileName)

        mockMvc
                .post(uri) {
                    this.content = fromClasspath(selfRequestFile)
                }
                .andDo {
                    print()
                    log()
                }
                .andExpect {
                    status { isOk }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(fromClasspath(selfResponseFile))
                    }
                }

        verify(postRequestedFor(urlEqualTo(graphqlEndpoint))
                .withRequestBody(GraphRequestMatcher(fromClasspath(requestFile))))
    }

    @Test
    fun testDefaultEndpoint() {
        val uri = "/abc/def?ghi=jkl&mno=pqr"
        val body = """{ "abc" : "def" }"""
        val response = """{ "def" : "ghi" }"""

        stubPost(defaultEndpoint + uri, body, response, verifyBody = false)

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

        verify(postRequestedFor(urlEqualTo(defaultEndpoint + uri))
                .withRequestBody(equalTo(body)))
    }

    companion object {
        const val defaultEndpoint = "/default"
        const val graphqlEndpoint = "/graphql"
    }

}