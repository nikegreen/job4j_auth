package ru.job4j.auth.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import ru.job4j.auth.util.Operation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;

/**
 * Контроллер для регистрации Person
 * -------------------------------------
 * Пример регистрации:
 * curl.exe -H "Content-Type: application/json" -X POST -d '{
 * >>     \"login\": \"admin3\",
 * >>     \"password\": \"password\"
 * >> }' http:/ /localhost:8080/person/sign-up
 * -------------------------------------
 * Пример входа:
 * curl.exe -i -H "Content-Type: application/json" -X POST -d '{
 * >>     \"login\": \"admin3\",
 * >>     \"password\": \"password\"
 * >> }' http:/ /localhost:8080/login
 * -------------------------------------
 * Ответ:
 * HTTP/1.1 200
 * Vary: Origin
 * Vary: Access-Control-Request-Method
 * Vary: Access-Control-Request-Headers
 * Authorization: Bearer eyJ0eXAiOiJKV1SiLCJhbGciOiJIUzUxMiJ1.
 * EyJzdWIiOiJhZG1pbjMiLCJleHAiOjE2NzkzODkxMDJ0.
 * c_CvS9Xg0f4Glkt7bGUa9miUE-bJcs7KeIrQufZocvljH9hTu9PVr31dvyLfRyXso7tZLmkEYtWybqjHVBRNKM
 * X-Content-Type-Options: nosniff
 * Cache-Control: no-cache, no-store, max-age=0, must-revalidate
 * Pragma: no-cache
 * Expires: 0
 * X-Frame-Options: DENY
 * Content-Length: 0
 * Date: Sat, 11 Mar 2023 08:58:22 GMT
 * -------------------------------------
 * Пример команды вывода всех пользователей:
 * curl.exe -H "Authorization: Bearer eyJ0eXAiOiJKV1SiLCJhbGciOiJIUzUxMiJ1.
 * EyJzdWIiOiJhZG1pbjMiLCJleHAiOjE2NzkzODkxMDJ0.c_CvS9Xg0f4Glkt7bGUa9miUE-
 * bJcs7KeIrQufZocvljH9hTu9PVr31dvyLfRyXso7tZLmkEYtWybqjHVBRNKM"
 * -X GET http:/ /localhost:8080/person/
 */
@RestController
@RequestMapping("/person")
public class SignUpController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SignUpController.class.getSimpleName());
    private final UserDetailsServiceImpl users;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    /**
     * Конструктор
     * @param users тип {@link ru.job4j.auth.service.UserDetailsServiceImpl}
     *              сервис для авторизации пользователя
     * @param encoder тип {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}
     *                шифровальщик пароля
     * @param objectMapper тип {@link com.fasterxml.jackson.databind.ObjectMapper}
     */
    public SignUpController(UserDetailsServiceImpl users,
                            BCryptPasswordEncoder encoder,
                            ObjectMapper objectMapper) {
        this.users = users;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    /**
     * <p>Обработчик запроса регистрации нового пользователя</p>
     * @param person новый пользователь {@link ru.job4j.auth.model.Person}
     *               Формат данных: json.
     *               Пример запроса:
     * curl.exe -H "Content-Type: application/json" -X POST -d '{
     * >>     \"login\": \"admin3\",
     * >>     \"password\": \"password\"
     * >> }' http:/ /localhost:8080/person/sign-up
     */
    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public void signUp(@Valid @RequestBody Person person) {
        if (person == null
                || person.getLogin() == null
                || person.getPassword() == null
        ) {
            throw new NullPointerException("login and password mustn't be empty");
        }
        if (person.getPassword().length() < 1) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        users.create(person);
    }

    /**
     * Обработчик IllegalArgumentException исключения в SignUpController
     * @param e тип {@link java.lang.Exception} исключение
     * @param request тип {@link javax.servlet.http.HttpServletRequest} HTTP запрос
     * @param response тип {@link javax.servlet.http.HttpServletResponse} HTTP ответ
     * @throws IOException в случае ошибки ввода/вывода
     * ---------------------------------------------------------------------
     * Важно! Если Вы используете Spring Boot, то в целях безопасности
     * он ограничивает отображение сообщения ошибки.
     * Для того чтобы оно появилось в теле ответа,
     * добавьте следующую строку в application.properties:
     * server.error.include-message=always
     */
    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }
}
