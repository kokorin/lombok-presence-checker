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
import lombok.Setter;
import lombok.Synchronized;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@PresenceChecker
@Getter
@Setter
public class PresenceCheckerIsNotGeneratedForGeneratedFieldsTest {
    // Extra PresenceChecker on field should not affect generated code
    @PresenceChecker
    private String name;
    private String surname;
    private String patronymic;

    @Test
    public void onlyGeneratedForRequiredFields() throws Exception {
        Set<String> presenceCheckerFields = new HashSet<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            String name = field.getName();
            if (name.startsWith("has")) {
                presenceCheckerFields.add(name);
            }
        }

        Assert.assertEquals(new HashSet<>(Arrays.asList("hasName", "hasSurname", "hasPatronymic")), presenceCheckerFields);
    }

    @Test(expected = NoSuchMethodException.class)
    public void noPresenceCheckIsAddedForLockField() throws Exception {
        Assert.assertFalse(this.hasName());
        this.getClass().getMethod("has$lock");
    }

    @Test
    @Synchronized
    public void synchronizedAnnotationGeneratesLockField() throws Exception {
        Field lockField = this.getClass().getDeclaredField("$lock");
        Assert.assertNotNull(lockField);
    }
}
