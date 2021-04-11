package ga.patrick.r2g.service

import ga.patrick.r2g.property.Mapping
import ga.patrick.r2g.property.MappingProperties
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class MappingMatcherService(
        private val mappingProperties: MappingProperties
) {

    fun findMapping(uri: String, method: String): Mapping? {
        val matchingMappers = mappingProperties.mappings
                .filter { it.method == method && it.pathRegex.matches(uri) }

        return when {
            matchingMappers.isEmpty() -> null
            matchingMappers.size == 1 -> matchingMappers.single()
            else -> throw Exception("Matched ${matchingMappers.size} mappings for $method $uri, expected 1 or 0. " +
                    "Matched paths: ${matchingMappers.map { it.method + it.path }} ")
        }
    }

    companion object : KLogging()
}