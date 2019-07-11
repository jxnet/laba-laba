package laba.laba.server.util;

import laba.laba.server.PcapPktHdr;

import java.foreign.memory.Pointer;

public class PacketWrapper {

    private Pointer<PcapPktHdr> header;

    private Pointer<Byte> buffer;

    public PacketWrapper(Pointer<PcapPktHdr> header, Pointer<Byte> buffer) {
        this.header = header;
        this.buffer = buffer;
    }

    public Pointer<PcapPktHdr> getHeader() {
        return header;
    }

    public Pointer<Byte> getBuffer() {
        return buffer;
    }

}
