package ga.patrick.r2g.service

import ga.patrick.r2g.property.MappingProperties
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URI
import javax.servlet.http.HttpServletRequest

@Component
class ForwarderService(
        val mappingProperties: MappingProperties
) {

    fun send(requestEntity: HttpServletRequest): ResponseEntity<Any> {
        val path = URI.create(mappingProperties.defaultEndpoint + requestEntity.requestURI)
        val body = requestEntity.reader.readText()
//
//        val forward = RequestEntity(
//                requestEntity.parts, requestEntity.hea,
//                requestEntity.method, uri
//        )
//
//        return restTemplate.exchange(forward)
        throw Exception("")
    }

}