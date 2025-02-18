/*
 * Copyright (c) 2019, 2022, Oracle and/or its affiliates. All rights reserved.
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

package compiler.valhalla.inlinetypes;

import jdk.test.lib.Asserts;
import compiler.lib.ir_framework.*;

/*
 * @test
 * @key randomness
 * @summary Verify that C1 performs escape analysis before optimizing withfield bytecode to putfield.
 * @library /test/lib /
 * @requires (os.simpleArch == "x64" | os.simpleArch == "aarch64")
 * @build org.openjdk.asmtools.* org.openjdk.asmtools.jasm.*
 * @run driver org.openjdk.asmtools.JtregDriver jasm -strict TestWithfieldC1Classes.jasm
 * @compile -XDenablePrimitiveClasses TestWithfieldC1.java
 * @run main/othervm/timeout=300 -XX:+EnableValhalla -XX:+EnablePrimitiveClasses compiler.valhalla.inlinetypes.TestWithfieldC1
 */

@ForceCompileClassInitializer
public class TestWithfieldC1 {

    public static void main(String[] args) {
        final Scenario[] scenarios = {
                new Scenario(0, // C1 only
                        "-XX:+EnableValhalla", "-XX:+EnablePrimitiveClasses",
                        "-XX:TieredStopAtLevel=1",
                        "-XX:+TieredCompilation"),
                new Scenario(1, // C2 only. (Make sure the tests are correctly written)
                        "-XX:+EnableValhalla", "-XX:+EnablePrimitiveClasses",
                        "-XX:TieredStopAtLevel=4",
                        "-XX:-TieredCompilation"),
                new Scenario(2, // interpreter only
                        "-XX:+EnableValhalla", "-XX:+EnablePrimitiveClasses",
                        "-Xint"),
                new Scenario(3, // Xcomp Only C1.
                        "-XX:+EnableValhalla", "-XX:+EnablePrimitiveClasses",
                        "-XX:TieredStopAtLevel=1",
                        "-XX:+TieredCompilation",
                        "-Xcomp"),
                new Scenario(4, // Xcomp Only C2.
                        "-XX:+EnableValhalla", "-XX:+EnablePrimitiveClasses",
                        "-XX:TieredStopAtLevel=4",
                        "-XX:-TieredCompilation",
                        "-Xcomp")
        };

        InlineTypes.getFramework()
                   .addScenarios(scenarios)
                   .start();
    }

    static FooValue.ref foo_static;
    static FooValue.ref foo_static_arr[] = new FooValue.ref[1];
    FooValue.ref foo_instance;

    @DontInline
    static void set_foo_static_if_null(FooValue v) {
        if (foo_static == null) {
            foo_static = v;
        }
    }

    static void validate_foo_static_and(FooValue v) {
        Asserts.assertEQ(foo_static.x, 1);
        Asserts.assertEQ(foo_static.y, 1);
        Asserts.assertEQ(v.x, 2);
        Asserts.assertEQ(v.y, 2);
    }

    // escape with putstatic
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test1() {
        return FooValue.test1();
    }

    @Run(test = "test1")
    public void test1_verifier() {
        FooValue v = test1();
        validate_foo_static_and(v);
    }

    // escape with putfield
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test2() {
        return FooValue.test2(this);
    }

    @Run(test = "test2")
    public void test2_verifier() {
        foo_instance = null;
        FooValue v = test2();
        Asserts.assertEQ(foo_instance.x, 1);
        Asserts.assertEQ(foo_instance.y, 1);
        Asserts.assertEQ(v.x, 2);
        Asserts.assertEQ(v.y, 2);
    }

    // escape with function call
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test3() {
        return FooValue.test3();
    }

    @Run(test = "test3")
    public void test3_verifier() {
        foo_static = null;
        FooValue v = test3();
        validate_foo_static_and(v);
    }

    // escape and then branch backwards
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test4() {
        return FooValue.test4();
    }

    @Run(test = "test4")
    public void test4_verifier() {
        foo_static = null;
        FooValue v = test4();
        validate_foo_static_and(v);
    }

    // escape using a different local variable
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test5() {
        return FooValue.test5();
    }

    @Run(test = "test5")
    public void test5_verifier() {
        foo_static = null;
        FooValue v = test5();
        validate_foo_static_and(v);
    }

    // escape using aastore
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test6() {
        return FooValue.test6();
    }

    @Run(test = "test6")
    public void test6_verifier() {
        foo_static_arr[0] = null;
        FooValue v = test6();
        Asserts.assertEQ(foo_static_arr[0].x, 1);
        Asserts.assertEQ(foo_static_arr[0].y, 1);
        Asserts.assertEQ(v.x, 2);
        Asserts.assertEQ(v.y, 2);
    }

    // Copying a value into different local slots -- disable withfield optimization
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test7() {
        return FooValue.test7();
    }

    @Run(test = "test7")
    public void test7_verifier() {
        FooValue v = test7();
        Asserts.assertEQ(v.x, 1);
        Asserts.assertEQ(v.y, 1);
    }

    // escape by invoking non-static method
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test8() {
        return FooValue.test8();
    }

    @Run(test = "test8")
    public void test8_verifier() {
        foo_static = null;
        FooValue v = test8();
        validate_foo_static_and(v);
    }

    // duplicate reference with local variables
    @Test(compLevel = CompLevel.C1_SIMPLE)
    public FooValue test9() {
        return FooValue.test9(this);
    }

    @Run(test = "test9")
    public void test9_verifier() {
        foo_instance = null;
        FooValue v = test9();
        Asserts.assertEQ(foo_instance.x, 3);
        Asserts.assertEQ(foo_instance.y, 3);
        Asserts.assertEQ(v.x, 2);
        Asserts.assertEQ(v.y, 2);
    }
}
