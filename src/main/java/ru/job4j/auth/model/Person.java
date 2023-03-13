package ru.job4j.auth.model;

import lombok.Data;
import ru.job4j.auth.util.Operation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Зеленский Н. aka Nike Z.
 * Сущность Person
 */
@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id must be more than 0",
            groups = {Operation.OnFind.class, Operation.OnUpdate.class, Operation.OnDelete.class})
    private int id;

    @NotNull(message = "login must be non null",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    @NotBlank(message = "login must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String login;

    @NotNull(message = "password must be non null",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    @NotBlank(message = "password must be not empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String password;
}
