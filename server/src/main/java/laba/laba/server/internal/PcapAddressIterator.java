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

import laba.laba.server.PcapAddress;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PcapAddressIterator implements Iterator<PcapAddress> {

    private PcapAddress next;

    public PcapAddressIterator(final PcapAddress pcapAddress) {
        this.next = pcapAddress;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public PcapAddress next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        PcapAddress previous = next;
        next = next.next();
        return previous;
    }

}
