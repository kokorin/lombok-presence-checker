package com.github.kokorin.lombok;

import org.junit.Test;

public class PresenceCheckerNotGeneratedForStaticFieldTest {
    @PresenceChecker
    private static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        PresenceCheckerNotGeneratedForStaticFieldTest.name = name;
    }

    @Test(expected = NoSuchMethodException.class)
    public void test() throws Exception {
        this.getClass().getMethod("hasName");
    }
}
