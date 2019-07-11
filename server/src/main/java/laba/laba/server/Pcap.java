package laba.laba.server;

import com.ardikars.common.logging.Logger;
import com.ardikars.common.logging.LoggerFactory;
import com.ardikars.common.memory.Memories;
import com.ardikars.common.memory.Memory;
import com.ardikars.jxpacket.common.UnknownPacket;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import laba.laba.server.exception.BreakException;
import laba.laba.server.exception.PcapErrorException;
import laba.laba.server.internal.foreign.bpf_mapping;
import laba.laba.server.internal.foreign.pcap_mapping;

import java.foreign.Libraries;
import java.foreign.NativeTypes;
import java.foreign.Scope;
import java.foreign.memory.Callback;
import java.foreign.memory.LayoutType;
import java.foreign.memory.Pointer;
import java.lang.invoke.MethodHandles;

public class Pcap {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pcap.class);

    static final int OK = 0;

    static final Object LOCK = new Object();

    static final pcap_mapping MAPPING = Libraries.bind(MethodHandles.lookup(), pcap_mapping.class);

    static final int ERRBUF_SIZE = 256;

    static final Scope SCOPE = Libraries.libraryScope(MAPPING);

    final Pointer<pcap_mapping.pcap> pcap;
    final Pointer<bpf_mapping.bpf_program> bpf_program;
    final Pointer<PcapStat> pcap_stat;
    final int netmask;
    final int linktype;

    boolean filterActivated;

    Pcap(Pointer<pcap_mapping.pcap> pcap) {
        this(pcap, 0xFFFFFF00);
    }

    Pcap(Pointer<pcap_mapping.pcap> pcap, int netmask) {
        this.pcap = pcap;
        this.bpf_program = SCOPE.allocate(LayoutType.ofStruct(bpf_mapping.bpf_program.class));
        this.pcap_stat = SCOPE.allocate(LayoutType.ofStruct(PcapStat.class));
        this.netmask = netmask;
        this.linktype = MAPPING.pcap_datalink(pcap);
        this.filterActivated = false;
    }

    public static Pcap openOffline(String file) throws PcapErrorException {
        synchronized (LOCK) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Opening file: {}", file);
            }
            Pointer<Byte> errbuf = SCOPE.allocate(NativeTypes.INT8, ERRBUF_SIZE);
            Pointer<pcap_mapping.pcap> pointer = MAPPING.pcap_open_offline(SCOPE.allocateCString(file), errbuf);
            if (pointer == null || pointer.isNull()) {
                throw new PcapErrorException(Pointer.toString(errbuf));
            }
            return new Pcap(pointer);
        }
    }

    public static Pcap openOffline(String file, PcapTStampPrecision tStampPrecision) throws PcapErrorException {
        synchronized (LOCK) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Opening file ({}) with timestamp precision ({})", file, tStampPrecision.getValue());
            }
            Pointer<Byte> errbuf = SCOPE.allocate(NativeTypes.INT8, ERRBUF_SIZE);
            Pointer<pcap_mapping.pcap> pointer = MAPPING.pcap_open_offline_with_tstamp_precision(SCOPE.allocateCString(file),
                    tStampPrecision.getValue(), errbuf);
            if (pointer == null || pointer.isNull()) {
                throw new PcapErrorException(Pointer.toString(errbuf));
            }
            return new Pcap(pointer);
        }
    }

    public PcapDumper dumpOpen(String file) throws PcapErrorException {
        synchronized (LOCK) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Open dumper handler at new file on {}", file);
            }
            Pointer<pcap_mapping.pcap_dumper> pointer = MAPPING.pcap_dump_open(pcap, SCOPE.allocateCString(file));
            if (pointer == null || pointer.isNull()) {
                throw new PcapErrorException(Pointer.toString(MAPPING.pcap_geterr(pcap)));
            }
            return new PcapDumper(pointer);
        }
    }

    public PcapDumper dumpOpenAppend(String file) throws PcapErrorException {
        synchronized (LOCK) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Open dumper handler at existing file on {}", file);
            }
            Pointer<pcap_mapping.pcap_dumper> pointer = MAPPING.pcap_dump_open_append(pcap, SCOPE.allocateCString(file));
            if (pointer == null || pointer.isNull()) {
                throw new PcapErrorException(Pointer.toString(MAPPING.pcap_geterr(pcap)));
            }
            return new PcapDumper(pointer);
        }
    }

    public void dump(Pointer<Byte> user, Pointer<PcapPktHdr> header, Pointer<Byte> packets) {
        synchronized (LOCK) {
            MAPPING.pcap_dump(user, header, packets);
        }
    }

    public void setFilter(String filter, boolean optimize) throws PcapErrorException {
        synchronized (LOCK) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Set filter with expression {} and optimize is {}", filter, optimize ? "enabled" : "disabled");
            }
            if (MAPPING.pcap_compile(pcap,bpf_program, SCOPE.allocateCString(filter), optimize ? 1 : 0, netmask) != OK) {
                throw new PcapErrorException(Pointer.toString(MAPPING.pcap_geterr(pcap)));
            }
            if (MAPPING.pcap_setfilter(pcap, bpf_program) != OK) {
                throw new PcapErrorException(Pointer.toString(MAPPING.pcap_geterr(pcap)));
            }
            this.filterActivated = true;
        }
    }

    public  <T> void loop(int count, PcapHandler handler, Pointer<Byte> user) throws BreakException, PcapErrorException {
        synchronized (LOCK) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Looping {} packets", count);
            }
            Callback<PcapHandler> callback = SCOPE.allocateCallback(PcapHandler.class, handler);
            int result = MAPPING.pcap_loop(pcap, count, callback, user);
            if (result == 0) {
                return; // OK
            } else if (result == -2) {
                throw new BreakException("");
            } else {
                throw new PcapErrorException("Generic error.");
            }
        }
    }

    public  <T> void loop(int count, PacketListener handler, Pointer<Byte> user) throws BreakException, PcapErrorException {
        synchronized (LOCK) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Looping {} packets", count);
            }
            Callback<PcapHandler> callback = SCOPE.allocateCallback(PcapHandler.class, (user1, header, packets) -> {
                Memory memory = Memories.wrap(packets.asDirectByteBuffer(header.get().captureLength()));
                memory.writerIndex(memory.capacity());
                if (linktype == 1) {
                    handler.gotPacket(user1, header, Ethernet.newPacket(memory));
                } else{
                    handler.gotPacket(user1, header, UnknownPacket.newPacket(memory));
                }
            });
            int result = MAPPING.pcap_loop(pcap, count, callback, user);
            if (result == 0) {
                return; // OK
            } else if (result == -2) {
                throw new BreakException("");
            } else {
                throw new PcapErrorException("Generic error.");
            }
        }
    }

    public PcapStat status() {
        synchronized (LOCK) {
            int result = MAPPING.pcap_stats(pcap, pcap_stat);
            return pcap_stat.get();
        }
    }

    public void breakLoop() {
        synchronized (LOCK) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Break looping packets.");
            }
            MAPPING.pcap_breakloop(pcap);
        }
    }

    public void close() {
        synchronized (LOCK) {
            if (!bpf_program.isNull() && filterActivated) {
                MAPPING.pcap_freecode(bpf_program);
            }
            if (!pcap.isNull()) {
                MAPPING.pcap_close(pcap);
            }
        }
    }

}
