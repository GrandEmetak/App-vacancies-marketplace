package ru.job4j.currency;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Приложение "Курс Валют"  Часть 1/Парсинг сайта
 */
public class Temp {
    private static Document getPage() throws IOException {
        String url = "http://www.pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }


    public static void main(String[] args) throws IOException {
     /* Document  doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        var vef = doc.title();
        System.out.println(vef);
    }*/
        Document page = getPage();
        //css query language
        Elements tableWth = page.select("table[class=wt]");
       // System.out.println(tableWth);
        Elements names = tableWth.select("tr[class=wtr]");
        Elements values = tableWth.select("tr[valign=top]");
        for (Element name : names) {
            String date = name.select("th[id=dt]").text();
            System.out.println("    Явления     Температура     Давление    Влажность   Ветер");
        }

    }
}
