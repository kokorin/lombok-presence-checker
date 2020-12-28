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
