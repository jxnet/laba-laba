package laba.laba.server.util;

import com.ardikars.common.logging.Logger;
import com.ardikars.common.logging.LoggerFactory;
import com.ardikars.common.util.Hexs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import laba.laba.server.PcapPktHdr;
import laba.laba.shared.model.PacketHeader;
import laba.laba.shared.model.Record;
import laba.laba.shared.model.Timestamp;
import org.apache.kafka.common.serialization.Serializer;

public class PacketSerializer implements Serializer<PacketWrapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketSerializer.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, PacketWrapper data) {
        try {
            PcapPktHdr pktHdr = data.getHeader().get();
            PacketHeader header = new PacketHeader(new Timestamp(pktHdr.timestamp().second(), pktHdr.timestamp().microSecond()),
                pktHdr.captureLength(), pktHdr.length());
            String buffer = Hexs.toHexString(data.getBuffer().asDirectByteBuffer(header.getCaptureLength()));
            Record record = new Record(header, buffer);
            return OBJECT_MAPPER.writeValueAsString(record).getBytes();
        } catch (IllegalAccessException e) {
            LOGGER.error(e);
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
        }
        return new byte[0];
    }

}
