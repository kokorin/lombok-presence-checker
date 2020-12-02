package com.github.kokorin.lombok;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

@PresenceChecker
@Setter
@Getter
public class PresenceCheckerOnClassWithSetterTest {
    private String name;

    @Test
    public void test() {
        PresenceCheckerOnClassWithSetterTest pc = new PresenceCheckerOnClassWithSetterTest();
        Assert.assertFalse(pc.hasName());
        pc.setName("any");
        Assert.assertTrue(pc.hasName());
    }
}
