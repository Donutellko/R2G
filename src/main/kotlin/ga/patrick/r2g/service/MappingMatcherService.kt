package ga.patrick.r2g.service

import ga.patrick.r2g.property.Mapping
import javax.servlet.http.HttpServletRequest

interface MappingMatcherService {
    fun findMapping(request: HttpServletRequest): Mapping?
}