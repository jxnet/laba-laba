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

/**
 * Pcap timestamp type.
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.0.0
 */
public enum PcapTStampType {

    HOST(0), HOST_LOWPREC(1), HOST_HIPREC(2), ADAPTER(3), ADAPTER_UNSYNCED(4);

    private final int value;

    PcapTStampType(final int value) {
        this.value = value;
    }

    /**
     * Get timestamp type value.
     * @return returns integer value.
     */
    public int getValue() {
        return this.value;
    }

}
