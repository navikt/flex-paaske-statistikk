package no.nav.helse.flex.paske

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(value = ["/api/metrikk"], produces = [MediaType.TEXT_PLAIN_VALUE])
class Paskecontroller {

    @ResponseBody
    @RequestMapping(method = [RequestMethod.GET])
    fun hentMetrik(): String {
        val buf = StringBuffer()
        buf.append("\nAntall sjekket: ${MetrikkRepo.antallSjekket}\n")
        MetrikkRepo.dager.forEach { buf.append(it.second.formater()) }

        return buf.toString()
    }
}

private fun Dag.formater(): String {
    val prosent = try {
        this.feriePaaDenneDagen.toDouble() / this.soknad.toDouble() * 100
    } catch (e: Exception) {
        0.0
    }

    val buf = StringBuffer()
    buf.append("\n${this.dag}\n")
    buf.append("   ${this.soknad} soknader over dagen\n")
    buf.append("   ${this.haddeFerieIPerioden} hadde ferie i søknaden\n")
    buf.append("   ${this.feriePaaDenneDagen} hadde ferie på denne dagen\n")
    buf.append("   $prosent% av søknadene hadde ferie på denne dagen\n")
    return buf.toString()
}
