package com.github.kokorin.lombok;

import org.junit.Assert;
import org.junit.Test;

public class PresenceCheckerOnFieldTest {
    @PresenceChecker
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Test
    public void test() {
        PresenceCheckerOnFieldTest pc = new PresenceCheckerOnFieldTest();
        Assert.assertFalse(pc.hasName());
        pc.setName("any");
        Assert.assertTrue(pc.hasName());
    }
}
