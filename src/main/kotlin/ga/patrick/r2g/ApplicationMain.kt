package ga.patrick.r2g

import ga.patrick.r2g.property.ApiProperties
import ga.patrick.r2g.property.MappingProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
        ApiProperties::class,
        MappingProperties::class)
class MainApplication

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}
