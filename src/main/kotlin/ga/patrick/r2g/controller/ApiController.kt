package ga.patrick.r2g.controller

import ga.patrick.r2g.service.RequestProcessService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class ApiController(
        val requestProcessService: RequestProcessService
) {

    @RequestMapping("/**")
    fun endpoint(request: HttpServletRequest): ResponseEntity<String> {
        return requestProcessService.processRequest(request)
    }
}