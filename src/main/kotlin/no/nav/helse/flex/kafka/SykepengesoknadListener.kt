package no.nav.helse.flex.kafka

import no.nav.helse.flex.paske.Paskemetrikk
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

const val SENDT_SYKEPENGESOKNAD_TOPIC = "flex." + "syfosoknad-sykepengesoknad-migrering"

@Component
class SykepengesoknadListener(
    private val paskemetrikk: Paskemetrikk,
) {

    @KafkaListener(
        topics = [SENDT_SYKEPENGESOKNAD_TOPIC],
        concurrency = "1",
        containerFactory = "importKafkaListenerContainerFactory",
    )
    fun listenBatch(cr: List<ConsumerRecord<String, String>>, acknowledgment: Acknowledgment) {

        cr.forEach {
            paskemetrikk.prossesser(it.value())
        }

        acknowledgment.acknowledge()
    }
}
