package ga.patrick.r2g.service

import ga.patrick.r2g.client.GraphClient
import ga.patrick.r2g.property.MappingProperties
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
        val mapping = mappingMatcherService.findMapping(request)

        return if (mapping == null) {
            forwarderService.send(request)
        } else {
            val graphRequest = templateFillerService.fillTemplate(mapping, request)
            val uri = mappingProperties.endpoints.getValue(mapping.endpointName).uri
            graphClient.send(uri, graphRequest)
        }
    }

    companion object : KLogging()
}
