package ga.patrick.r2g.property

import ga.patrick.r2g.util.VariableUtils.getPaths
import ga.patrick.r2g.util.VariableUtils.toMatcher
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.annotation.PostConstruct

@ConfigurationProperties("r2g", ignoreUnknownFields = true)
@ConstructorBinding
class MappingProperties(
        val defaultEndpoint: String,
        val mappings: List<Mapping>,
        endpoints: List<Endpoint>
) {
    val endpoints: Map<String, Endpoint> = endpoints.associateBy { it.name }

    @PostConstruct
    fun validate() {
        val usedEndpointNames = mappings.map { it.endpointName }
        val missingEndpoints = usedEndpointNames - endpoints.keys
        if (missingEndpoints.isNotEmpty()) {
            throw Exception("Endpoints not found by names: $missingEndpoints.")
        }
    }
}

data class Endpoint(
        val name: String,
        val uri: String
)

data class Mapping(
        val path: String,
        val method: String,
        val endpointName: String,
        val template: String,
        val pathRegex: Regex = path.toMatcher(),
        val paths: Set<String> = template.getPaths()
)
