package ga.patrick.r2g.service

import ga.patrick.r2g.client.GraphDAO
import ga.patrick.r2g.property.Mapping
import ga.patrick.r2g.util.VariableUtils.fillTemplate
import ga.patrick.r2g.util.VariableUtils.formatAs
import ga.patrick.r2g.util.VariableUtils.getJsonPaths
import ga.patrick.r2g.util.VariableUtils.getPathVariables
import ga.patrick.r2g.util.VariableUtils.getQueryVariables
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class TemplateFillerService {

    fun fillTemplate(mapping: Mapping, request: HttpServletRequest): GraphDAO {
        val fillingVariables = getVariables(mapping, request)

        val requestVariables = mapping.variables
                .mapNotNull { definition ->
                    fillingVariables[definition.source]?.formatAs(definition.type)
                            ?.let{ definition.name to it }
                }.toMap()

        return GraphDAO(
                query = mapping.template.fillTemplate(fillingVariables),
                variables = requestVariables
        )
    }

    private fun getVariables(mapping: Mapping, request: HttpServletRequest): Map<String, String?> {
        val queryParams = request.parameterMap.getQueryVariables()

        val uriParams = request.requestURI.getPathVariables(mapping.path)

        val requestBody = request.reader.readText()
        val variablesSources = mapping.variables.map { it.source }
        val bodyParams = requestBody.getJsonPaths(mapping.paths + variablesSources)

        val fillingVariables: Map<String, String?> = queryParams + uriParams + bodyParams

        return fillingVariables
    }
}