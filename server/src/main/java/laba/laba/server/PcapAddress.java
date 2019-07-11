/**
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package laba.laba.server;

import com.ardikars.common.net.InetAddress;
import laba.laba.server.internal.PcapAddressIterator;
import laba.laba.server.internal.SockAddrParser;
import laba.laba.server.internal.foreign.pcap_mapping;

import java.util.Iterator;

/**
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.0.0
 */
public class PcapAddress implements Iterable<PcapAddress> {

    PcapAddress next;
    InetAddress address;
    InetAddress netmask;
    InetAddress broadcast;
    InetAddress destination;

    PcapAddress() {
        //
    }

    PcapAddress(pcap_mapping.pcap_addr pcap_addr) {
        SockAddrParser parser = SockAddrParser.getInstance();
        this.address = parser.parse(pcap_addr.addr$get());
        this.netmask = parser.parse(pcap_addr.netmask$get());
        this.broadcast = parser.parse(pcap_addr.broadaddr$get());
        this.destination = parser.parse(pcap_addr.dstaddr$get());
        if (!pcap_addr.next$get().isNull()) {
            this.next = new PcapAddress(pcap_addr.next$get().get());
        }
    }

    public PcapAddress next() {
        return next;
    }

    public InetAddress address() {
        return address;
    }

    public InetAddress netmask() {
        return netmask;
    }

    public InetAddress broadcast() {
        return broadcast;
    }

    public InetAddress destination() {
        return destination;
    }

    @Override
    public Iterator<PcapAddress> iterator() {
        return new PcapAddressIterator(this);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{\n")
                .append("\t\"address\": \"").append(address).append("\",\n")
                .append("\t\"netmask\": \"").append(netmask).append("\",\n")
                .append("\t\"broadcast\": \"").append(broadcast).append("\",\n")
                .append("\t\"destination\": \"").append(destination).append("\"\n")
                .append("}")
                .toString();
    }

}
