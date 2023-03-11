package ru.job4j.auth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.auth.model.Person;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private PersonService persons;

    public UserDetailsServiceImpl(PersonService personService) {
        this.persons = personService;
    }

    /**
     * Поиск пользователя в хранилище по логину
     * @param username - строка с логином пользователя
     * @return тип {@link org.springframework.security.core.userdetails.User}
     *  детальная информация о пользователе для авторизации
     * @throws UsernameNotFoundException исключение при ошибке
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person user = persons.findByLogin(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getLogin(), user.getPassword(), emptyList());
    }

    /**
     * Создание и сохранение пользователя в хранилище
     * @param user пользователь
     * @return пользователь
     */
    public Person create(Person user) {
        return persons.create(user);
    }

    /**
     * Вернуть список всех пользователей из хранилища
     * @return список пользователей
     */
    public List<Person> findAll() {
        return persons.findAll();
    }
}
