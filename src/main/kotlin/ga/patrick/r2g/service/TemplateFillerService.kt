package ga.patrick.r2g.service

import ga.patrick.r2g.client.GraphDTO
import ga.patrick.r2g.property.Mapping
import javax.servlet.http.HttpServletRequest

interface TemplateFillerService {
    fun fillTemplate(mapping: Mapping, request: HttpServletRequest): GraphDTO
    fun getVariables(mapping: Mapping, request: HttpServletRequest): Map<String, String?>
}