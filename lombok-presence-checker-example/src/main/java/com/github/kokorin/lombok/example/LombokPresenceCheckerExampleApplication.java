package com.github.kokorin.lombok.example;

import com.github.kokorin.lombok.PresenceChecker;
import lombok.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication(scanBasePackageClasses = LombokPresenceCheckerExampleApplication.class)
@EnableWebMvc
public class LombokPresenceCheckerExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(LombokPresenceCheckerExampleApplication.class, args);
	}

	@RestController
	@RequestMapping("user")
	public static class UserRestController {
		private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
		private final AtomicReference<User> userRef = new AtomicReference<>(
				new User("Иван", "Фёдорович", "Крузенштерн", "Человек и пароход")
		);

		@GetMapping
		public UserDto getUser() {
			return userMapper.toDto(userRef.get());
		}

		@PostMapping
		public UserDto setUser(@RequestBody UserDto userDto) {
			userRef.set(userMapper.fromDto(userDto));
			return userDto;
		}

		@PutMapping
		public UserDto updateUser(@RequestBody UserUpdateDto userUpdateDto) {
			// MapStruct will update only those fields which were explicitly passed
			// in HTTP request body
			User updated = userRef.updateAndGet(user -> {
				User result = user.toBuilder().build();
				userMapper.updateUser(userUpdateDto, result);
				return result;
			});

			return userMapper.toDto(updated);
		}
	}

	@Getter
	@Setter
	@Builder(toBuilder = true)
	@NoArgsConstructor
	@AllArgsConstructor
	public static class User {
		private String name;
		private String patronymic;
		private String surname;
		private String nickname;
	}

	@Getter
	@Setter
	public static class UserDto {
		private String name;
		private String patronymic;
		private String surname;
		private String nickname;
	}

	@Getter
	@Setter
	@PresenceChecker
	public static class UserUpdateDto {
		private String name;
		private String patronymic;
		private String surname;
		private String nickname;
	}

	@Mapper
	public static interface UserMapper {
		UserDto toDto(User user);
		User fromDto(UserDto dto);
		void updateUser(UserUpdateDto dto, @MappingTarget User user);
	}
}
