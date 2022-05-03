package no.nav.helse.flex.kafka

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.helse.flex.domain.Sykepengesoknad
import no.nav.helse.flex.objectMapper
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
        concurrency = "12",
        containerFactory = "importKafkaListenerContainerFactory",
    )
    fun listenBatch(cr: List<ConsumerRecord<String, String>>, acknowledgment: Acknowledgment) {

        cr.forEach {
            val soknad = it.value().tilSykepengesoknadDTO()
            paskemetrikk.prossesser(soknad)
        }

        acknowledgment.acknowledge()
    }

    fun String.tilSykepengesoknadDTO(): Sykepengesoknad = objectMapper.readValue(this)
}
