package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Post(package ru.job4j.grabber.utils.Post) это модель. В ней не должно быть парсинга.
 * Вынесена логика парсинга в класс SqlRuParse.
 */
public class SqlRuPar {
    private Post post = new Post();
    private SqlDateTimeParser sqlDateTimeParser = new SqlDateTimeParser();
//Экземпляр класса с методом для конвертации даты в формат LocalDateTime

    /**
     * Кнструктор, при создании экземпляра объекта передается ссылка в параметры,
     * для дальнейшего парсинга данной страницы и записи через метод loader,
     * данных в поля объекта
     *
     * @param link ссылка на страницу для парсинга и заполнения полей экземпляра класса
     * @throws IOException
     */
    public SqlRuPar(String link) throws IOException {
        loader(link);
    }

    /**
     * Метод проводит запись в поля объекат(id, title, link, description, created(LocalDateTime)) Post,
     * при его создании.
     * @param link ссылка на страницу вакансии
     * @throws IOException
     */
    private void loader(String link) throws IOException {
        post.setLink(link);
        Document document = Jsoup.connect(link).get();
        Elements elements = document.select(".msgTable");
        var tr = elements.select("td[class=messageHeader]");
        var elo = tr.get(0);
        //название вакансии
        String vacStr = elo.text();
        post.setTitle(vacStr); // zapisali nazvanie vakansii
//описание вакансии body
        Elements elements1 = document.select(".msgBody");
        String desc = elements1.get(1).text();
        post.setDescription(desc);
// дата
        Elements dat = elements.select("td[class=msgFooter]");
        boolean msdt = dat.isEmpty();
        String[] daTeAr = dat.text().split(" ");
        String bild = daTeAr[0] + " " + daTeAr[1] + " " + daTeAr[2] + " " + daTeAr[3];
        var time = sqlDateTimeParser.parse(bild);
        post.setCreated(time);
    }

    public static void main(String[] args) throws IOException {
        //несколько разных ссылок на разные вакансии
// String link = "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
// String link = "https://www.sql.ru/forum/1336128/arhitektor-resheniy-moskva-300-000-350-000-rub";
        String link = "https://www.sql.ru/forum/1337113/frontend-razrabotchik-remote-do-330-000";
        SqlRuPar sqlRuPar = new SqlRuPar(link);

        System.out.println();
        System.out.println(sqlRuPar.post.getId());
        System.out.println();
        System.out.println(sqlRuPar.post.getTitle());
        System.out.println();
        System.out.println(sqlRuPar.post.getLink());
        System.out.println();
        System.out.println(sqlRuPar.post.getDescription());
        System.out.println();
        System.out.println(sqlRuPar.post.getCreated());
    }
}
