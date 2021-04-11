package ga.patrick.r2g.util

import ga.patrick.r2g.util.VariableUtils.fillTemplate
import ga.patrick.r2g.util.VariableUtils.getPathVariables
import ga.patrick.r2g.util.VariableUtils.getPaths
import ga.patrick.r2g.util.VariableUtils.toMatcher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class VariableUtilsTest {

    @TestFactory
    fun toMatcherTest() = listOf(
            "/abc" to "/abc",
            "/abc/#{abc}" to "/abc/(.+)",
            "/abc/#{abc}/#{asdasd}" to "/abc/(.+)/(.+)"

    ).map { (source, expected) ->
        dynamicTest("$source -> $expected") {
            val actual = source.toMatcher()
            Assertions.assertEquals(expected, actual.pattern)
        }
    }

    @Test
    fun fillTemplateTest() {
        val source = template
        val variables = mapOf("userId" to "123", "currency" to "RUR")
        val expected = """
        {
          users(ids: "123") {
            accounts(where: { currency_eq: "RUR" }) {
            }
          }
        }
        """
        val actual = source.fillTemplate(variables)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun getPathsTest() {
        val source = template
        val expected = setOf("userId", "currency")
        val actual = source.getPaths()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun getPathVariablesTest() {
        val source = "/abc/123/geh/RUR"
        val pattern = "/abc/#{userId}/geh/#{currency}"
        val expected = mapOf("userId" to "123", "currency" to "RUR")
        val actual = source.getPathVariables(pattern)
        assertThat(actual).isEqualTo(expected)
    }


    companion object {
        val template = """
        {
          users(ids: "#{userId}") {
            accounts(where: { currency_eq: "#{currency}" }) {
            }
          }
        }
        """
    }
}