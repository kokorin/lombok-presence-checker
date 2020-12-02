package com.github.kokorin.lombok;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

public class PresenceCheckerOnFieldWithSetterTest {
    @PresenceChecker
    @Setter
    @Getter
    private String name;

    @Test
    public void test() {
        PresenceCheckerOnFieldWithSetterTest pc = new PresenceCheckerOnFieldWithSetterTest();
        Assert.assertFalse(pc.hasName());
        pc.setName("any");
        Assert.assertTrue(pc.hasName());
    }
}
