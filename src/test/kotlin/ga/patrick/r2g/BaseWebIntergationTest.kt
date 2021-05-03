package ga.patrick.r2g

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.post
import ga.patrick.r2g.StringLoader.fromClasspath
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(properties = ["spring.cloud.discovery.enabled=false"],
        webEnvironment = WebEnvironment.RANDOM_PORT)
abstract class BaseWebIntergationTest {

    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(context: WebApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
    }

    fun getFilenames(requestName: String,
                     responseName: String = requestName,
                     type: String = "external") =
            "/__files/$type/request/$requestName-request.json" to
                    "/__files/$type/response/$responseName-response.json"

    fun stubGraph(uri: String,
                  requestName: String,
                  responseName: String = requestName) {

        val (requestFile, responseFile) = getFilenames(requestName, responseName)

        val response = fromClasspath(responseFile)

        WireMock.stubFor(post(uri)
                .withRequestBody(GraphRequestMatcher(fromClasspath(requestFile)))
                .willReturn(aResponse()
                        .withBody(response)))
    }

    fun stubPost(uri: String,
                 request: String?,
                 response: String,
                 verifyBody: Boolean = true) {

        WireMock.stubFor(post(uri)
                .apply {
                    if (verifyBody) withRequestBody(equalTo(request))
                }
                .willReturn(aResponse()
                        .withBody(response)))
    }
}