package laba.laba.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Record {

    @JsonProperty("packetHeader")
    private PacketHeader packetHeader;

    @JsonProperty("packetBuffer")
    private String packetBuffer;

    public Record() {
        //
    }

    public Record(PacketHeader packetHeader, String packetBuffer) {
        this.packetHeader = packetHeader;
        this.packetBuffer = packetBuffer;
    }

    public PacketHeader getPacketHeader() {
        return packetHeader;
    }

    public String getPacketBuffer() {
        return packetBuffer;
    }

}
