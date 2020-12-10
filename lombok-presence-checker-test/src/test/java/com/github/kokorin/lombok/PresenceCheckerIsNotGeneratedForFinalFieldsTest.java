package com.github.kokorin.lombok;

import lombok.Getter;
import org.junit.Test;

@PresenceChecker
@Getter
public class PresenceCheckerIsNotGeneratedForFinalFieldsTest {
    private final String name = "name";

    @Test(expected = NoSuchMethodException.class)
    public void notGenerated() throws Exception {
        this.getClass().getMethod("hasName");
    }
}
