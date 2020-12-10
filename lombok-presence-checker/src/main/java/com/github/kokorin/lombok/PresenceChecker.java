package com.github.kokorin.lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Put on any field to make lombok build a presence checker.
 * <p>
 * Example:
 * <pre>
 *     private &#64;PresenceChecker @Setter String foo;
 * </pre>
 *
 * will generate:
 *
 * <pre>
 *     private boolean hasFoo;
 *
 *     public booleans hasFoo() {
 *         return hasFoo;
 *     }
 *
 *     public void setFoo(String foo) {
 *         this.foo = foo;
 *         this.hasFoo = true;
 *     }
 * </pre>
 *
 * <p>
 * This annotation can also be applied to a class, in which case it'll be as if all non-static fields that don't already have
 * a {@code PresenceChecker} annotation have the annotation.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface PresenceChecker {
}