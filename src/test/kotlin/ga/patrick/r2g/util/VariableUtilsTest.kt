package ga.patrick.r2g.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import ga.patrick.r2g.util.VariableUtils.flattenTree
import ga.patrick.r2g.util.VariableUtils.toMatcher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class VariableUtilsTest {

    val objectMapper = ObjectMapper()

    @TestFactory
    fun testToMatcher() = listOf(
            "/abc" to "/abc",
            "/abc/\${abc}" to "/abc/.+",
            "/abc/\${abc}/\${asdasd}" to "/abc/.+/.+"

    ).map { (source, expected) ->
        dynamicTest("$source -> $expected") {
            val actual = source.toMatcher()
            Assertions.assertEquals(expected, actual.pattern)
        }
    }

    @Test
    fun a() {
        val jsonContext: DocumentContext = JsonPath.parse("""{"a": {"c": ["d"] } }""")
        println(jsonContext)
    }

    @TestFactory
    fun testFlattenNode() = listOf(
            """["a", "b"]""" to setOf("[0]" to "a", "[1]" to "b"),
            """{ "a": "b" }""" to setOf("a" to "b"),
            """{ "a": "b", "c": "d" } """ to setOf("a" to "b", "c" to "d"),
            """{ "a": { "c": "d" } }""" to setOf("a.c" to "d"),
            """{ "a": { "c": [ "d" ] } }""" to setOf("a.c[0]" to "d"),
    ).map { (source, expected) ->
        dynamicTest(source) {
            val tree = objectMapper.readTree(source)
            val actual = tree.flattenTree()

            assertThat(actual).isEqualTo(expected)
        }
    }
}