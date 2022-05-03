package no.nav.helse.flex.kafka

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.helse.flex.domain.Sykepengesoknad
import no.nav.helse.flex.logger
import no.nav.helse.flex.objectMapper
import no.nav.helse.flex.paske.Paskemetrikk
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

const val SENDT_SYKEPENGESOKNAD_TOPIC = "flex." + "syfosoknad-sykepengesoknad-migrering"

@Component
@Profile("test")
class SykepengesoknadListener(
    private val paskemetrikk: Paskemetrikk,
) {

    private val log = logger()
    @KafkaListener(
        topics = [SENDT_SYKEPENGESOKNAD_TOPIC],
        id = "sendtarbeidsgiverSoknader",
        idIsGroup = true,
        concurrency = "12",
        containerFactory = "importKafkaListenerContainerFactory",
        properties = [
            "auto.offset.reset=earliest"
        ],
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
