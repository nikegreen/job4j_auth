package ru.job4j.auth.util;

/**
 * Зеленский Н. aka Nike Z.
 * Класс маркеров для выбора событий обработки исключений Person
 */
public class Operation {
    /**
     * Создание
     */
    public interface OnCreate {
    }

    /**
     * Поиск
     */
    public interface OnFind {
    }

    /**
     * Удаление
     */
    public interface OnDelete {
    }

    /**
     * Обновление
     */
    public interface OnUpdate {
    }
}
