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
        Assert.assertFalse(this.hasName());
        this.setName("any");
        Assert.assertTrue(this.hasName());
    }
}
