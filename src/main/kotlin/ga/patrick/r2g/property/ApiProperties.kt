package ga.patrick.r2g.property

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
class ApiProperties(
        val gitLink: String
)