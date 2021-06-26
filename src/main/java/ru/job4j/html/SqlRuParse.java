package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 2. Парсинг HTML страницы. [#260358]
 * Топик : 2.3.5. Проект. Агрегатор Java
 * В нашей программе мы сделаем запрос на сервер, получим HTML.
 * Для этой операции воспользуемся библиотекой jsoup.
 * Она позволяет сделать запрос на сервер и извлечь нужный текст из полученного HTML.
 * Добавьте в программу вывод даты.
 */
public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            var date = td.parent().child(5); //чилдрен содержащий дату
            System.out.println(date.text()); //вывод даты
            System.out.println();
        }
    }
}
