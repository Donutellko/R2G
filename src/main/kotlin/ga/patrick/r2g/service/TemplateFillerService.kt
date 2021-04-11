package ga.patrick.r2g.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.ValueNode
import ga.patrick.r2g.property.Mapping
import ga.patrick.r2g.util.VariableUtils.flattenTree
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class TemplateFillerService(
        val objectMapper: ObjectMapper
) {

    fun fillTemplate(mapping: Mapping, request: HttpServletRequest): String {
        val queryParams = request.parameterMap
                .mapValues { (_, value) -> value.toString() }

        val bodyParams = getJsonParms(request.reader.readText())

        val variables = queryParams + bodyParams

        return mapping.template.fill(variables)
    }

    public fun getJsonParms(text: String): Map<String, String> {
        try {
            val tree: JsonNode = objectMapper.readTree(text)
            return tree.flattenTree().toMap()
        } catch (ex: Exception) {
            return emptyMap()
        }
    }


    private fun String.fill(variables: Map<String, String>): String {
        TODO("Not yet implemented")
    }
}
