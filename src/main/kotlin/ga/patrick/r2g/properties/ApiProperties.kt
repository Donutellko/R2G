package ga.patrick.r2g.properties

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
class ApiProperties(
        val gitLink: String
)