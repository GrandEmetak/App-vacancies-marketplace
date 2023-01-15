package app.vacancies.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * @return List Post объектов (у каждого объекат прописываются поля title, link, description, created)
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
}
