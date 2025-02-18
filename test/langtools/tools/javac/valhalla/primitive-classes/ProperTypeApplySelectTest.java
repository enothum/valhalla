/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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

/*
 * @test
 * @bug 8244559
 * @summary Check that javac transforms Types to AST nodes properly.
 * @compile -XDenablePrimitiveClasses ProperTypeApplySelectTest.java
 * @run main/othervm -XX:+EnableValhalla -XX:+EnablePrimitiveClasses ProperTypeApplySelectTest
 */

import java.util.List;

public class ProperTypeApplySelectTest {

  static String out = "";

  primitive static class Foo<V> {
    int x;
    Foo(int x) { this.x = x; }
  }

  static void m(Foo foo) {
    out += "inline";
  }
  static void m(Foo.ref foo) {
    out += "ref";
  }

  public static void main(String[] args) {
    List<Foo.ref<Integer>> list = List.of(new Foo<Integer>(3));
    list.stream().forEach(e -> m(e));
    if (!out.equals("ref"))
        throw new AssertionError("Unexpected: " + out);
  }
}
