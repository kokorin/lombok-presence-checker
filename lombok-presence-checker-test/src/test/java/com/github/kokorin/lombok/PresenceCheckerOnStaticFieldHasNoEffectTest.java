package com.github.kokorin.lombok;

import org.junit.Assert;
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

    @Test
    public void test() {
        Assert.fail("TO BE IMPLEMENTED");
    }
}
