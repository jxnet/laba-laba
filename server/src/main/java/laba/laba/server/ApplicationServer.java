package laba.laba.server;

import com.ardikars.common.logging.Logger;
import com.ardikars.common.logging.LoggerFactory;
import laba.laba.server.config.ServerConfig;
import laba.laba.server.internal.exception.BreakException;
import laba.laba.server.internal.exception.PcapErrorException;
import laba.laba.server.util.PacketWrapper;
import laba.laba.shared.model.Record;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.foreign.memory.Pointer;
import java.util.Map;
import java.util.UUID;

public class ApplicationServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServer.class);

    public static KafkaProducer<String, Record> producer() {
        Map<String, Object> kafkaConfig = ServerConfig.getInstance().asMapKafkaConfig();
        return new KafkaProducer<>(kafkaConfig);
    }

    public static void main(String[] args) throws PcapErrorException, BreakException {
        final Producer producer = producer();
        final PcapInterface pcapInterface = PcapInterface.lookup();
        final Pcap pcap = pcapInterface.openLive();
        pcap.setFilter("!(tcp port " + ServerConfig.getInstance().getKafkaPort() + ")", true);
        pcap.loop(10, (PcapHandler) (user, header, packet) -> {
            final String key = UUID.randomUUID().toString();
            final PacketWrapper wrapper = new PacketWrapper(header, packet);
            final ProducerRecord record = new ProducerRecord(ServerConfig.getInstance().getTopic(), key, wrapper);
            producer.send(record, (metadata, exception) -> {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("key ({})", key);
                    LOGGER.debug("hasOffset ({})", metadata.hasOffset());
                    LOGGER.debug("hasTimestamp ({})", metadata.hasTimestamp());
                    LOGGER.debug("offset ({})", metadata.offset());
                    LOGGER.debug("timestamp ({})", metadata.timestamp());
                    LOGGER.debug("partition ({})", metadata.partition());
                    LOGGER.debug("serializedKeySize ({})", metadata.serializedKeySize());
                    LOGGER.debug("serializedValueSize ({})", metadata.serializedValueSize());
                    LOGGER.debug("topic ({})", metadata.topic());
                }
            });
        }, Pointer.ofNull());
        pcap.close();
        producer.close();
    }

}
