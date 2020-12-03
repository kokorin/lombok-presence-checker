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
        Assert.assertFalse(this.hasName());
        this.setName("any");
        Assert.assertTrue(this.hasName());
    }
}
