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
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.0.0
 */
@NativeStruct("[i64(tv_sec)i32(tv_usec)x32](timeval)")
public interface Timestamp extends Struct<Timestamp> {

    @NativeGetter("tv_sec")
    long second();

    @NativeGetter("tv_usec")
    int microSecond();

    default String json() {
        return new StringBuilder()
                .append("{\n")
                .append("\t\"second\": \"").append(second()).append("\",\n")
                .append("\t\"microSecond\": \"").append(microSecond()).append("\"\n")
                .append("}")
                .toString();
    }

}
