package ga.patrick.r2g.util

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.apache.commons.lang3.StringUtils

object VariableUtils {
    private const val variablePrefix = "#{"
    private const val variableSuffix = "}"
    private const val INTERMEDIATE_REPLACEMENT = "=%INTERMEDIATE%="

    private val charactersToEscape = "/?.*{+#^".toCharArray()

    val variableMatcher = Regex("${variablePrefix.escape()}(.+?)$variableSuffix")

    fun String.toMatcher() = Regex(this
            .replace(variableMatcher, INTERMEDIATE_REPLACEMENT)
            .escape()
            .replace(INTERMEDIATE_REPLACEMENT, "(.+)"))

    fun String.fillTemplate(variables: Map<String, String?>): String {
        val searchList = variables.keys.map { "$variablePrefix$it$variableSuffix" }.toTypedArray()
        val replacementList = variables.values.toTypedArray()
        return StringUtils.replaceEach(this, searchList, replacementList)
    }

    fun String.getPaths(): Set<String> = variableMatcher.findAll(this)
            .map { it.groupValues.last() }
            .toSet()

    fun String.getPathVariables(pattern: String): Map<String, String> {
        val namesMatched = variableMatcher.findAll(pattern).toList()
        if (namesMatched.isEmpty()) {
            return emptyMap()
        }

        val valuesMatched = pattern.toMatcher().findAll(this).toList()

        val names = namesMatched.map { it.groupValues.last() }
        val values = valuesMatched.single().groupValues
                .let { it.subList(1, it.size) }

        return names.zip(values).toMap()
    }

    fun Map<String, Array<String?>>.getQueryVariables(): Map<String, String?> =
            mapValues { (_, value) -> value.joinToString(",") }

    fun String.getJsonPaths(paths: Set<String>): Map<String, String?> {
        if (this.isBlank()) {
            return emptyMap()
        }

        val jsonContext: DocumentContext = JsonPath.parse(this)
        return paths.map { path ->
            path to jsonContext.readStringOrNull(path)
        }.toMap()
    }

    private fun String.escape(): String {
        var escaped = this
        charactersToEscape.forEach { escaped = escaped.replace(it.toString(), "\\$it") }
        return escaped
    }

    private fun DocumentContext.readStringOrNull(path: String): String? {
        return try {
            read<Any>(path).toString()
        } catch (ex: Exception) {
            null
        }
    }
}