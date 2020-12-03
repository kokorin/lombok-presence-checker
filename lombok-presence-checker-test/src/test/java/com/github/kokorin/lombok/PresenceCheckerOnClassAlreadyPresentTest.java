package com.github.kokorin.lombok;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

@PresenceChecker
@Getter
@Setter
public class PresenceCheckerOnClassAlreadyPresentTest {
    private boolean hasName = false;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.hasName = true;
    }

    public boolean hasName() {
        return hasName;
    }

    @Test
    public void noErrorsDuringCompilation() {
        Assert.assertFalse(this.hasName());
        this.setName("any");
        Assert.assertTrue(this.hasName());
    }

}
