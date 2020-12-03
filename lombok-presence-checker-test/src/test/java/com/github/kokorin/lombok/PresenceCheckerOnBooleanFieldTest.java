package com.github.kokorin.lombok;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

public class PresenceCheckerOnBooleanFieldTest {
    @PresenceChecker
    @Getter
    @Setter
    private Boolean bWrapper;

    @PresenceChecker
    @Getter
    @Setter
    private boolean bPrimitive;

    @Test
    public void testWrapper() {
        Assert.assertFalse(this.hasBWrapper());
        this.setBWrapper(true);
        Assert.assertTrue(this.hasBWrapper());
    }

    @Test
    public void testPrimitive() {
        Assert.assertFalse(this.hasBPrimitive());
        this.setBPrimitive(true);
        Assert.assertTrue(this.hasBPrimitive());
    }
}
