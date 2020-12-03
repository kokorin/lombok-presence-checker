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

        UserDto dto = new UserDto();
        dto.setName("updated_name");
        Assert.assertTrue(dto.hasName());
        Assert.assertFalse(dto.hasValue());

        Mappers.getMapper(UserMapper.class).updateUser(dto, user);

        Assert.assertEquals("updated_name", user.getName());
        Assert.assertEquals(42, user.getValue());
    }

    @Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
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
