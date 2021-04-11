package ga.patrick.r2g.property

import ga.patrick.r2g.util.VariableUtils.toMatcher
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("r2g", ignoreUnknownFields = true)
@ConstructorBinding
data class MappingProperties(
        val defaultEndpoint: String,
        val endpoints: List<Endpoint>,
        val mappings: List<Mapping>
)

data class Endpoint(
        val name: String,
        val uri: String
)

data class Mapping(
        val path: String,
        val method: String,
        val endpoint: String,
        val template: String,
        val pathRegex: Regex = path.toMatcher()
)
