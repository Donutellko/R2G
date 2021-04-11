package ga.patrick.r2g.service

import ga.patrick.r2g.property.MappingProperties
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI
import javax.servlet.http.HttpServletRequest

@Component
class ForwarderService(
        mappingProperties: MappingProperties
) {
    val webclient = WebClient.builder()
            .baseUrl(mappingProperties.defaultEndpoint)
            .build()

    fun send(requestEntity: HttpServletRequest): ResponseEntity<String> {
        val path = URI.create(requestEntity.requestURI)
        val body = requestEntity.reader.readText()
        val headers = requestEntity.headerNames.toList()
                .map { it to requestEntity.getHeaders(it).toList() }

        val method = HttpMethod.valueOf(requestEntity.method)

        val requestSpec = webclient
                .method(method)
                .uri(path)
                .bodyValue(body)

        headers.forEach { (name, values) ->
            values.forEach { requestSpec.header(name, it) }
        }

        return requestSpec
                .retrieve()
                .toEntity(String::class.java)
                .block()!!
    }

}