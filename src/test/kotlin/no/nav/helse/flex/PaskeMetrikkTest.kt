package no.nav.helse.flex

import no.nav.helse.flex.domain.Soknadstatus
import no.nav.helse.flex.domain.Soknadstype
import no.nav.helse.flex.domain.Sporsmal
import no.nav.helse.flex.domain.Svar
import no.nav.helse.flex.paske.Paskecontroller
import no.nav.helse.flex.paske.Paskemetrikk
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.*

class PaskeTest : FellesTestOppsett() {

    @Autowired
    lateinit var paksecontroller: Paskecontroller

    @Autowired
    lateinit var paskemetrikk: Paskemetrikk

    @Test
    @Disabled
    fun ferieTest() {

        val metrikkFoer = paksecontroller.hentMetrik()
        metrikkFoer `should be equal to` """
Antall sjekket: 0

2020-05-01
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2020-05-17
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2021-04-01
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2021-04-02
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2021-04-05
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2021-05-01
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2021-05-17
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2022-04-14
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2022-04-15
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2022-04-18
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen
"""

        paskemetrikk.prossesser(skapSoknadUtenFerie().serialisertTilString())
        paskemetrikk.prossesser(skapSoknadMedFerie().serialisertTilString())

        val metrikkEtter = paksecontroller.hentMetrik()
        metrikkEtter `should be equal to` """
Antall sjekket: 2

2020-05-01
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2020-05-17
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2021-04-01
   2 soknader over dagen
   1 hadde ferie i søknaden
   1 hadde ferie på denne dagen
   50.0% av søknadene hadde ferie på denne dagen

2021-04-02
   2 soknader over dagen
   1 hadde ferie i søknaden
   1 hadde ferie på denne dagen
   50.0% av søknadene hadde ferie på denne dagen

2021-04-05
   2 soknader over dagen
   1 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   0.0% av søknadene hadde ferie på denne dagen

2021-05-01
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2021-05-17
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2022-04-14
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2022-04-15
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen

2022-04-18
   0 soknader over dagen
   0 hadde ferie i søknaden
   0 hadde ferie på denne dagen
   NaN% av søknadene hadde ferie på denne dagen
"""
    }
}

fun skapSoknadUtenFerie(): SykepengesoknadFull {
    return SykepengesoknadFull(
        soknadstype = Soknadstype.ARBEIDSTAKERE,
        status = Soknadstatus.SENDT,
        fom = LocalDate.of(2021, 4, 1),
        tom = LocalDate.of(2021, 4, 10),
        sporsmal = emptyList()
    )
}

fun skapSoknadMedFerie(): SykepengesoknadFull {
    return SykepengesoknadFull(
        soknadstype = Soknadstype.ARBEIDSTAKERE,
        status = Soknadstatus.SENDT,
        fom = LocalDate.of(2021, 4, 1),
        tom = LocalDate.of(2021, 4, 10),
        sporsmal = listOf(
            Sporsmal(
                tag = "FERIE_V2",
                svar = listOf(Svar(verdi = "JA", id = "sdf")),
                undersporsmal = listOf(
                    Sporsmal(
                        tag = "FERIE_NAR",
                        svar = listOf(Svar(verdi = "{\"fom\":\"2021-04-01\",\"tom\":\"2021-04-02\"}", id = "sdf")),
                        undersporsmal = emptyList()
                    )
                )
            )
        )
    )
}

data class SykepengesoknadFull(
    val soknadstype: Soknadstype,
    val status: Soknadstatus,
    val fom: LocalDate?,
    val tom: LocalDate?,
    val sporsmal: List<Sporsmal>,
)
fun Any.serialisertTilString(): String = objectMapper.writeValueAsString(this)
