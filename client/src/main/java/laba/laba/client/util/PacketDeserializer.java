package laba.laba.client.util;

import com.ardikars.common.logging.Logger;
import com.ardikars.common.logging.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import laba.laba.shared.model.Record;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class PacketDeserializer implements Deserializer<Record> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDeserializer.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Record deserialize(String topic, byte[] data) {
        try {
            return OBJECT_MAPPER.readValue(data, Record.class);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

}
