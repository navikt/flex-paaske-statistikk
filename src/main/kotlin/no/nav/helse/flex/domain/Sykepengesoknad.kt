package no.nav.helse.flex.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Sykepengesoknad(
    val id: String,
    val fnr: String,
    val soknadstype: Soknadstype,
    val status: Soknadstatus,
    val opprettet: LocalDateTime?,
    val avbruttDato: LocalDate? = null,
    val sendtNav: LocalDateTime? = null,
    val korrigerer: String? = null,
    val korrigertAv: String? = null,
    val sporsmal: List<Sporsmal>,
    val fom: LocalDate?,
    val tom: LocalDate?,
    val sendtArbeidsgiver: LocalDateTime? = null,
    val arbeidsgiverOrgnummer: String? = null,
    val arbeidsgiverNavn: String? = null,
    val egenmeldtSykmelding: Boolean? = null,
    val avbruttFeilinfo: Boolean? = null,

) {

    fun getSporsmalMedTag(tag: String): Sporsmal {
        return getSporsmalMedTagOrNull(tag)
            ?: throw RuntimeException("Søknaden inneholder ikke spørsmål med tag: $tag")
    }

    fun getSporsmalMedTagOrNull(tag: String): Sporsmal? {
        return sporsmal.flatten().firstOrNull { s -> s.tag == tag }
    }
}

fun List<Sporsmal>.flatten(): List<Sporsmal> =
    flatMap {
        mutableListOf(it).apply {
            addAll(it.undersporsmal.flatten())
        }
    }
