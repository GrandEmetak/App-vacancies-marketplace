package ru.job4j.grabber.utils;

import java.util.List;

/**
 * 3. Архитектура проекта - Аргегатор Java Вакансий [#260359]
 * Хранилище.
 * Наш проект будет хранить данные в базе Postgresql.
 * Связь с базой будет осуществляться через интерфейс. ru.job4j.grabber.Store.
 */
public interface Store {
    /**
     * Метод save() - сохраняет объявление в базе.
     * @param post
     */
    void save(Post post);

    /**
     * Метод getAll() - позволяет извлечь объявления из базы.
     * @return
     */
    List<Post> getAll();

    /**
     *Метод findById(int id) - позволяет извлечь объявление из базы по id.
     * @param id
     * @return
     */
    Post findById(int id);
}
