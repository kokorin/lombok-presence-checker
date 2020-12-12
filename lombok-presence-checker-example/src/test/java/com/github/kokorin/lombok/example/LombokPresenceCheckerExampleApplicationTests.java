package com.github.kokorin.lombok.example;

import com.github.kokorin.lombok.example.LombokPresenceCheckerExampleApplication.UserDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LombokPresenceCheckerExampleApplicationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void partialUpdateWithPresenceChecker() {
        String url = "http://localhost:" + port + "/user";

        UserDto userDto = restTemplate.getForObject(url, UserDto.class);

        Assert.assertEquals("Иван", userDto.getName());
        Assert.assertEquals("Фёдорович", userDto.getPatronymic());
        Assert.assertEquals("Крузенштерн", userDto.getSurname());
        Assert.assertEquals("Человек и пароход", userDto.getNickname());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<UserDto> updateResponse = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>("{\"name\":\"Jon\"}", httpHeaders),
                UserDto.class
        );

        Assert.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        userDto = updateResponse.getBody();

        Assert.assertEquals("Jon", userDto.getName());
        Assert.assertEquals("Фёдорович", userDto.getPatronymic());
        Assert.assertEquals("Крузенштерн", userDto.getSurname());
        Assert.assertEquals("Человек и пароход", userDto.getNickname());

        updateResponse = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>("{" +
                        "\"patronymic\":null," +
                        "\"surname\":\"Snow\"," +
                        "\"nickname\":\"you know nothing\"" +
                        "}", httpHeaders),
                UserDto.class
        );

        Assert.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        userDto = updateResponse.getBody();

        Assert.assertEquals("Jon", userDto.getName());
        Assert.assertNull(userDto.getPatronymic());
        Assert.assertEquals("Snow", userDto.getSurname());
        Assert.assertEquals("you know nothing", userDto.getNickname());
    }

}
