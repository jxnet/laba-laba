package laba.laba.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PacketHeader {

    @JsonProperty("timestamp")
    private Timestamp timestamp;

    @JsonProperty("captureLength")
    private int captureLength;

    @JsonProperty("length")
    private int length;

    public PacketHeader() {
        //
    }

    public PacketHeader(Timestamp timestamp, int captureLength, int length) {
        this.timestamp = timestamp;
        this.captureLength = captureLength;
        this.length = length;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getCaptureLength() {
        return captureLength;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "PacketHeader{" +
                "timestamp=" + timestamp +
                ", captureLength=" + captureLength +
                ", length=" + length +
                '}';
    }

}
