/*
 * Copyright (c) 2015, 2022, Oracle and/or its affiliates. All rights reserved.
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

// -- This file was mechanically generated: Do not edit! -- //

/*
 * @test
 * @compile -XDenablePrimitiveClasses Point.java Value.java VarHandleTestAccessValue.java
 * @run testng/othervm -XX:+EnableValhalla -XX:+EnablePrimitiveClasses -Diters=10    -Xint                   VarHandleTestAccessValue
 * @run testng/othervm -XX:+EnableValhalla -XX:+EnablePrimitiveClasses -Diters=20000 -XX:TieredStopAtLevel=1 VarHandleTestAccessValue
 * @run testng/othervm -XX:+EnableValhalla -XX:+EnablePrimitiveClasses -Diters=20000                         VarHandleTestAccessValue
 * @run testng/othervm -XX:+EnableValhalla -XX:+EnablePrimitiveClasses -Diters=20000 -XX:-TieredCompilation  VarHandleTestAccessValue
 */

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class VarHandleTestAccessValue extends VarHandleBaseTest {
    static final Class<?> type = Value.class;

    static final Value static_final_v = Value.getInstance(Point.getInstance(1,1));

    static Value static_v;

    final Value final_v = Value.getInstance(Point.getInstance(1,1));

    Value v;

    static final Value static_final_v2 = Value.getInstance(Point.getInstance(1,1));

    static Value static_v2;

    final Value final_v2 = Value.getInstance(Point.getInstance(1,1));

    Value v2;

    VarHandle vhFinalField;

    VarHandle vhField;

    VarHandle vhStaticField;

    VarHandle vhStaticFinalField;

    VarHandle vhArray;

    VarHandle vhArrayObject;

    VarHandle[] allocate(boolean same) {
        List<VarHandle> vhs = new ArrayList<>();

        String postfix = same ? "" : "2";
        VarHandle vh;
        try {
            vh = MethodHandles.lookup().findVarHandle(
                    VarHandleTestAccessValue.class, "final_v" + postfix, type);
            vhs.add(vh);

            vh = MethodHandles.lookup().findVarHandle(
                    VarHandleTestAccessValue.class, "v" + postfix, type);
            vhs.add(vh);

            vh = MethodHandles.lookup().findStaticVarHandle(
                VarHandleTestAccessValue.class, "static_final_v" + postfix, type);
            vhs.add(vh);

            vh = MethodHandles.lookup().findStaticVarHandle(
                VarHandleTestAccessValue.class, "static_v" + postfix, type);
            vhs.add(vh);

            if (same) {
                vh = MethodHandles.arrayElementVarHandle(Value[].class);
            }
            else {
                vh = MethodHandles.arrayElementVarHandle(String[].class);
            }
            vhs.add(vh);
        } catch (Exception e) {
            throw new InternalError(e);
        }
        return vhs.toArray(new VarHandle[0]);
    }

    @BeforeClass
    public void setup() throws Exception {
        vhFinalField = MethodHandles.lookup().findVarHandle(
                VarHandleTestAccessValue.class, "final_v", type);

        vhField = MethodHandles.lookup().findVarHandle(
                VarHandleTestAccessValue.class, "v", type);

        vhStaticFinalField = MethodHandles.lookup().findStaticVarHandle(
            VarHandleTestAccessValue.class, "static_final_v", type);

        vhStaticField = MethodHandles.lookup().findStaticVarHandle(
            VarHandleTestAccessValue.class, "static_v", type);

        vhArray = MethodHandles.arrayElementVarHandle(Value[].class);
        vhArrayObject = MethodHandles.arrayElementVarHandle(Object[].class);
    }


    @DataProvider
    public Object[][] varHandlesProvider() throws Exception {
        List<VarHandle> vhs = new ArrayList<>();
        vhs.add(vhField);
        vhs.add(vhStaticField);
        vhs.add(vhArray);

        return vhs.stream().map(tc -> new Object[]{tc}).toArray(Object[][]::new);
    }

    @Test
    public void testEquals() {
        VarHandle[] vhs1 = allocate(true);
        VarHandle[] vhs2 = allocate(true);

        for (int i = 0; i < vhs1.length; i++) {
            for (int j = 0; j < vhs1.length; j++) {
                if (i != j) {
                    assertNotEquals(vhs1[i], vhs1[j]);
                    assertNotEquals(vhs1[i], vhs2[j]);
                }
            }
        }

        VarHandle[] vhs3 = allocate(false);
        for (int i = 0; i < vhs1.length; i++) {
            assertNotEquals(vhs1[i], vhs3[i]);
        }
    }

    @Test(dataProvider = "varHandlesProvider")
    public void testIsAccessModeSupported(VarHandle vh) {
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.GET));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.SET));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.GET_VOLATILE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.SET_VOLATILE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.GET_ACQUIRE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.SET_RELEASE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.GET_OPAQUE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.SET_OPAQUE));

        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.COMPARE_AND_SET));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.COMPARE_AND_EXCHANGE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.COMPARE_AND_EXCHANGE_ACQUIRE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.COMPARE_AND_EXCHANGE_RELEASE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.WEAK_COMPARE_AND_SET_PLAIN));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.WEAK_COMPARE_AND_SET));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.WEAK_COMPARE_AND_SET_ACQUIRE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.WEAK_COMPARE_AND_SET_RELEASE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_SET));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_SET_ACQUIRE));
        assertTrue(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_SET_RELEASE));

        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_ADD));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_ADD_ACQUIRE));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_ADD_RELEASE));

        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_OR));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_OR_ACQUIRE));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_OR_RELEASE));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_AND));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_AND_ACQUIRE));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_AND_RELEASE));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_XOR));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_XOR_ACQUIRE));
        assertFalse(vh.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_XOR_RELEASE));
    }


    @DataProvider
    public Object[][] typesProvider() throws Exception {
        List<Object[]> types = new ArrayList<>();
        types.add(new Object[] {vhField, Arrays.asList(VarHandleTestAccessValue.class)});
        types.add(new Object[] {vhStaticField, Arrays.asList()});
        types.add(new Object[] {vhArray, Arrays.asList(Value[].class, int.class)});

        return types.stream().toArray(Object[][]::new);
    }

    @Test(dataProvider = "typesProvider")
    public void testTypes(VarHandle vh, List<Class<?>> pts) {
        assertEquals(vh.varType(), type);

        assertEquals(vh.coordinateTypes(), pts);

        testTypes(vh);
    }


    @Test
    public void testLookupInstanceToStatic() {
        checkIAE("Lookup of static final field to instance final field", () -> {
            MethodHandles.lookup().findStaticVarHandle(
                    VarHandleTestAccessValue.class, "final_v", type);
        });

        checkIAE("Lookup of static field to instance field", () -> {
            MethodHandles.lookup().findStaticVarHandle(
                    VarHandleTestAccessValue.class, "v", type);
        });
    }

    @Test
    public void testLookupStaticToInstance() {
        checkIAE("Lookup of instance final field to static final field", () -> {
            MethodHandles.lookup().findVarHandle(
                VarHandleTestAccessValue.class, "static_final_v", type);
        });

        checkIAE("Lookup of instance field to static field", () -> {
            vhStaticField = MethodHandles.lookup().findVarHandle(
                VarHandleTestAccessValue.class, "static_v", type);
        });
    }


    @DataProvider
    public Object[][] accessTestCaseProvider() throws Exception {
        List<AccessTestCase<?>> cases = new ArrayList<>();

        cases.add(new VarHandleAccessTestCase("Instance final field",
                                              vhFinalField, vh -> testInstanceFinalField(this, vh)));
        cases.add(new VarHandleAccessTestCase("Instance final field unsupported",
                                              vhFinalField, vh -> testInstanceFinalFieldUnsupported(this, vh),
                                              false));

        cases.add(new VarHandleAccessTestCase("Static final field",
                                              vhStaticFinalField, VarHandleTestAccessValue::testStaticFinalField));
        cases.add(new VarHandleAccessTestCase("Static final field unsupported",
                                              vhStaticFinalField, VarHandleTestAccessValue::testStaticFinalFieldUnsupported,
                                              false));

        cases.add(new VarHandleAccessTestCase("Instance field",
                                              vhField, vh -> testInstanceField(this, vh)));
        cases.add(new VarHandleAccessTestCase("Instance field unsupported",
                                              vhField, vh -> testInstanceFieldUnsupported(this, vh),
                                              false));

        cases.add(new VarHandleAccessTestCase("Static field",
                                              vhStaticField, VarHandleTestAccessValue::testStaticField));
        cases.add(new VarHandleAccessTestCase("Static field unsupported",
                                              vhStaticField, VarHandleTestAccessValue::testStaticFieldUnsupported,
                                              false));

        cases.add(new VarHandleAccessTestCase("Array",
                                              vhArray, VarHandleTestAccessValue::testArray));
        cases.add(new VarHandleAccessTestCase("Array Object[]",
                                              vhArrayObject, VarHandleTestAccessValue::testArray));
        cases.add(new VarHandleAccessTestCase("Array unsupported",
                                              vhArray, VarHandleTestAccessValue::testArrayUnsupported,
                                              false));
        cases.add(new VarHandleAccessTestCase("Array index out of bounds",
                                              vhArray, VarHandleTestAccessValue::testArrayIndexOutOfBounds,
                                              false));
        cases.add(new VarHandleAccessTestCase("Array store exception",
                                              vhArrayObject, VarHandleTestAccessValue::testArrayStoreException,
                                              false));
        // Work around issue with jtreg summary reporting which truncates
        // the String result of Object.toString to 30 characters, hence
        // the first dummy argument
        return cases.stream().map(tc -> new Object[]{tc.toString(), tc}).toArray(Object[][]::new);
    }

    @Test(dataProvider = "accessTestCaseProvider")
    public <T> void testAccess(String desc, AccessTestCase<T> atc) throws Throwable {
        T t = atc.get();
        int iters = atc.requiresLoop() ? ITERS : 1;
        for (int c = 0; c < iters; c++) {
            atc.testAccess(t);
        }
    }




    static void testInstanceFinalField(VarHandleTestAccessValue recv, VarHandle vh) {
        // Plain
        {
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "get Value value");
        }


        // Volatile
        {
            Value x = (Value) vh.getVolatile(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "getVolatile Value value");
        }

        // Lazy
        {
            Value x = (Value) vh.getAcquire(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "getRelease Value value");
        }

        // Opaque
        {
            Value x = (Value) vh.getOpaque(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "getOpaque Value value");
        }
    }

    static void testInstanceFinalFieldUnsupported(VarHandleTestAccessValue recv, VarHandle vh) {
        checkUOE(() -> {
            vh.set(recv, Value.getInstance(Point.getInstance(2,2)));
        });

        checkUOE(() -> {
            vh.setVolatile(recv, Value.getInstance(Point.getInstance(2,2)));
        });

        checkUOE(() -> {
            vh.setRelease(recv, Value.getInstance(Point.getInstance(2,2)));
        });

        checkUOE(() -> {
            vh.setOpaque(recv, Value.getInstance(Point.getInstance(2,2)));
        });


        checkUOE(() -> {
            Value o = (Value) vh.getAndAdd(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddAcquire(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddRelease(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOr(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrAcquire(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrRelease(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAnd(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndAcquire(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndRelease(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXor(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorAcquire(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorRelease(recv, Value.getInstance(Point.getInstance(1,1)));
        });
    }

    static void testStaticFinalField(VarHandle vh) {
        // Plain
        {
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "get Value value");
        }


        // Volatile
        {
            Value x = (Value) vh.getVolatile();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "getVolatile Value value");
        }

        // Lazy
        {
            Value x = (Value) vh.getAcquire();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "getRelease Value value");
        }

        // Opaque
        {
            Value x = (Value) vh.getOpaque();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "getOpaque Value value");
        }
    }

    static void testStaticFinalFieldUnsupported(VarHandle vh) {
        checkUOE(() -> {
            vh.set(Value.getInstance(Point.getInstance(2,2)));
        });

        checkUOE(() -> {
            vh.setVolatile(Value.getInstance(Point.getInstance(2,2)));
        });

        checkUOE(() -> {
            vh.setRelease(Value.getInstance(Point.getInstance(2,2)));
        });

        checkUOE(() -> {
            vh.setOpaque(Value.getInstance(Point.getInstance(2,2)));
        });


        checkUOE(() -> {
            Value o = (Value) vh.getAndAdd(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddAcquire(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddRelease(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOr(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrAcquire(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrRelease(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAnd(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndAcquire(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndRelease(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXor(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorAcquire(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorRelease(Value.getInstance(Point.getInstance(1,1)));
        });
    }


    static void testInstanceField(VarHandleTestAccessValue recv, VarHandle vh) {
        // Plain
        {
            vh.set(recv, Value.getInstance(Point.getInstance(1,1)));
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "set Value value");
        }


        // Volatile
        {
            vh.setVolatile(recv, Value.getInstance(Point.getInstance(2,2)));
            Value x = (Value) vh.getVolatile(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "setVolatile Value value");
        }

        // Lazy
        {
            vh.setRelease(recv, Value.getInstance(Point.getInstance(1,1)));
            Value x = (Value) vh.getAcquire(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "setRelease Value value");
        }

        // Opaque
        {
            vh.setOpaque(recv, Value.getInstance(Point.getInstance(2,2)));
            Value x = (Value) vh.getOpaque(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "setOpaque Value value");
        }

        vh.set(recv, Value.getInstance(Point.getInstance(1,1)));

        // Compare
        {
            boolean r = vh.compareAndSet(recv, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            assertEquals(r, true, "success compareAndSet Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success compareAndSet Value value");
        }

        {
            boolean r = vh.compareAndSet(recv, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(r, false, "failing compareAndSet Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing compareAndSet Value value");
        }

        {
            Value r = (Value) vh.compareAndExchange(recv, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
            assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchange Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchange Value value");
        }

        {
            Value r = (Value) vh.compareAndExchange(recv, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchange Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchange Value value");
        }

        {
            Value r = (Value) vh.compareAndExchangeAcquire(recv, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchangeAcquire Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchangeAcquire Value value");
        }

        {
            Value r = (Value) vh.compareAndExchangeAcquire(recv, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "failing compareAndExchangeAcquire Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing compareAndExchangeAcquire Value value");
        }

        {
            Value r = (Value) vh.compareAndExchangeRelease(recv, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
            assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchangeRelease Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchangeRelease Value value");
        }

        {
            Value r = (Value) vh.compareAndExchangeRelease(recv, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchangeRelease Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchangeRelease Value value");
        }

        {
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = vh.weakCompareAndSetPlain(recv, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetPlain Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success weakCompareAndSetPlain Value value");
        }

        {
            boolean success = vh.weakCompareAndSetPlain(recv, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(success, false, "failing weakCompareAndSetPlain Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing weakCompareAndSetPlain Value value");
        }

        {
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = vh.weakCompareAndSetAcquire(recv, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetAcquire Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success weakCompareAndSetAcquire Value");
        }

        {
            boolean success = vh.weakCompareAndSetAcquire(recv, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(success, false, "failing weakCompareAndSetAcquire Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing weakCompareAndSetAcquire Value value");
        }

        {
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = vh.weakCompareAndSetRelease(recv, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetRelease Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success weakCompareAndSetRelease Value");
        }

        {
            boolean success = vh.weakCompareAndSetRelease(recv, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(success, false, "failing weakCompareAndSetRelease Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing weakCompareAndSetRelease Value value");
        }

        {
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = vh.weakCompareAndSet(recv, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSet Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success weakCompareAndSet Value value");
        }

        {
            boolean success = vh.weakCompareAndSet(recv, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(success, false, "failing weakCompareAndSet Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing weakCompareAndSet Value value");
        }

        // Compare set and get
        {
            vh.set(recv, Value.getInstance(Point.getInstance(1,1)));

            Value o = (Value) vh.getAndSet(recv, Value.getInstance(Point.getInstance(2,2)));
            assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSet Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSet Value value");
        }

        {
            vh.set(recv, Value.getInstance(Point.getInstance(1,1)));

            Value o = (Value) vh.getAndSetAcquire(recv, Value.getInstance(Point.getInstance(2,2)));
            assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSetAcquire Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSetAcquire Value value");
        }

        {
            vh.set(recv, Value.getInstance(Point.getInstance(1,1)));

            Value o = (Value) vh.getAndSetRelease(recv, Value.getInstance(Point.getInstance(2,2)));
            assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSetRelease Value");
            Value x = (Value) vh.get(recv);
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSetRelease Value value");
        }


    }

    static void testInstanceFieldUnsupported(VarHandleTestAccessValue recv, VarHandle vh) {

        checkUOE(() -> {
            Value o = (Value) vh.getAndAdd(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddAcquire(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddRelease(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOr(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrAcquire(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrRelease(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAnd(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndAcquire(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndRelease(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXor(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorAcquire(recv, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorRelease(recv, Value.getInstance(Point.getInstance(1,1)));
        });
    }


    static void testStaticField(VarHandle vh) {
        // Plain
        {
            vh.set(Value.getInstance(Point.getInstance(1,1)));
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "set Value value");
        }


        // Volatile
        {
            vh.setVolatile(Value.getInstance(Point.getInstance(2,2)));
            Value x = (Value) vh.getVolatile();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "setVolatile Value value");
        }

        // Lazy
        {
            vh.setRelease(Value.getInstance(Point.getInstance(1,1)));
            Value x = (Value) vh.getAcquire();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "setRelease Value value");
        }

        // Opaque
        {
            vh.setOpaque(Value.getInstance(Point.getInstance(2,2)));
            Value x = (Value) vh.getOpaque();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "setOpaque Value value");
        }

        vh.set(Value.getInstance(Point.getInstance(1,1)));

        // Compare
        {
            boolean r = vh.compareAndSet(Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            assertEquals(r, true, "success compareAndSet Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success compareAndSet Value value");
        }

        {
            boolean r = vh.compareAndSet(Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(r, false, "failing compareAndSet Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing compareAndSet Value value");
        }

        {
            Value r = (Value) vh.compareAndExchange(Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
            assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchange Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchange Value value");
        }

        {
            Value r = (Value) vh.compareAndExchange(Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchange Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchange Value value");
        }

        {
            Value r = (Value) vh.compareAndExchangeAcquire(Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchangeAcquire Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchangeAcquire Value value");
        }

        {
            Value r = (Value) vh.compareAndExchangeAcquire(Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "failing compareAndExchangeAcquire Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing compareAndExchangeAcquire Value value");
        }

        {
            Value r = (Value) vh.compareAndExchangeRelease(Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
            assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchangeRelease Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchangeRelease Value value");
        }

        {
            Value r = (Value) vh.compareAndExchangeRelease(Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchangeRelease Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchangeRelease Value value");
        }

        {
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = vh.weakCompareAndSetPlain(Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetPlain Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success weakCompareAndSetPlain Value value");
        }

        {
            boolean success = vh.weakCompareAndSetPlain(Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(success, false, "failing weakCompareAndSetPlain Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing weakCompareAndSetPlain Value value");
        }

        {
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = vh.weakCompareAndSetAcquire(Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetAcquire Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success weakCompareAndSetAcquire Value");
        }

        {
            boolean success = vh.weakCompareAndSetAcquire(Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(success, false, "failing weakCompareAndSetAcquire Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing weakCompareAndSetAcquire Value value");
        }

        {
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = vh.weakCompareAndSetRelease(Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetRelease Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success weakCompareAndSetRelease Value");
        }

        {
            boolean success = vh.weakCompareAndSetRelease(Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(success, false, "failing weakCompareAndSetRelease Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing weakCompareAndSetRelease Value value");
        }

        {
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = vh.weakCompareAndSet(Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSet Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success weakCompareAndSet Value");
        }

        {
            boolean success = vh.weakCompareAndSet(Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
            assertEquals(success, false, "failing weakCompareAndSet Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing weakCompareAndSet Value value");
        }

        // Compare set and get
        {
            vh.set(Value.getInstance(Point.getInstance(1,1)));

            Value o = (Value) vh.getAndSet(Value.getInstance(Point.getInstance(2,2)));
            assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSet Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSet Value value");
        }

        {
            vh.set(Value.getInstance(Point.getInstance(1,1)));

            Value o = (Value) vh.getAndSetAcquire(Value.getInstance(Point.getInstance(2,2)));
            assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSetAcquire Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSetAcquire Value value");
        }

        {
            vh.set(Value.getInstance(Point.getInstance(1,1)));

            Value o = (Value) vh.getAndSetRelease(Value.getInstance(Point.getInstance(2,2)));
            assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSetRelease Value");
            Value x = (Value) vh.get();
            assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSetRelease Value value");
        }


    }

    static void testStaticFieldUnsupported(VarHandle vh) {

        checkUOE(() -> {
            Value o = (Value) vh.getAndAdd(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddAcquire(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddRelease(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOr(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrAcquire(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrRelease(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAnd(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndAcquire(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndRelease(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXor(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorAcquire(Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorRelease(Value.getInstance(Point.getInstance(1,1)));
        });
    }


    static void testArray(VarHandle vh) {
        Value[] array = new Value[10];

        for (int i = 0; i < array.length; i++) {
            // Plain
            {
                vh.set(array, i, Value.getInstance(Point.getInstance(1,1)));
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "get Value value");
            }


            // Volatile
            {
                vh.setVolatile(array, i, Value.getInstance(Point.getInstance(2,2)));
                Value x = (Value) vh.getVolatile(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "setVolatile Value value");
            }

            // Lazy
            {
                vh.setRelease(array, i, Value.getInstance(Point.getInstance(1,1)));
                Value x = (Value) vh.getAcquire(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "setRelease Value value");
            }

            // Opaque
            {
                vh.setOpaque(array, i, Value.getInstance(Point.getInstance(2,2)));
                Value x = (Value) vh.getOpaque(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "setOpaque Value value");
            }

            vh.set(array, i, Value.getInstance(Point.getInstance(1,1)));

            // Compare
            {
                boolean r = vh.compareAndSet(array, i, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
                assertEquals(r, true, "success compareAndSet Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success compareAndSet Value value");
            }

            {
                boolean r = vh.compareAndSet(array, i, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
                assertEquals(r, false, "failing compareAndSet Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing compareAndSet Value value");
            }

            {
                Value r = (Value) vh.compareAndExchange(array, i, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
                assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchange Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchange Value value");
            }

            {
                Value r = (Value) vh.compareAndExchange(array, i, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
                assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchange Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchange Value value");
            }

            {
                Value r = (Value) vh.compareAndExchangeAcquire(array, i, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
                assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchangeAcquire Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchangeAcquire Value value");
            }

            {
                Value r = (Value) vh.compareAndExchangeAcquire(array, i, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
                assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "failing compareAndExchangeAcquire Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing compareAndExchangeAcquire Value value");
            }

            {
                Value r = (Value) vh.compareAndExchangeRelease(array, i, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
                assertEquals(r, Value.getInstance(Point.getInstance(2,2)), "success compareAndExchangeRelease Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success compareAndExchangeRelease Value value");
            }

            {
                Value r = (Value) vh.compareAndExchangeRelease(array, i, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
                assertEquals(r, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchangeRelease Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing compareAndExchangeRelease Value value");
            }

            {
                boolean success = false;
                for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                    success = vh.weakCompareAndSetPlain(array, i, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
                    if (!success) weakDelay();
                }
                assertEquals(success, true, "success weakCompareAndSetPlain Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success weakCompareAndSetPlain Value value");
            }

            {
                boolean success = vh.weakCompareAndSetPlain(array, i, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
                assertEquals(success, false, "failing weakCompareAndSetPlain Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing weakCompareAndSetPlain Value value");
            }

            {
                boolean success = false;
                for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                    success = vh.weakCompareAndSetAcquire(array, i, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
                    if (!success) weakDelay();
                }
                assertEquals(success, true, "success weakCompareAndSetAcquire Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success weakCompareAndSetAcquire Value");
            }

            {
                boolean success = vh.weakCompareAndSetAcquire(array, i, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
                assertEquals(success, false, "failing weakCompareAndSetAcquire Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing weakCompareAndSetAcquire Value value");
            }

            {
                boolean success = false;
                for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                    success = vh.weakCompareAndSetRelease(array, i, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
                    if (!success) weakDelay();
                }
                assertEquals(success, true, "success weakCompareAndSetRelease Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "success weakCompareAndSetRelease Value");
            }

            {
                boolean success = vh.weakCompareAndSetRelease(array, i, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(3,3)));
                assertEquals(success, false, "failing weakCompareAndSetRelease Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "failing weakCompareAndSetRelease Value value");
            }

            {
                boolean success = false;
                for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                    success = vh.weakCompareAndSet(array, i, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
                    if (!success) weakDelay();
                }
                assertEquals(success, true, "success weakCompareAndSet Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "success weakCompareAndSet Value");
            }

            {
                boolean success = vh.weakCompareAndSet(array, i, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(3,3)));
                assertEquals(success, false, "failing weakCompareAndSet Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(1,1)), "failing weakCompareAndSet Value value");
            }

            // Compare set and get
            {
                vh.set(array, i, Value.getInstance(Point.getInstance(1,1)));

                Value o = (Value) vh.getAndSet(array, i, Value.getInstance(Point.getInstance(2,2)));
                assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSet Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSet Value value");
            }

            {
                vh.set(array, i, Value.getInstance(Point.getInstance(1,1)));

                Value o = (Value) vh.getAndSetAcquire(array, i, Value.getInstance(Point.getInstance(2,2)));
                assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSetAcquire Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSetAcquire Value value");
            }

            {
                vh.set(array, i, Value.getInstance(Point.getInstance(1,1)));

                Value o = (Value) vh.getAndSetRelease(array, i, Value.getInstance(Point.getInstance(2,2)));
                assertEquals(o, Value.getInstance(Point.getInstance(1,1)), "getAndSetRelease Value");
                Value x = (Value) vh.get(array, i);
                assertEquals(x, Value.getInstance(Point.getInstance(2,2)), "getAndSetRelease Value value");
            }


        }
    }

    static void testArrayUnsupported(VarHandle vh) {
        Value[] array = new Value[10];

        int i = 0;

        checkUOE(() -> {
            Value o = (Value) vh.getAndAdd(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddAcquire(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndAddRelease(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOr(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrAcquire(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseOrRelease(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAnd(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndAcquire(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseAndRelease(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXor(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorAcquire(array, i, Value.getInstance(Point.getInstance(1,1)));
        });

        checkUOE(() -> {
            Value o = (Value) vh.getAndBitwiseXorRelease(array, i, Value.getInstance(Point.getInstance(1,1)));
        });
    }

    static void testArrayIndexOutOfBounds(VarHandle vh) throws Throwable {
        Value[] array = new Value[10];

        for (int i : new int[]{-1, Integer.MIN_VALUE, 10, 11, Integer.MAX_VALUE}) {
            final int ci = i;

            checkAIOOBE(() -> {
                Value x = (Value) vh.get(array, ci);
            });

            checkAIOOBE(() -> {
                vh.set(array, ci, Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                Value x = (Value) vh.getVolatile(array, ci);
            });

            checkAIOOBE(() -> {
                vh.setVolatile(array, ci, Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                Value x = (Value) vh.getAcquire(array, ci);
            });

            checkAIOOBE(() -> {
                vh.setRelease(array, ci, Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                Value x = (Value) vh.getOpaque(array, ci);
            });

            checkAIOOBE(() -> {
                vh.setOpaque(array, ci, Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                boolean r = vh.compareAndSet(array, ci, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            });

            checkAIOOBE(() -> {
                Value r = (Value) vh.compareAndExchange(array, ci, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                Value r = (Value) vh.compareAndExchangeAcquire(array, ci, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                Value r = (Value) vh.compareAndExchangeRelease(array, ci, Value.getInstance(Point.getInstance(2,2)), Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                boolean r = vh.weakCompareAndSetPlain(array, ci, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            });

            checkAIOOBE(() -> {
                boolean r = vh.weakCompareAndSet(array, ci, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            });

            checkAIOOBE(() -> {
                boolean r = vh.weakCompareAndSetAcquire(array, ci, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            });

            checkAIOOBE(() -> {
                boolean r = vh.weakCompareAndSetRelease(array, ci, Value.getInstance(Point.getInstance(1,1)), Value.getInstance(Point.getInstance(2,2)));
            });

            checkAIOOBE(() -> {
                Value o = (Value) vh.getAndSet(array, ci, Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                Value o = (Value) vh.getAndSetAcquire(array, ci, Value.getInstance(Point.getInstance(1,1)));
            });

            checkAIOOBE(() -> {
                Value o = (Value) vh.getAndSetRelease(array, ci, Value.getInstance(Point.getInstance(1,1)));
            });


        }
    }

    static void testArrayStoreException(VarHandle vh) throws Throwable {
        Object[] array = new Value[10];
        Arrays.fill(array, Value.getInstance(Point.getInstance(1,1)));
        Object value = new Object();

        // Set
        checkASE(() -> {
            vh.set(array, 0, value);
        });

        // SetVolatile
        checkASE(() -> {
            vh.setVolatile(array, 0, value);
        });

        // SetOpaque
        checkASE(() -> {
            vh.setOpaque(array, 0, value);
        });

        // SetRelease
        checkASE(() -> {
            vh.setRelease(array, 0, value);
        });

        // CompareAndSet
        checkASE(() -> { // receiver reference class
            boolean r = vh.compareAndSet(array, 0, Value.getInstance(Point.getInstance(1,1)), value);
        });

        // WeakCompareAndSet
        checkASE(() -> { // receiver reference class
            boolean r = vh.weakCompareAndSetPlain(array, 0, Value.getInstance(Point.getInstance(1,1)), value);
        });

        // WeakCompareAndSetVolatile
        checkASE(() -> { // receiver reference class
            boolean r = vh.weakCompareAndSet(array, 0, Value.getInstance(Point.getInstance(1,1)), value);
        });

        // WeakCompareAndSetAcquire
        checkASE(() -> { // receiver reference class
            boolean r = vh.weakCompareAndSetAcquire(array, 0, Value.getInstance(Point.getInstance(1,1)), value);
        });

        // WeakCompareAndSetRelease
        checkASE(() -> { // receiver reference class
            boolean r = vh.weakCompareAndSetRelease(array, 0, Value.getInstance(Point.getInstance(1,1)), value);
        });

        // CompareAndExchange
        checkASE(() -> { // receiver reference class
            Value x = (Value) vh.compareAndExchange(array, 0, Value.getInstance(Point.getInstance(1,1)), value);
        });

        // CompareAndExchangeAcquire
        checkASE(() -> { // receiver reference class
            Value x = (Value) vh.compareAndExchangeAcquire(array, 0, Value.getInstance(Point.getInstance(1,1)), value);
        });

        // CompareAndExchangeRelease
        checkASE(() -> { // receiver reference class
            Value x = (Value) vh.compareAndExchangeRelease(array, 0, Value.getInstance(Point.getInstance(1,1)), value);
        });

        // GetAndSet
        checkASE(() -> { // receiver reference class
            Value x = (Value) vh.getAndSet(array, 0, value);
        });

        // GetAndSetAcquire
        checkASE(() -> { // receiver reference class
            Value x = (Value) vh.getAndSetAcquire(array, 0, value);
        });

        // GetAndSetRelease
        checkASE(() -> { // receiver reference class
            Value x = (Value) vh.getAndSetRelease(array, 0, value);
        });
    }
}

