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

package laba.laba.server.internal.foreign;

import java.foreign.annotations.NativeHeader;
import java.foreign.annotations.NativeFunction;
import java.foreign.annotations.NativeGetter;
import java.foreign.annotations.NativeStruct;
import java.foreign.memory.Pointer;
import java.foreign.memory.Struct;

/**
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.0.0
 */
@NativeHeader(
        libraries = {"pcap"}
)
public interface bpf_mapping {

    @NativeFunction("(u64:${bpf_insn}i32)i32")
    int bpf_validate(Pointer<bpf_insn> insn_p, int var1);

    @NativeFunction("(u64:${bpf_insn}u64:u8u32u32)u32")
    int bpf_filter(Pointer<bpf_mapping.bpf_insn> insn_p, Pointer<Byte> p, int var1, int var2);

    @NativeFunction("(u64:${bpf_insn}u64:u8u32u32u64:${bpf_aux_data})u32")
    int bpf_filter_with_aux_data(Pointer<bpf_mapping.bpf_insn> insn_p, Pointer<Byte> p, int var1, int var2, Pointer<bpf_mapping.bpf_aux_data> aux_data_p);

    @NativeStruct("[u32(bf_len)x32u64(bf_insns):${bpf_insn}](bpf_program)")
    public interface bpf_program extends Struct<bpf_program> {

        @NativeGetter("bf_len")
        int bf_len$get();

        @NativeGetter("bf_insns")
        Pointer<bpf_mapping.bpf_insn> bf_insns$get();

    }

    @NativeStruct("[u16(code)u8(jt)u8(jf)u32(k)](bpf_insn)")
    public interface bpf_insn extends Struct<bpf_mapping.bpf_insn> {

        @NativeGetter("code")
        short code$get();

        @NativeGetter("jt")
        byte jt$get();

        @NativeGetter("jf")
        byte jf$get();

        @NativeGetter("k")
        int k$get();

    }

    @NativeStruct("[u16(vlan_tag_present)u16(vlan_tag)](bpf_aux_data)")
    public interface bpf_aux_data extends Struct<bpf_mapping.bpf_aux_data> {

        @NativeGetter("vlan_tag_present")
        short vlan_tag_present$get();

        @NativeGetter("vlan_tag")
        short vlan_tag$get();

    }

}
