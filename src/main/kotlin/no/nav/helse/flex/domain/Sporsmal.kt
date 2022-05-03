package no.nav.helse.flex.domain

data class Sporsmal(
    val tag: String,
    val svar: List<Svar> = emptyList(),
    val undersporsmal: List<Sporsmal> = emptyList()
) {
    val forsteSvar: String?
        get() = if (svar.isEmpty())
            null
        else
            svar[0].verdi
}
