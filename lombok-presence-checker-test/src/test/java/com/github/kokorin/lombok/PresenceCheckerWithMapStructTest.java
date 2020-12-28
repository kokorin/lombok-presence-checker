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
import org.junit.Assert;
import org.junit.Test;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

public class PresenceCheckerWithMapStructTest {

    @Test
    public void test() {
        User user = new User();
        user.setName("to_be_updated");
        user.setValue(42);

        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        UserDto dto = new UserDto();

        Assert.assertFalse(dto.hasName());
        Assert.assertFalse(dto.hasValue());

        mapper.updateUser(dto, user);

        Assert.assertEquals("to_be_updated", user.getName());
        Assert.assertEquals(42, user.getValue());

        dto.setName("updated_name");
        dto.setValue(146);
        Assert.assertTrue(dto.hasName());
        Assert.assertTrue(dto.hasValue());

        mapper.updateUser(dto, user);

        Assert.assertEquals("updated_name", user.getName());
        Assert.assertEquals(146, user.getValue());
    }

    @Mapper
    public interface UserMapper {
        void updateUser(UserDto dto, @MappingTarget User user);
    }

    @Getter
    @Setter
    public static class User {
        private String name;
        private long value;
    }

    @PresenceChecker
    @Getter
    @Setter
    public static class UserDto {
        private String name;
        private long value;
    }
}
