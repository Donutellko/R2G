package ga.patrick.r2g.controller

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import ga.patrick.r2g.BaseWebIntergationTest
import ga.patrick.r2g.service.RequestProcessService
import ga.patrick.r2g.toJson
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.get

@SpringBootTest
class ApiControllerIntegrationTest : BaseWebIntergationTest() {

    @SpyBean
    lateinit var service: RequestProcessService

    @Test
    fun testGet() {
        val uri = ""

        whenever(service.processGet(any()))
                .thenReturn(ApiControllerTest.someOkResponse)

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

        verify(service).processGet(uri)
    }

    companion object {
        val someObject: Any = mapOf("key" to "value")
        val someOkResponse: ResponseEntity<Any> = ResponseEntity.ok(someObject)
    }

}