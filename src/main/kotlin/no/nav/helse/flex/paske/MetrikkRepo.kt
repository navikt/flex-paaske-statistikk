package no.nav.helse.flex.paske

import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

class Dag(val dag: LocalDate) {

    val soknad = AtomicInteger(0)
    val feriePaaDenneDagen = AtomicInteger(0)
    val haddeFerieIPerioden = AtomicInteger(0)
}

val dagerSomSjekkes = listOf(
    LocalDate.of(2020, 5, 1),
    LocalDate.of(2020, 5, 17),
    LocalDate.of(2021, 4, 1),
    LocalDate.of(2021, 4, 2),
    LocalDate.of(2021, 4, 5),
    LocalDate.of(2021, 5, 1),
    LocalDate.of(2021, 5, 17),
    LocalDate.of(2022, 4, 14),
    LocalDate.of(2022, 4, 15),
    LocalDate.of(2022, 4, 18),
)

object MetrikkRepo {
    val prossessert = AtomicInteger(0)
    val dager = dagerSomSjekkes.map { it to Dag(it) }
}
