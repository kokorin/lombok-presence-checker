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
