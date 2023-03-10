package ru.job4j.auth.controller;

import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;
import ru.job4j.auth.service.PersonService;

import java.util.List;

/**
 * Rest контроллер для Person
 */
@RestController
@RequestMapping("/person")
public class PersonController {
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
    public ResponseEntity<Person> findById(@PathVariable int id) {
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
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<Person>(
                this.persons.create(person),
                HttpStatus.CREATED
        );
    }

    /**
     * Сервис обновляет сущность Person в хранилище сервера
     * @param person - сохраняемая сущность тип {@link ru.job4j.auth.model.Person}
     * @return тип {@link org.springframework.http.ResponseEntity<java.lang.Void>}
     */
    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        this.persons.update(person);
        return ResponseEntity.ok().build();
    }

    /**
     * Сервис удаляет сущность Person в хранилище сервера по {@param id}
     * @param id - удаляемая сущность тип int.
     * @return тип {@link org.springframework.http.ResponseEntity<java.lang.Void>}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.persons.delete(id);
        return ResponseEntity.ok().build();
    }
}