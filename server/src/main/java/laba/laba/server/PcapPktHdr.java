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

import java.foreign.annotations.NativeGetter;
import java.foreign.annotations.NativeStruct;
import java.foreign.memory.Struct;

/**
 * Header of a packet in the dump file.
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.0.0
 */
@NativeStruct(
        value = "[${timeval}(ts)u32(caplen)u32(len)](pcap_pkthdr)",
        resolutionContext = { Timestamp.class }
)
public interface PcapPktHdr extends Struct<PcapPktHdr> {

    @NativeGetter("ts")
    Timestamp timestamp();

    @NativeGetter("caplen")
    int captureLength();

    @NativeGetter("len")
    int length();

    default String json() {
        return new StringBuilder()
                .append("{")
                .append("\"timestamp\":{")
                .append("\"second\":").append(timestamp().second()).append(",")
                .append("\"microSecond\":").append(timestamp().microSecond()).append(",")
                .append("},")
                .append("\"captureLength\":").append(captureLength()).append(",")
                .append("\"length\":").append(length())
                .append("}")
                .toString();
    }

}
