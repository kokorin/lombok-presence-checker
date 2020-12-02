package com.github.kokorin.lombok;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

@PresenceChecker
@Getter
@Setter
public class PresenceCheckerAlreadyPresentTest {
    private boolean hasName = false;
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
        PresenceCheckerAlreadyPresentTest pc = new PresenceCheckerAlreadyPresentTest();
        Assert.assertFalse(pc.hasName());
        pc.setName("any");
        Assert.assertTrue(pc.hasName());
    }

}
