package no.nav.helse.flex.paske

import no.nav.helse.flex.domain.Periode
import no.nav.helse.flex.domain.Soknadstatus
import no.nav.helse.flex.domain.Soknadstype
import no.nav.helse.flex.domain.Sporsmal
import no.nav.helse.flex.domain.Sykepengesoknad
import no.nav.helse.flex.objectMapper
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class Paskemetrikk {
    fun prossesser(soknad: Sykepengesoknad) {
        MetrikkRepo.prossessert.incrementAndGet()
        with(soknad) {
            if (arbeidstaker() && sendtEllerKorrigert()) {
                MetrikkRepo.dager.forEach {
                    sjekkDag(it.second)
                }
            }
        }
    }
}

fun Sykepengesoknad.arbeidstaker(): Boolean {
    return this.soknadstype == Soknadstype.ARBEIDSTAKERE
}

fun Sykepengesoknad.sjekkDag(helligdag: Dag) {
    if (helligdag.dag.isBetweenInclusive(this.fom!!, this.tom!!)) {
        helligdag.soknad.incrementAndGet()
        val feriesporsmal = this.getSporsmalMedTagOrNull("FERIE_V2")
        val forsteSvar = feriesporsmal?.forsteSvar
        if (forsteSvar == "JA") {
            helligdag.haddeFerieIPerioden.incrementAndGet()

            val ferieOverDagen = getSporsmalMedTag("FERIE_NAR")
                .hentPeriode()
                .any { helligdag.dag.isBetweenInclusive(it) }
            if (ferieOverDagen) {
                helligdag.feriePaaDenneDagen.incrementAndGet()
            }
        }
    }
}

fun Sykepengesoknad.sendtEllerKorrigert(): Boolean {
    return this.status == Soknadstatus.KORRIGERT || this.status == Soknadstatus.SENDT
}

fun LocalDate.isBetweenInclusive(periode: Periode): Boolean {
    return this.isBetweenInclusive(periode.fom, periode.tom)
}

fun LocalDate.isBetweenInclusive(start: LocalDate, end: LocalDate): Boolean {
    return !this.isBefore(start) && !this.isAfter(end)
}

private fun Sporsmal.hentPeriode(): List<Periode> {
    return this.svar.map { it.verdi.getJsonPeriode() }
}

fun String.getJsonPeriode(): Periode {
    return objectMapper.readValue(this, Periode::class.java)
}