package ga.patrick.r2g.service

import com.nhaarman.mockitokotlin2.whenever
import ga.patrick.r2g.property.Mapping
import ga.patrick.r2g.property.MappingProperties
import ga.patrick.r2g.service.MappingMatcherServiceTest.TestData.Companion.EQUALS_VALIDATOR
import ga.patrick.r2g.service.MappingMatcherServiceTest.TestData.Companion.NULL_VALIDATOR
import ga.patrick.r2g.service.impl.MappingMatcherServiceImpl
import ga.patrick.r2g.util.VariableUtils.toMatcher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockHttpServletRequest
import java.net.URI
import java.util.function.Consumer

@SpringBootTest(classes = [MappingMatcherServiceImpl::class])
class MappingMatcherServiceTest {

    @Autowired
    lateinit var mappingMatcherService: MappingMatcherService

    @MockBean
    lateinit var mappingProperties: MappingProperties

    @TestFactory
    fun testPositive() = listOf(
            TestData(
                    "find and return single mapping",
                    mappings = listOf(exampleMapping),
                    request = exampleRequest,
                    validator = EQUALS_VALIDATOR(exampleMapping)
            ),
            TestData(
                    "find and return most specific mapping, if two match",
                    mappings = listOf(
                            exampleMapping.copy(path = "/path"),
                            exampleMapping.copy(path = "/path/#{id}")
                    ),
                    request = exampleRequest.apply{ requestURI = "/path/" },
                    validator = EQUALS_VALIDATOR(exampleMapping.copy(path = "/path"))
            ),
            TestData(
                    "do not find mapping if method is wrong",
                    mappings = listOf(exampleMapping),
                    request = exampleRequest.apply {
                        method = HttpMethod.POST.name
                    },
                    validator = NULL_VALIDATOR
            ),
            TestData(
                    "do not find mapping if there is no mappings",
                    mappings = listOf(),
                    request = exampleRequest,
                    validator = NULL_VALIDATOR
            )

    ).map { (name, mappings, request, validator) ->
        DynamicTest.dynamicTest(name) {
            whenever(mappingProperties.mappings)
                    .thenReturn(mappings)

            val actual = mappingMatcherService.findMapping(request)

            validator.accept(actual)
        }
    }

    /**
     * Feature-request: out of several mappings, select the most accurate
     */
//    @Test
//    fun `throw exception if two mappings match`() {
//        val mappings = listOf(
//                exampleMapping.copy(path = "/path/#{var1}/"),
//                exampleMapping.copy(path = "/path/#{var1}/#{var2}/")
//        )
//        val request = exampleRequest.apply {
//            requestURI = "/path/abc/def/"
//        }
//
//        whenever(mappingProperties.mappings)
//                .thenReturn(mappings)
//
//        val actual = assertThrows<Exception> {
//            mappingMatcherService.findMapping(request)
//        }
//
//        val expectedString = "Matched 2 mappings for GET /path/abc/def/?user=value1&param2=value2, expected 1 or 0. " +
//                "Matched paths: [[GET, /path/#{var1}/], [GET, /path/#{var1}/#{var2}/]]"
//
//        Assertions.assertEquals(expectedString, actual.message)
//    }


    data class TestData(
            val name: String,
            val mappings: List<Mapping>,
            val request: MockHttpServletRequest,
            val validator: Consumer<Mapping?>
    ) {
        companion object {
            val NOT_NULL_VALIDATOR = { actual: Any? -> Assertions.assertNotNull(actual) }
            val NULL_VALIDATOR = { actual: Any? -> Assertions.assertNull(actual) }
            fun EQUALS_VALIDATOR(expected: Mapping?) = { actual: Mapping? -> Assertions.assertEquals(expected, actual) }
        }
    }

    companion object {
        val exampleUri = URI("/some/path")
        val exampleMethod = HttpMethod.GET
        val exampleQuery = "user=value1&param2=value2"

        val exampleMapping = Mapping(
                path = exampleUri.path,
                methods = listOf(exampleMethod),
                endpointName = "some-endpoint",
                template = """
                { 
                    user(id: #{user}) {
                        name
                    } 
                }""".trimIndent()
        )

        val exampleRequest: MockHttpServletRequest
            get() = MockHttpServletRequest().apply {
                requestURI = exampleUri.path
                method = exampleMethod.name
                queryString = exampleQuery
            }
    }
}
