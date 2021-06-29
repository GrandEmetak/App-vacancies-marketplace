package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 2.1. Преобразование даты [# 289476]
 * Топик: 2.3.5. Проект. Агрегатор Java вакансий
 * Сайт sql.ru отображает дату в формате удобном для человека.
 * Такой формат Java не может преобразовать.
 * Вам нужно через методы String преобразовать строку в дату.
 * Выделим интерфейс package ru.job4j.gabber.utils.DateTimeParse
 * и реализуем его конкретно по сайту sql.ru
 */
public class SqlDateTimeParser implements DateTimeParser {
    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "01"),
            Map.entry("фев", "02"),
            Map.entry("мар", "03"),
            Map.entry("апр", "04"),
            Map.entry("май", "05"),
            Map.entry("июн", "06"),
            Map.entry("июл", "07"),
            Map.entry("авг", "08"),
            Map.entry("сен", "09"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12")
    );

    /**
     * Метод производит через методы String преобразование строки в дату(LocalDateTime).
     * @param parse строка разбираемая и конвернтируемая в объект LocalDateTime
     * @return LocalDateTime
     */
    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yy HH:mm");
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("d MM yy HH:mm");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd MM yy");

        String[] str = parse.split(",");
        if (str[0].contains("вчера") || str[0].contains("сегодня")) {
            if (str[0].contains("сегодня")) {
                LocalDate localDate = LocalDate.now();
                String ld = localDate.format(dtf2);
                String ld1 = ld + str[1];
                return LocalDateTime.parse(ld1, dtf1);
            } else {
                LocalDate localDate = (LocalDate.now()).minusDays(1);
                String ld = localDate.format(dtf2);
                String ld1 = ld + str[1];
                return LocalDateTime.parse(ld1, dtf);
            }
        }
        String[] arrStr = str[0].split(" "); // three column
        if (arrStr[0].length() < 2) {
            String rsl = arrStr[0] + " " + MONTHS.get(arrStr[1]) + " " + arrStr[2] + str[1];
            return LocalDateTime.parse(rsl, dtf1);
        } else {
            String rsl = arrStr[0] + " " + MONTHS.get(arrStr[1]) + " " + arrStr[2] + str[1];
            return LocalDateTime.parse(rsl, dtf);
        }
    }

    public static void main(String[] args) throws IOException {
        SqlDateTimeParser sqlDateTimeParser = new SqlDateTimeParser();
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            var elementDate = td.parent().child(5); //чилдрен содержащий дату
            String str = elementDate.text();
            System.out.println(str);
            System.out.println(sqlDateTimeParser.parse(str));
            System.out.println();
        }
    }
}
