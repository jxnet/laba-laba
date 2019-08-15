package laba.laba.client;

import com.ardikars.common.memory.Memories;
import com.ardikars.common.memory.Memory;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.fasterxml.jackson.databind.ObjectMapper;
import laba.laba.client.config.ClientConfig;
import laba.laba.shared.model.Record;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ApplicationClient {

    public static KafkaConsumer<String, Record> consumer() {
        Map<String, Object> kafkaConfig = ClientConfig.getInstance().asMapKafkaConfig();
        return new KafkaConsumer<>(kafkaConfig);
    }

    public static void main(String[] args) {
        Consumer<String, Record> consumer = consumer();
        consumer.subscribe(ClientConfig.getInstance().getTopics());
        while (true) {
            ConsumerRecords<String, Record> record = consumer.poll(Duration.ZERO);
            record.iterator().forEachRemaining(r -> {
                Memory memory = Memories.wrap(r.value().getPacketBuffer());
                memory.writerIndex(memory.capacity());
                Packet packet = Ethernet.newPacket(memory);
                packet.forEach(pkt -> {
                    System.out.println(pkt);
                });
            });
        }
    }

}
