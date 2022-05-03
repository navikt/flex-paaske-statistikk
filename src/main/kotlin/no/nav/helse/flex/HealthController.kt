package no.nav.helse.flex

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(value = ["/internal/health"], produces = [MediaType.APPLICATION_JSON_VALUE])
class HealthController {

    @ResponseBody
    @RequestMapping(method = [RequestMethod.GET])
    fun hentHealth(): String {
        return "OK"
    }
}
