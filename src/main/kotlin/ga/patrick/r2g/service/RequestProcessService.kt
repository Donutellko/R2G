package ga.patrick.r2g.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class RequestProcessService(
        private val mappingMatcherService: MappingMatcherService,
        private val templateFillerService: TemplateFillerService,
        private val forwarderService: ForwarderService
) {

    fun processRequest(request: HttpServletRequest): ResponseEntity<Any> {
        val mapping = mappingMatcherService.findMapping(request.requestURI, request.method)

        return if (mapping == null) {
            forwarderService.send(request)
        } else {
            val requestText = templateFillerService.fillTemplate(mapping, request)
//            graphService.send()
            ResponseEntity.ok("")
        }

        return ResponseEntity.ok("Hello")
    }
}