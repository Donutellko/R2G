package ga.patrick.r2g.service

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TemplateFillerServiceTest {

    @TestFactory
    fun testFillTemplate() = listOf(
            TestData("no variables in temlate",
                    "",
                    "")
    ).map { (name, body, uri) ->
        DynamicTest.dynamicTest(name) {
            // TODO
        }
    }

    data class TestData(
            val name: String,
            val body: String,
            val uri: String
    )
}