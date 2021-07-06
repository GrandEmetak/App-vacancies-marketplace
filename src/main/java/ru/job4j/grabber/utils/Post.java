package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 2.3. Загрузка деталей поста. [#285212]
 * Топик : 2.3.5. Проект. Агрегатор Java вакансий
 * Создайте метод для загрузки деталей объявления.
 * Заполнить поля объекта Post по ссылке на страницу вакансии
 */
public class Post {
    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;

    /**
     * Кнструктор, при создании экземпляра объекта передается ссылка в параметры,
     * для дальнейшего парсинга данной страницы и записи через метод loader,
     * данных в поля объекта
     * @param link ссылка на страницу для парсинга и заполнения полей экземпляра класса
     * @throws IOException
     */
    public Post(String link) throws IOException {
        loader(link);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Метод проводит запись в поля объекат(id, title, link, description, created(LocalDateTime)) Post,
     * при его создании.
     * @param link ссылка на страницу вакансии
     * @throws IOException
     */
    private void loader(String link) throws IOException {
        //Экземпляр класса с методом для конвертации даты в формат LocalDateTime
        SqlDateTimeParser sqlDateTimeParser = new SqlDateTimeParser();

        this.link = link; // zapisali link na stranicy vacancii
        Document document = Jsoup.connect(link).get();
        Elements elements = document.select(".msgTable");
        var tr = elements.select("td[class=messageHeader]");
        var elo = tr.get(0);
        String ty = elo.attr("id");
        String[] arStr = ty.split("id");
        // zapisali id
        this.id = Integer.parseInt(arStr[1]);
        //название вакансии
        String vacStr = elo.text();
        this.title = vacStr; // zapisali nazvanie vakansii
//описание вакансии body
        Elements elements1 = document.select(".msgBody");
        Map<Integer, String> mapStr = new HashMap<>();
        int i = 0;
        for (Element element : elements1) {
            mapStr.put(i++, element.text());
        }
        this.description = mapStr.get(1); // zapisali opisanie vacancii
// дата
        Elements dat = elements.select("td[class=msgFooter]");
        boolean msdt = dat.isEmpty();
        String[] daTeAr = dat.text().split(" ");
        String bild = daTeAr[0] + " " + daTeAr[1] + " " + daTeAr[2] + " " + daTeAr[3];
        var time = sqlDateTimeParser.parse(bild);
        this.created = time;
    }

    public static void main(String[] args) throws IOException {
//несколько разных ссылок на разные вакансии
// String link = "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
// String link = "https://www.sql.ru/forum/1336128/arhitektor-resheniy-moskva-300-000-350-000-rub";
        String link = "https://www.sql.ru/forum/1337113/frontend-razrabotchik-remote-do-330-000";
        Post post = new Post(link);
        System.out.println();
        System.out.println(post.getId());
        System.out.println();
        System.out.println(post.getTitle());
        System.out.println();
        System.out.println(post.getLink());
        System.out.println();
        System.out.println(post.getDescription());
        System.out.println();
        System.out.println(post.getCreated());
    }

}
