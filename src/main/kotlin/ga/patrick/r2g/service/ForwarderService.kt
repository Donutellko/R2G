package ga.patrick.r2g.service

import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import javax.servlet.http.HttpServletRequest

@Component
class ForwarderService(
        private val forwarderClient: WebClient
) {

    fun send(requestEntity: HttpServletRequest): ResponseEntity<String> {
        val body = requestEntity.reader.readText()
        val headers = requestEntity.headerNames.toList()
                .map { it to requestEntity.getHeaders(it).toList() }

        val method = HttpMethod.valueOf(requestEntity.method)

        val requestSpec = forwarderClient
                .method(method)
                .uri { uriBuilder ->
                    uriBuilder
                            .path(requestEntity.requestURI)
                            .query(requestEntity.queryString)
                            .build()
                }
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