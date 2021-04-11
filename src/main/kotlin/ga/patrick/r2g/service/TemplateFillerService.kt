package ga.patrick.r2g.service

import ga.patrick.r2g.property.Mapping
import ga.patrick.r2g.util.VariableUtils.fillTemplate
import ga.patrick.r2g.util.VariableUtils.getJsonPaths
import ga.patrick.r2g.util.VariableUtils.getPathVariables
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class TemplateFillerService {

    fun fillTemplate(mapping: Mapping, request: HttpServletRequest): String {
        val queryParams = request.parameterMap
                .mapValues { (_, value) -> value.toString() }

        val uriParams = request.requestURI.getPathVariables(mapping.path)

        val requestBody = request.reader.readText()
        val bodyParams = requestBody.getJsonPaths(mapping.paths)

        val variables: Map<String, String?> = uriParams + queryParams + bodyParams

        return mapping.template.fillTemplate(variables)
    }

}
