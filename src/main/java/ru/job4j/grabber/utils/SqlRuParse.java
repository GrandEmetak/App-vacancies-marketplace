package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Post(package ru.job4j.grabber.utils.Post) это модель. В ней не должно быть парсинга.
 * Вынесена логика парсинга в класс SqlRuParse. 2.3. Загрузка деталей поста. [#285212 #174917]
 *Топик : 2.3.5. Проект.  Создайте метод для загрузки деталей объявления.
 * +
 * 2.4. SqlRuParse [#285213] Топик : 2.3.5. Проект.
 * В этом задании нужно собрать все элементы парсинга в классе SqlRuParse.
 * Метод list должен загружаеть список всех постов. *
 * Метод detail должен загружать детали одного поста в Post.
 */
public class SqlRuParse implements Parse {
    //Экземпляр класса с методом для конвертации даты в формат LocalDateTime
    private SqlDateTimeParser sqlDateTimeParser = new SqlDateTimeParser();
    //Модель данных Post включает (id, title, link, description, created(LocalDateTime))
    private List<Post> postList = new ArrayList<>();

    //пустой кончтруктор можно и не создавать
    public SqlRuParse() {
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
        Map<Integer, String> links = new HashMap<>();
        SqlDateTimeParser sqlDateTimeParser = new SqlDateTimeParser();
        //пример Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Document doc = Jsoup.connect(linkArg).get(); // запарсили титульную страницу из Аргуметов метода
        Elements row2 = doc.select(".sort_options"); // записали все Элементы по селекту
        Elements row = doc.select(".postslisttopic"); //все топики на текущей станице
        for (Element td : row) { // вынимаем по одному
            Element href = td.child(0); //заголовок
            //ссылка на топик он все запишет с него в Пост -- добавить возврат Пост объекта
            postList.add(detail(href.attr("href")));
        }
        // Парсить нужно первые 5 страниц.
        Elements link1 = row2.select("a[href]");
        for (Element element : link1) {
            // System.out.println("Первая ссылка и первый Элемент Сорт" + element.attr("href"));
           // System.out.println(element.text());
            links.put(Integer.parseInt(element.text()), element.attr("href"));
        }
        for (int i = 2; i < 6; i++) {
            String strLink = links.get(i); // получаем след страницу ссылку - стр 2
            Document doc1 = Jsoup.connect(strLink).get(); // загружаем ее
            Elements row3 = doc1.select(".postslisttopic"); // все топики с нее получаем
            for (Element td : row3) {
                Element href = td.child(0);
                postList.add(detail(href.attr("href")));
            }
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
        Elements dat = elements.select("td[class=msgFooter]");
        boolean msdt = dat.isEmpty();
        String[] daTeAr = dat.text().split(" ");
        // нижние if с вложенным if из-за приходящих со страницы с топиком данны
        // пример - сегодня, 18:22 [223447713] Ответить
        if (daTeAr[0].contains("вчера,") || daTeAr[0].contains("сегодня,")) {
            if (daTeAr[0].contains("сегодня,")) {
                String build0 = daTeAr[0] + " " + daTeAr[1];
                System.out.println("Т что по будет конвертировано по дате :" + build0);
                var time = sqlDateTimeParser.parse(build0);
                post.setCreated(time);
            } else {
                String build1 = daTeAr[0] + " " + daTeAr[1];
                System.out.println("Т что по будет конвертировано по дате :" + build1);

                var time = sqlDateTimeParser.parse(build1);
                post.setCreated(time);
            }
        } else {

            String bild = daTeAr[0] + " " + daTeAr[1] + " " + daTeAr[2] + " " + daTeAr[3];
            System.out.println("Т что по будет конвертировано по дате : " + bild);
            var time = sqlDateTimeParser.parse(bild);
            post.setCreated(time);

        }
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

        SqlRuParse sqlRuParse = new SqlRuParse();
        String link = "https://www.sql.ru/forum/job-offers";
        sqlRuParse.list(link);
        System.out.println(sqlRuParse.postList.size());
        Post wathsUp = sqlRuParse.postList.get(3);
        System.out.println(wathsUp.getTitle());
        System.out.println(wathsUp.getDescription());
        System.out.println(wathsUp.getCreated());
    }
}
