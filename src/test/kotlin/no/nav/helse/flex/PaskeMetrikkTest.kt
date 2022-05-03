package no.nav.helse.flex

import no.nav.helse.flex.domain.Soknadstatus
import no.nav.helse.flex.domain.Soknadstype
import no.nav.helse.flex.domain.Sporsmal
import no.nav.helse.flex.domain.Svar
import no.nav.helse.flex.domain.Svartype
import no.nav.helse.flex.domain.Sykepengesoknad
import no.nav.helse.flex.domain.Visningskriterie
import no.nav.helse.flex.paske.Paskecontroller
import no.nav.helse.flex.paske.Paskemetrikk
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class PaskeTest : FellesTestOppsett() {

    @Autowired
    lateinit var paksecontroller: Paskecontroller

    @Autowired
    lateinit var paskemetrikk: Paskemetrikk

    @Test
    fun ferieTest() {

        val metrikkFoer = paksecontroller.hentMetrik()
        metrikkFoer `should be equal to` """
Sjekket 0

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

        paskemetrikk.prossesser(skapSoknadUtenFerie())
        paskemetrikk.prossesser(skapSoknadMedFerie())

        val metrikkEtter = paksecontroller.hentMetrik()
        metrikkEtter `should be equal to` """
Sjekket 2

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

fun skapSoknadUtenFerie(): Sykepengesoknad {
    return Sykepengesoknad(
        fnr = "123",
        id = UUID.randomUUID().toString(),
        soknadstype = Soknadstype.ARBEIDSTAKERE,
        status = Soknadstatus.SENDT,
        opprettet = LocalDateTime.now(),
        fom = LocalDate.of(2021, 4, 1),
        tom = LocalDate.of(2021, 4, 10),
        sporsmal = emptyList()
    )
}

fun skapSoknadMedFerie(): Sykepengesoknad {
    return Sykepengesoknad(
        fnr = "123",
        id = UUID.randomUUID().toString(),
        soknadstype = Soknadstype.ARBEIDSTAKERE,
        status = Soknadstatus.SENDT,
        opprettet = LocalDateTime.now(),
        fom = LocalDate.of(2021, 4, 1),
        tom = LocalDate.of(2021, 4, 10),
        sporsmal = listOf(
            Sporsmal(
                tag = "FERIE_V2",
                sporsmalstekst = "Bla bla?",
                svar = listOf(Svar(verdi = "JA", id = "sdf")),
                svartype = Svartype.JA_NEI,
                kriterieForVisningAvUndersporsmal = Visningskriterie.JA,
                undersporsmal = listOf(
                    Sporsmal(
                        tag = "FERIE_NAR",
                        sporsmalstekst = "Bla bla?",
                        svar = listOf(Svar(verdi = "{\"fom\":\"2021-04-01\",\"tom\":\"2021-04-02\"}", id = "sdf")),
                        svartype = Svartype.JA_NEI,
                        kriterieForVisningAvUndersporsmal = Visningskriterie.JA,
                        undersporsmal = emptyList()
                    )
                )
            )
        )
    )
}
