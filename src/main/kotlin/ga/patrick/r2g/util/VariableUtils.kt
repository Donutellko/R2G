package ga.patrick.r2g.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.ValueNode

object VariableUtils {
    private const val variableMatcher = """(\$\{.+?})"""

    fun String.toMatcher() = Regex(replace(Regex(variableMatcher), ".+"))

    fun JsonNode.flattenTree(path: String = ""): Set<Pair<String, String>> {
        return when (this) {
            is ValueNode -> setOf(path to textValue())

            is ArrayNode -> elements().asSequence()
                    .flatMapIndexed { i: Int, value: JsonNode -> value.flattenTree("$path[$i]") }
                    .toSet()

            is ObjectNode -> fields().asSequence()
                    .flatMap { (name, value) -> value.flattenTree("$path.$name") }
                    .toSet()

            else -> throw Exception(this.nodeType.toString())
        }
    }
}