package ga.patrick.r2g.service

import ga.patrick.r2g.client.GraphClient
import ga.patrick.r2g.property.MappingProperties
import ga.patrick.r2g.util.fullUri
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class RequestProcessService(
        private val mappingMatcherService: MappingMatcherService,
        private val templateFillerService: TemplateFillerService,
        private val forwarderService: ForwarderService,
        private val graphClient: GraphClient,
        private val mappingProperties: MappingProperties
) {

    fun processRequest(request: HttpServletRequest): ResponseEntity<String> {
        val mapping = mappingMatcherService.findMapping(request.fullUri, request.method)

        return if (mapping == null) {
            forwarderService.send(request)
        } else {
            val requestText = templateFillerService.fillTemplate(mapping, request)
            val uri = mappingProperties.endpoints.getValue(mapping.endpointName).uri
            logger.debug { "request for $uri: \n $requestText" }
            graphClient.send(uri, requestText)
        }
    }

    companion object : KLogging()
}
