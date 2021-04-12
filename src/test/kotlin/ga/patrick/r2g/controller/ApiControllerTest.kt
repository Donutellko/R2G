package ga.patrick.r2g.controller

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import ga.patrick.r2g.service.RequestProcessService
import ga.patrick.r2g.toJson
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest


@SpringBootTest(classes = [ApiController::class])
class ApiControllerTest {

    @Autowired
    lateinit var controller: ApiController

    @MockBean
    lateinit var service: RequestProcessService

    @Test
    fun testEndpoint() {
        val uri = ""
        val mockRequest = MockHttpServletRequest(HttpMethod.GET.name, uri)

        whenever(service.processRequest(any()))
                .thenReturn(someOkResponse)

        val result = controller.endpoint(mockRequest)

        Assertions.assertEquals(someOkResponse, result)
        verify(service).processRequest(mockRequest)
    }

    companion object {
        val someObject: Any = mapOf("key" to "value")
        val someOkResponse: ResponseEntity<String> = ResponseEntity.ok(someObject.toJson())
    }
}