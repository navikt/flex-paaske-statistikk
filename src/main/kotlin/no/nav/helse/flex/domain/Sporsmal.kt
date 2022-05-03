package no.nav.helse.flex.domain

data class Sporsmal(
    val id: String? = null,
    val tag: String,
    val sporsmalstekst: String? = null,
    val undertekst: String? = null,
    val svartype: Svartype,
    val min: String? = null,
    val max: String? = null,
    val pavirkerAndreSporsmal: Boolean = false,
    val kriterieForVisningAvUndersporsmal: Visningskriterie? = null,
    val svar: List<Svar> = emptyList(),
    val undersporsmal: List<Sporsmal> = emptyList()
) {
    val forsteSvar: String?
        get() = if (svar.isEmpty())
            null
        else
            svar[0].verdi
}
