package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.model.Person;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    /**
     * Поиск пользователя в хранилище по логину
     * @param login строка с логином пользователя
     *              тип {@link java.lang.String}
     * @return тип {@link java.util.Optional<ru.job4j.auth.model.Person>}
     */
    Optional<Person> findByLogin(String login);
}