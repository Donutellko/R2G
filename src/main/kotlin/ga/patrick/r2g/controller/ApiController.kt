package ga.patrick.r2g.controller

import ga.patrick.r2g.service.RequestProcessService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class ApiController(
        val requestProcessService: RequestProcessService
) {

    @GetMapping("/**")
    fun get(request: HttpServletRequest): ResponseEntity<Any> {
        val uri = request.requestURI
                .split(request.contextPath + "/all/")[1]
        return requestProcessService.processGet(uri)
    }
}