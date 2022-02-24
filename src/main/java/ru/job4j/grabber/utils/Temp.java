package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * полностью рабочий вариант
 */
public class Temp {
    public static void main(String[] args) throws IOException {
        Map<Integer, String> links = new HashMap<>();
        //[] links = new String[20];
        SqlDateTimeParser sqlDateTimeParser = new SqlDateTimeParser();
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row2 = doc.select(".sort_options");
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
        Elements link = row2.select("a[href]");
        System.out.println("ссылка что пришла с титульной страницы : нижние 2 принта");
        for (Element element : link) {
            //var por = element.attr("href");
            System.out.println(element.attr("href"));
            System.out.println(element.text());
            links.put(Integer.parseInt(element.text()), element.attr("href"));
        }
        for (int i = 2; i < 6; i++) {
            String strLink = links.get(i);
            System.out.println("то что вынимаем по ключб из Мапы : " + strLink);
            Document doc1 = Jsoup.connect(strLink).get();
            Elements row3 = doc1.select(".postslisttopic");
            for (Element td : row3) {
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



      /*  Element element = row.first();
        //System.out.println(element);
        for (Element el : row) {
            Element tr = el.child(0);
            System.out.println(tr);

            System.out.println(tr.attr("href")); //вывели все Хрефы
            System.out.println(tr.text());
        }*/
        /*for (Element td: row) {
            System.out.println("element" + td);
            Element href = td.child(0);
            //System.out.println("что внутри у ребенка " + href);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            System.out.println("Дети Хреф : " + href.child(0));
            var elementDate = td.parent().child(5); //чилдрен содержащий дату
            String str = elementDate.text();
            System.out.println(str);
            // System.out.println(sqlDateTimeParser.parse(str));
            System.out.println();
        }*/

        //рабочий блок выводит ссылки
      /*  Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".sort_options");
        System.out.println(row);

      /*  for (Element el : row) {
            System.out.println(el);*/
      /*  var tr = row.attr("td[style=text-align:left]");
        System.out.println(tr);
        Elements link = row.select("a[href]");
        for (Element element : link) {
            System.out.println(element.attr("href"));
            System.out.println(element.text());
        }*/

        // Element tr = el.child(0);
           /* System.out.println(tr);

            System.out.println(tr.attr("href")); //вывели все Хрефы
            System.out.println(tr.text());*/
    }
}


