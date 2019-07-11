package laba.laba.client;

import com.ardikars.common.memory.Memories;
import com.ardikars.common.memory.Memory;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.fasterxml.jackson.databind.ObjectMapper;
import laba.laba.shared.model.Record;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ApplicationClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static KafkaConsumer<String, Record> consumer() throws IOException {
        Properties properties = new Properties();
        String propertiesFile = System.getProperty("profiles.active", "development");
        if (propertiesFile.equals("production")) {
            propertiesFile = "profiles/"+ propertiesFile +"/application.properties";
        } else {
            propertiesFile = "profiles/" + propertiesFile + "/application.properties";
        }
        properties.load(ApplicationClient.class.getClassLoader().getResourceAsStream(propertiesFile));
        return new KafkaConsumer(properties);
    }

    public static void main(String[] args) throws IOException {
        Consumer<String, Record> consumer = consumer();
        consumer.subscribe(Arrays.asList("packet"));
        while (true) {
            ConsumerRecords<String, Record> record = consumer.poll(Duration.ZERO);
            record.iterator().forEachRemaining(r -> {
                Memory memory = Memories.wrap(r.value().getPacketBuffer());
                memory.writerIndex(memory.capacity());
                Packet packet = Ethernet.newPacket(memory);
                packet.forEach(packet1 -> {
                    System.out.println(packet1);
                });
            });
        }
    }

}
