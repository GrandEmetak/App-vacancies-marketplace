package app.vacancies.grabber.utils;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Преобразование даты
 * Сайт sql.ru отображает дату в формате удобном для человека.
 * Такой формат Java не может преобразовать.
 * Нужно через методы String преобразовать строку в дату.
 * Реализовать метод, преобразующий дату из формата sql.ru.
 * Выделим интерфейс
 */
public interface DateTimeParser {

    LocalDateTime parse(String parse);
}
