package ru.job4j.auth.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.UserDetailsServiceImpl;

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
    private final UserDetailsServiceImpl users;
    private final BCryptPasswordEncoder encoder;

    public SignUpController(UserDetailsServiceImpl users,
                            BCryptPasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
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
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        users.create(person);
    }
}
