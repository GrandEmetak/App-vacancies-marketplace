package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 2.1. Преобразование даты [# 289476]
 *  Топик: 2.3.5. Проект. Агрегатор Java вакансий
 *  Сайт sql.ru отображает дату в формате удобном для человека.
 *  Такой формат Java не может преобразовать.
 *  Вам нужно через методы String преобразовать строку в дату.
 *  Реализовать метод, преобразующий дату из формата sql.ru.
 * Выделим интерфейс
 */
public interface DateTimeParser {

     LocalDateTime parse(String parse);
}
