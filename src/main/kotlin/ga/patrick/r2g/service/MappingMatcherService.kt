package ga.patrick.r2g.service

import ga.patrick.r2g.property.Mapping
import ga.patrick.r2g.property.MappingProperties
import ga.patrick.r2g.util.fullUri
import mu.KLogging
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class MappingMatcherService(
        private val mappingProperties: MappingProperties
) {


    fun findMapping(request: HttpServletRequest): Mapping? {
        val mappings = mappingProperties.mappings
        val matchingMappers = mappings
                .filter { mapping -> mapping.matches(request) }

        return when {
            matchingMappers.isEmpty() -> null
            matchingMappers.size == 1 -> matchingMappers.single()
            else -> throw Exception("Matched ${matchingMappers.size} mappings for ${request.method} ${request.fullUri}, expected 1 or 0. " +
                    "Matched paths: ${matchingMappers.map { it.methods + it.path }}")
        }
    }

    companion object : KLogging()
}

private fun Mapping.matches(request: HttpServletRequest) =
        HttpMethod.valueOf(request.method) in methods &&
                (pathRegex.matches(request.requestURI)
                        || pathRegex.matches(request.fullUri))
