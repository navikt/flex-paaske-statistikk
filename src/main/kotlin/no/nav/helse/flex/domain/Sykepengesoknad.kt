package no.nav.helse.flex.domain

import java.time.LocalDate

data class Sykepengesoknad(
    val soknadstype: Soknadstype,
    val status: Soknadstatus,
    val fom: LocalDate?,
    val tom: LocalDate?,
    val sporsmal: List<Sporsmal>,
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
