/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * @test
 * @bug 8217872 8221545
 * @summary [lworld] Javac forbids look ups on the light weight box type of a value type
 * @compile -XDenablePrimitiveClasses LookupOnLoxTest2.java
 * @run main/othervm -Xverify:none -XX:+EnableValhalla -XX:+EnablePrimitiveClasses LookupOnLoxTest2
 */

public primitive class LookupOnLoxTest2 {

    int ARRAY[] = { 10, 20, 30 };

    static primitive class Tuple {
        private final int index;
        private final int element;

        private Tuple(int index, int element) {
            this.index = index;
            this.element = element;
        }
    }

    static primitive class Cursor {
        private final int[] array;
        private final int index;

        private Cursor(int[] array, int index) {
            this.array = array;
            this.index = index;
        }

        Tuple current() {
            return new Tuple(index, array[index]);
        }

        Cursor.ref next() {
            if (index + 1 == array.length) {
                return null;
            }
            return new Cursor(array, index + 1);
        }
    }

    private static Cursor.ref indexedElements(int[] array) {
        if (array.length == 0) {
            return null;
        }
        return new Cursor(array, 0);
    }

    public int sum() {
        int sum = 0;
        for (Cursor.ref cursor = indexedElements(ARRAY); cursor != null; cursor = cursor.next()) {
            Tuple tuple = cursor.current();
            sum += tuple.index + tuple.element;
        }
        return sum;
    }

    public static void main(String [] args) {
        LookupOnLoxTest2 x = new LookupOnLoxTest2();
        if (x.sum() != 63 || x.ARRAY.length != 3) {
            throw new AssertionError("Broken");
        }
        LookupOnLoxTest2.ref xbox = x;
        if (xbox.sum() != 63 || xbox.ARRAY.length != 3) {
            throw new AssertionError("Broken");
        }
    }
}
