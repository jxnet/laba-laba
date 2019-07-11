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

package laba.laba.server.internal;

import com.ardikars.common.net.Inet4Address;
import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.net.InetAddress;
import laba.laba.server.internal.foreign.pcap_mapping;

import java.foreign.memory.Pointer;

public class SockAddrParser {

    private static final SockAddrParser PARSER = new SockAddrParser();

    public static SockAddrParser getInstance() {
        return PARSER;
    }

    public InetAddress parse(Pointer<pcap_mapping.sockaddr> pointer) {
        if (!pointer.isNull()) {
            pcap_mapping.sockaddr sockaddr = pointer.get();
            if (sockaddr.sa_family$get() == 2) {
                byte[] data = new byte[Inet4Address.IPV4_ADDRESS_LENGTH];
                for (int i = 0; i < data.length; i++) {
                    data[i] = sockaddr.sa_data$get().get(i + 2);
                }
                return Inet4Address.valueOf(data);
            } else if (sockaddr.sa_family$get() == 10) {
                byte[] data = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
                for (int i = 0; i < data.length; i++) {
                    data[i] = sockaddr.sa_data$get().get(i);
                }
                return Inet6Address.valueOf(data);
            }
        }
        return null;
    }

}
