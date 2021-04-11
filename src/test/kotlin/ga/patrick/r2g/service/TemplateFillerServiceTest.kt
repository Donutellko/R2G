package ga.patrick.r2g.service

import ga.patrick.r2g.util.VariableUtils.variableMatcher
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class TemplateFillerServiceTest {

    @TestFactory
    fun testFillTemplate() = listOf(
            TestData("no variables in temlate")
    )

    data class TestData(
            val name: String,
//            val body: String,
//            val uri: String
    ) {

    }

}