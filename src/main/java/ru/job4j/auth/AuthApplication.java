package ru.job4j.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Зеленский Н. aka Nike Z.
 * Запуск сервиса
 */
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
		System.out.println("start REST http://localhost:8080/person/");
	}

}
