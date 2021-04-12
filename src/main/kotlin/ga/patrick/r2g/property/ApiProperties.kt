package ga.patrick.r2g.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("api")
@ConstructorBinding
class ApiProperties(
        val gitLink: String?
)