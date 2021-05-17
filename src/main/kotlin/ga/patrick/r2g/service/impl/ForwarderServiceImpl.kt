package ga.patrick.r2g.service.impl

import ga.patrick.r2g.bpp.MeasureTime
import ga.patrick.r2g.configuration.WebclientConfiguration
import ga.patrick.r2g.configuration.WebclientConfiguration.Companion.FORWARDER_CLIENT
import ga.patrick.r2g.service.ForwarderService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import javax.servlet.http.HttpServletRequest

@MeasureTime
@Component
class ForwarderServiceImpl(
        @Qualifier(FORWARDER_CLIENT)
        private val forwarderClient: WebClient
) : ForwarderService {

    override fun send(requestEntity: HttpServletRequest): ResponseEntity<String> {
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
                .onRawStatus({ true }) { Mono.empty() } // ignore any errors, proceed with actual response
                .toEntity(String::class.java)
                .block()!!
    }
}