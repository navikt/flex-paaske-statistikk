package no.nav.helse.flex.paske

import java.time.LocalDate

class Dag(val dag: LocalDate) {

    var soknad = 0
    var feriePaaDenneDagen = 0
    var haddeFerieIPerioden = 0
}

object MetrikkRepo {
    var antallSjekket = 0
    val dager = listOf(
        Dag(LocalDate.of(2019, 4, 18)),
        Dag(LocalDate.of(2019, 4, 19)),
        Dag(LocalDate.of(2019, 4, 22)),
        Dag(LocalDate.of(2019, 5, 1)),
        Dag(LocalDate.of(2019, 5, 17)),
        Dag(LocalDate.of(2020, 4, 9)),
        Dag(LocalDate.of(2020, 4, 10)),
        Dag(LocalDate.of(2020, 4, 13)),
        Dag(LocalDate.of(2020, 5, 1)),
        Dag(LocalDate.of(2020, 5, 17)),
        Dag(LocalDate.of(2021, 4, 1)),
        Dag(LocalDate.of(2021, 4, 2)),
        Dag(LocalDate.of(2021, 4, 5)),
        Dag(LocalDate.of(2021, 5, 1)),
        Dag(LocalDate.of(2021, 5, 17)),
        Dag(LocalDate.of(2022, 4, 14)),
        Dag(LocalDate.of(2022, 4, 15)),
        Dag(LocalDate.of(2022, 4, 18)),
    )
}
