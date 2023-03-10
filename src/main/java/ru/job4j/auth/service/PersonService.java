package ru.job4j.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository persons;

    /**
     * Получить список всех {@link ru.job4j.auth.model.Person}
     * @return тип {@link java.util.List<ru.job4j.auth.model.Person>}
     * список хранящихся Person в сервисе.
     */
    public List<Person> findAll() {
        return Streamable.of(this.persons.findAll()).toList();
    }

    /**
     * Получить {@link ru.job4j.auth.model.Person} по {@param id}
     * @param id - идентификатор тип int.
     * @return тип {@link java.util.Optional<ru.job4j.auth.model.Person>}
     */
    public java.util.Optional<Person> findById(int id) {
        return persons.findById(id);
    }

    /**
     * Сервис добавляет сущность Person в хранилище сервера
     * @param person - добавляемая сущность тип {@link ru.job4j.auth.model.Person}
     * @return тип {@link ru.job4j.auth.model.Person}
     * содержит результат попытки добавить сущность Person.
     */
    public Person create(Person person) {
        return this.persons.save(person);
    }

    /**
     * Сервис обновляет сущность Person в хранилище сервера
     * @param person - сохраняемая сущность тип {@link ru.job4j.auth.model.Person}
     * @return тип boolean
     * true  - если пользователь обновлён
     * false - если без изменений
     * @throws NoSuchElementException - если пользователя нет в хранилище
     */
    public boolean update(Person person) {
        Optional<Person> person1 = this.persons.findById(person.getId());
        if (person1.isEmpty()) {
            throw new NoSuchElementException(
                    "Пользователь {id=" + person.getId()
                            + "; login=" + person.getLogin()
                            + "; password" + person.getPassword()
                            + "} не обновлён т.к. не найден");
        }
        if (person1.get().getPassword().equals(person.getPassword())
                && person1.get().getLogin().equals(person.getLogin())) {
            return false;
        }
        this.persons.save(person);
        return true;
    }

    /**
     * Сервис удаляет сущность Person в хранилище сервера по {@param id}
     * @param id - удаляемая сущность тип int.
     * @return тип boolean
     * true  - пользователь c ID удален
     * false - пользователь c ID не найден
     */
    public boolean delete(int id) {
        Optional<Person> person = this.persons.findById(id);
        person.ifPresent(p -> this.persons.deleteById(id));
        return person.isPresent();
    }

    /**
     * Поиск пользователя в хранилище по логину
     * @param login строка с логином пользователя
     *              тип {@link java.lang.String}
     * @return тип {@link java.util.Optional<ru.job4j.auth.model.Person>}
     */
    public Optional<Person> findByLogin(String login) {
        return persons.findByLogin(login);
    }
}
