package com.github.kokorin.lombok;

import org.junit.Test;

public class PresenceCheckerOnStaticFieldHasNoEffectTest {
    @PresenceChecker
    private static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        PresenceCheckerOnStaticFieldHasNoEffectTest.name = name;
    }

    @Test(expected = NoSuchMethodException.class)
    public void test() throws Exception {
        this.getClass().getMethod("hasName");
    }
}
