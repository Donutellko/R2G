package ga.patrick.r2g.service.impl

import ga.patrick.r2g.bpp.MeasureTime
import ga.patrick.r2g.client.GraphClient
import ga.patrick.r2g.property.MappingProperties
import ga.patrick.r2g.service.ForwarderService
import ga.patrick.r2g.service.MappingMatcherService
import ga.patrick.r2g.service.RequestProcessService
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@MeasureTime
@Service
class RequestProcessServiceImpl(
        private val mappingMatcherService: MappingMatcherService,
        private val templateFillerService: TemplateFillerServiceImpl,
        private val forwarderService: ForwarderService,
        private val graphClient: GraphClient,
        private val mappingProperties: MappingProperties
) : RequestProcessService {

    override fun processRequest(request: HttpServletRequest): ResponseEntity<String> {
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
