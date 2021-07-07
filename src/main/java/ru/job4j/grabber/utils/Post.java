package ru.job4j.grabber.utils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 2.3. Загрузка деталей поста. [#285212]
 * Топик : 2.3.5. Проект. Агрегатор Java вакансий
 * Создайте метод для загрузки деталей объявления.
 * Заполнить поля объекта Post по ссылке на страницу вакансии
 */
public class Post {
    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

}
