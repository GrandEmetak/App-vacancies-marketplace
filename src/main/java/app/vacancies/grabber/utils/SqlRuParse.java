package app.vacancies.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Post(package ru.job4j.grabber.utils.Post) это модель. В ней не должно быть парсинга.
 * Вынесена логика парсинга в класс SqlRuParse. 2.3. Загрузка деталей поста. [#285212 #174917]
 * Топик : 2.3.5. Проект.  Создайте метод для загрузки деталей объявления.
 * +
 * 2.4. SqlRuParse [#285213] Топик : 2.3.5. Проект.
 * В этом задании нужно собрать все элементы парсинга в классе SqlRuParse.
 * Метод list должен загружаеть список всех постов. *
 * Метод detail должен загружать детали одного поста в Post.
 */
public class SqlRuParse implements Parse {

    private DateTimeParser sqlDateTimeParser;

    // в конструктор передается интерфейс
    //Это не интерфейс "объект", передаваемый методу, все еще просто обычный объект.
    // Это просто способ сказать "этот параметр принимает любой объект, который поддерживает этот интерфейс".
    // Это эквивалентно принятию некоторого объекта типа базового класса, даже если вы передаете подкласс.
    //это называется программированием на интерфейсы. Вы не кодируете определенный класс реализации списков узлов,
    // а интерфейс, реализованный всеми этими реализациями.
    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.sqlDateTimeParser = dateTimeParser;
    }

    /**
     * Загружает список всех постов
     * Метод проводит разбор страницы пришедшей в сылке-links,
     * запись всех топиков на странице (каждый тотпик записывается в экземпляр объекат Post)
     * проходит первые 5 страниц начиная от титутльной, запись постов происходи на каждой стрнанице
     *
     * @param link титульная страница сайта для парсинга данных постов(topics)
     * @return List<Post> объектов (у каждого объекат прописываются поля title, link, description, created)
     * @throws IOException
     */
    @Override
    public List<Post> list(String link) throws IOException {
        String linkArg = link;
        //Модель данных Post включает (id, title, link, description, created(LocalDateTime))
        List<Post> postList = new ArrayList<>();
        //пример Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Document doc = Jsoup.connect(linkArg).get(); // запарсили титульную страницу из Аргуметов метода
        Elements row = doc.select(".postslisttopic"); //все топики на текущей станице
        for (Element td : row) { // вынимаем по одному
            Element href = td.child(0); //заголовок
            //ссылка на топик он все запишет с него в Пост -- добавить возврат Пост объекта
            postList.add(detail(href.attr("href")));
        }
        return postList;
    }

    /**
     * Загружает детали одного поста
     * Метод проводит запись в поля объекат(id, title, link, description, created(LocalDateTime)) Post,
     * при его создании.
     *
     * @param link ссылка на страницу вакансии(одна вакансия/topiс)
     * @return
     */
    @Override
    public Post detail(String link) throws IOException {
        Post post = new Post();
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
        String date = elements.select("td[class=msgFooter]").text();
        var time = sqlDateTimeParser.parse(date.substring(0, date.indexOf(" [")));
        post.setCreated(time);
        return post;
    }

    public static void main(String[] args) throws IOException {
        //несколько разных ссылок на разные вакансии
// String link = "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
// String link = "https://www.sql.ru/forum/1336128/arhitektor-resheniy-moskva-300-000-350-000-rub";
      /*  String link = "https://www.sql.ru/forum/1337113/frontend-razrabotchik-remote-do-330-000";
        SqlRuParse sqlRuParse = new SqlRuParse(link);
        System.out.println();
        System.out.println(sqlRuParse.post.getId());
        System.out.println();
        System.out.println(sqlRuParse.post.getTitle());
        System.out.println();
        System.out.println(sqlRuParse.post.getLink());
        System.out.println();
        System.out.println(sqlRuParse.post.getDescription());
        System.out.println();
        System.out.println(sqlRuParse.post.getCreated());*/

        SqlDateTimeParser sqlDateTimeParser = new SqlDateTimeParser();
        SqlRuParse sqlRuParse = new SqlRuParse(sqlDateTimeParser);
        List<Post> postList = new ArrayList<>();
        String link = "https://www.sql.ru/forum/job-offers";
        postList = sqlRuParse.list(link);
        System.out.println(postList.size());
        Post wathsUp = postList.get(3);
        System.out.println(wathsUp.getTitle());
        System.out.println(wathsUp.getDescription());
        System.out.println(wathsUp.getCreated());
    }
}
