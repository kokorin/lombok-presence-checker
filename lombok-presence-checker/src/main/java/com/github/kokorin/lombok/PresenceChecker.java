/*
 *    Copyright  2020 Denis Kokorin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

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
