package laba.laba.server;

import laba.laba.server.internal.foreign.pcap_mapping;

import java.foreign.NativeTypes;
import java.foreign.memory.Pointer;

public class PcapDumper {

    Pointer<pcap_mapping.pcap_dumper> pcap_dumper;

    PcapDumper(Pointer<pcap_mapping.pcap_dumper> pcap_dumper) {
        this.pcap_dumper = pcap_dumper;
    }

    public long position() {
        synchronized (Pcap.LOCK) {
            return Pcap.MAPPING.pcap_dump_ftell(pcap_dumper);
        }
    }

    public void flush() {
        synchronized (Pcap.LOCK) {
            Pcap.MAPPING.pcap_dump_flush(pcap_dumper);
        }
    }

    public void close() {
        synchronized (Pcap.LOCK) {
            if (!pcap_dumper.isNull()) {
                Pcap.MAPPING.pcap_dump_close(pcap_dumper);
            }
        }
    }

    public Pointer<Byte> pointer() {
        return pcap_dumper.cast(NativeTypes.VOID).cast(NativeTypes.UINT8);
    }

}
