package ga.patrick.r2g.service

import ga.patrick.r2g.property.Mapping
import ga.patrick.r2g.property.MappingProperties
import ga.patrick.r2g.util.fullUri
import mu.KLogging
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class MappingMatcherService(
        private val mappingProperties: MappingProperties
) {

    fun findMapping(request: HttpServletRequest): Mapping? {
        val matchingMappers = mappingProperties.mappings
                .filter {
                    it.method == request.method &&
                            (it.pathRegex.matches(request.requestURI)
                                    || it.pathRegex.matches(request.fullUri))
                }

        return when {
            matchingMappers.isEmpty() -> null
            matchingMappers.size == 1 -> matchingMappers.single()
            else -> throw Exception("Matched ${matchingMappers.size} mappings for ${request.method} ${request.fullUri}, expected 1 or 0. " +
                    "Matched paths: ${matchingMappers.map { it.method + it.path }} ")
        }
    }

    companion object : KLogging()
}