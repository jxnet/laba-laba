package laba.laba.server;

import laba.laba.server.exception.BreakException;
import laba.laba.server.exception.PcapErrorException;
import laba.laba.server.util.PacketWrapper;
import laba.laba.shared.model.Record;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.foreign.memory.Pointer;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class ApplicationServer {

    public static KafkaProducer<String, Record> producer() throws IOException {
        Properties properties = new Properties();
        String propertiesFile = System.getProperty("profiles.active", "development");
        if (propertiesFile.equals("production")) {
            propertiesFile = "profiles/"+ propertiesFile +"/application.properties";
        } else {
            propertiesFile = "profiles/" + propertiesFile + "/application.properties";
        }
        properties.load(ApplicationServer.class.getClassLoader().getResourceAsStream(propertiesFile));
        return new KafkaProducer<>(properties);
    }

    public static void main(String[] args) throws IOException, PcapErrorException, BreakException {
        Producer producer = producer();
        PcapInterface pcapInterface = PcapInterface.lookup();
        Pcap pcap = pcapInterface.openLive();
        pcap.loop(10, (PcapHandler) (user, header, packet) -> {
            String key = UUID.randomUUID().toString();
            PacketWrapper wrapper = new PacketWrapper(header, packet);
            ProducerRecord record = new ProducerRecord("packet", key, wrapper);
            producer.send(record);
        }, Pointer.ofNull());
        pcap.close();
        producer.close();
    }

}
