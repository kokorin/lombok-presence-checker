package com.github.kokorin.lombok;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

public class PresenceCheckerOnFieldAlreadyPresentTest {
    private boolean hasName = false;
    @PresenceChecker
    @Getter
    @Setter
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.hasName = true;
    }

    private boolean hasName() {
        return hasName;
    }

    @Test
    public void noErrorsDuringCompilation() {
        PresenceCheckerOnFieldAlreadyPresentTest pc = new PresenceCheckerOnFieldAlreadyPresentTest();
        Assert.assertFalse(pc.hasName());
        pc.setName("any");
        Assert.assertTrue(pc.hasName());
    }

}
