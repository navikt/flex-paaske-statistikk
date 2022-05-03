package no.nav.helse.flex.kafka

import no.nav.helse.flex.logger
import no.nav.helse.flex.paske.Paskemetrikk
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

const val SENDT_SYKEPENGESOKNAD_TOPIC = "flex." + "syfosoknad-sykepengesoknad-migrering"

@Component
class SykepengesoknadListener(
    private val paskemetrikk: Paskemetrikk,
) {

    val log = logger()

    @KafkaListener(
        topics = [SENDT_SYKEPENGESOKNAD_TOPIC],
        concurrency = "1",
        containerFactory = "importKafkaListenerContainerFactory",
    )
    fun listenBatch(cr: List<ConsumerRecord<String, String>>, acknowledgment: Acknowledgment) {

        log.info("Mottar ${cr.size} records")
        val time = measureTimeMillis {
            cr.forEach {
                paskemetrikk.prossesser(it.value())
            }
            acknowledgment.acknowledge()
        }
        log.info("Prossesserte ${cr.size} records iløpet av $time millis")
    }
}
