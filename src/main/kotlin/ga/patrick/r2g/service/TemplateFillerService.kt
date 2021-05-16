package ga.patrick.r2g.service

import ga.patrick.r2g.client.GraphDAO
import ga.patrick.r2g.property.Mapping
import javax.servlet.http.HttpServletRequest

interface TemplateFillerService {
    fun fillTemplate(mapping: Mapping, request: HttpServletRequest): GraphDAO
    fun getVariables(mapping: Mapping, request: HttpServletRequest): Map<String, String?>
}