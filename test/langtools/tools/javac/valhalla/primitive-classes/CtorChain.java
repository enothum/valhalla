/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8198749
 * @summary Test value instatiation using new/ctors by chaining constructors.
 * @compile -XDenablePrimitiveClasses CtorChain.java
 * @run main/othervm -XX:+EnableValhalla -XX:+EnablePrimitiveClasses CtorChain
 */

public primitive class CtorChain {
    int x1, x2, x3, x4, x5;
    CtorChain() {
        this(10);
    }
    CtorChain(int a) {
        this(a, 20);
    }
    CtorChain(int a, int b) {
        this(a, b, 30);
    }
    CtorChain(int a, int b, int c) {
        this(a, b, c, 40);
    }
    CtorChain(int a, int b, int c, int d) {
        this(a, b, c, d, 50);
    }
    CtorChain(int a, int b, int c, int d, int e) {
        this.x1 = a;
        this.x2 = b;
        this.x3 = c;
        this.x4 = d;
        this.x5 = e;
        return;
    }

    public static void main(String [] args) {
        CtorChain cc = new CtorChain();
        if (cc.x1 != 10 || cc.x2 != 20 || cc.x3 != 30 || cc.x4 != 40 || cc.x5 != 50)
            throw new AssertionError("Broken");
    }
}
