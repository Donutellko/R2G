package ga.patrick.r2g

import org.junit.jupiter.api.BeforeEach
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

abstract class BaseWebIntergationTest {

    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(context: WebApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
    }
}