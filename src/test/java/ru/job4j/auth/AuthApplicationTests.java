package ru.job4j.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class AuthApplicationTests {

	@Test
	void contextLoads() {
		assertThat(true).isEqualTo(true);
	}

}
