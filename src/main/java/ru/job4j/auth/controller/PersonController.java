package ru.job4j.auth.controller;

import com.sun.istack.NotNull;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.util.Operation;

import javax.validation.Valid;

/**
 * Rest контроллер для Person
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    final static Logger LOGGER = Logger.getLogger(PersonController.class);
    private final PersonService persons;

    public PersonController(final PersonService persons) {
        this.persons = persons;
    }

    /**
     * Получить список всех {@link ru.job4j.auth.model.Person}
     * @return тип {@link java.util.List<ru.job4j.auth.model.Person>}
     * список хранящихся Person в сервисе.
     */
    @GetMapping("/")
    public List<Person> findAll() {
        return this.persons.findAll();
    }

    /**
     * Получить {@link ru.job4j.auth.model.Person} по {@param id}
     * @param id - идентификатор тип int.
     * @return тип {@link org.springframework.http.ResponseEntity<ru.job4j.auth.model.Person>}
     */
    @GetMapping("/{id}")
    @Validated(Operation.OnFind.class)
    public ResponseEntity<Person> findById(@Valid @PathVariable int id) {
        var person = this.persons.findById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    /**
     * Сервис добавляет сущность Person в хранилище сервера
     * @param person - добавляемая сущность тип {@link ru.job4j.auth.model.Person}
     * @return тип {@link org.springframework.http.ResponseEntity<ru.job4j.auth.model.Person>}
     * содержит результат попытки добавить сущность Person.
     */
    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        validatePerson(person, "Create: ");
        return new ResponseEntity<Person>(
                this.persons.create(person),
                HttpStatus.CREATED
        );
    }

    /**
     * Сервис обновляет сущность Person в хранилище сервера
     * @param person - сохраняемая сущность тип {@link ru.job4j.auth.model.Person}
     * @return тип {@link org.springframework.http.ResponseEntity<java.lang.Void>}
     *  status = 304 - не изменено. Если не поменялось содержимое.
     */
    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        validatePerson(person, "Update: ");
        ResponseEntity<Void> response;
        try {
            response = this.persons.update(person) ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } catch (NoSuchElementException e) {
            LOGGER.error("PUT /person/ " + e.getMessage());
            response = ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOGGER.error("PUT /person/ " + e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    /**
     * Сервис удаляет сущность Person в хранилище сервера по {@param id}
     * @param id - удаляемая сущность тип int.
     * @return тип {@link org.springframework.http.ResponseEntity<java.lang.Void>}
     */
    @DeleteMapping("/{id}")
    @Validated(Operation.OnDelete.class)
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        ResponseEntity<Void> response;
        try {
            response = this.persons.delete(id) ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } catch (Exception e) {
            LOGGER.error("DELETE /person/" + id + " " + e.getMessage());
            response = ResponseEntity.internalServerError().build();
        }
        return response;
    }

    /**
     * Проверка заполнения полей пользователя
     * @param person пользователь тип {@link ru.job4j.auth.model.Person}
     * @param headMsg начало сообщения 'login and password mustn't be empty'
     */
    private void validatePerson(Person person, @NotNull String headMsg) {
        if (person == null
                || person.getLogin() == null
                || person.getPassword() == null
        ) {
            throw new NullPointerException(headMsg + "login and password mustn't be empty");
        }
    }

    /**
     * метод PATCH для модели Person
     * @param person пользователь - поля: id, login, password.
     *               Поле id обязательно и должно быть целым положительным числом
     * @return тип {@link ru.job4j.auth.model.Person} обновлённый пользователь из хранилища
     * @throws InvocationTargetException ошибка
     * @throws IllegalAccessException ошибка
     */
    @PatchMapping("/updatePatchMappingExample")
    @Validated(Operation.OnUpdate.class)
    public Person updatePatchMappingExample(@Valid @RequestBody Person person)
            throws InvocationTargetException, IllegalAccessException {
        var current = persons.findById(person.getId()).orElse(null);
        if (current == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var methods = current.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Impossible invoke set method from object : "
                                    + current + ", Check set and get pairs.");
                }
                var newValue = getMethod.invoke(person);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        persons.update(current);
        return current;
    }
}