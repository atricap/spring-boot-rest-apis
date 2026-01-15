package com.luv2code.springboot.todos;

import com.luv2code.springboot.todos.entity.Authority;
import com.luv2code.springboot.todos.response.AuthenticationResponse;
import com.luv2code.springboot.todos.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestTestClient
class UserTests {

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void users_info_when_authenticated() {
		String token = restTestClient.post()
				.uri("/api/auth/login")
				.header("Content-Type", "application/json")
				.body("""
						{"email": "mary@luv2code.com",
						 "password": "test123"}""")
				.exchange()
				.returnResult(AuthenticationResponse.class)
				.getResponseBody()
				.getToken();
		assertNotNull(token, "Token should not be null");

		restTestClient.get()
				.uri("/api/users/info")
				.header("Authorization", "Bearer " + token)
				.exchange()
				.expectBody(UserResponse.class)
				.isEqualTo(new UserResponse(2, "Mary Garcia", "mary@luv2code.com",
						List.of(Authority.EMPLOYEE, Authority.MANAGER)));
	}

}
