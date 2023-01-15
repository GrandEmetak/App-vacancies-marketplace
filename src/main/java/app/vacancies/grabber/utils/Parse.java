package app.vacancies.grabber.utils;

import java.io.IOException;
import java.util.List;

/**
 * Извлечение данных с сайта.
 * Операция извлечения данных с  сайта описывается отдельным интерфейсом.
Parse
 */
public interface Parse {
    /**
     * Этот метод загружает список объявлений по ссылке типа -
     * "https://www.sql.ru/forum/job-offers/1 "
     * @param link ссылка основной страницы с которой начинать парсинг сайта
     * @return
     * @throws IOException
     */
    List<Post> list(String link) throws IOException;

    /**
     * Этот метод загружает детали объявления по ссылке типа -
     * "https://www.sql.ru/forum/1323839/razrabotchik-java-g-kazan"
     * Непосредственна сама страница вакансии/topic с описанием
     * Производит запись в экземпляр класса Post
     * @param link
     * @return
     * @throws IOException
     */
    Post detail(String link) throws IOException;
}
