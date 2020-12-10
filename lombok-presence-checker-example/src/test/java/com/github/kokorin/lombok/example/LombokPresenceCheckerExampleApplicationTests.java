package com.github.kokorin.lombok.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest
class LombokPresenceCheckerExampleApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		String url = "http://localhost:" + port + "/api/openapi?group=v1";
		String content = restTemplate.getForObject(url, String.class);

	}

}
