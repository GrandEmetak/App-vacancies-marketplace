package ru.job4j.grabber.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 5. PsqlStore [#285209]
 * Топик : 2.3.6. Проект. Агрегатор Java
 * Реализуйте класс PsqlStore на основании интерфейса Store.
 * Реализуйте класс PsqlStore.
 * Напишите метод main для демонстрации работы класса PsqlStore.
 * Реализует интерфейс AutoCloseable
 */
public class PsqlStore implements Store, AutoCloseable {
    private Connection cnn;

    /**
     * Метод привязывыет драйев, открывает создает объект для соединение
     *
     * @param cfg файл конфигурации(ключ/драйвер) для установки соединения
     */
    public PsqlStore(Properties cfg) throws SQLException {
        try {
            Class.forName(cfg.getProperty("hibernate.connection.driver_class"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        String url = cfg.getProperty("hibernate.connection.url");
        String login = cfg.getProperty("hibernate.connection.username");
        String password = cfg.getProperty("hibernate.connection.password");
        cnn = DriverManager.getConnection(url, login, password);
    }

    /**
     * Метод save(Post post) - сохраняет указанное объявление (в виде объект Post) в БД.
     *
     * @param post объект включает(id, title, link, description, created-LocalDateTime)
     * @throws Exception
     */
    @Override
    public void save(Post post) throws Exception {
        try (PreparedStatement preparedStatement =
                     cnn.prepareStatement("insert into post(name, text, link, created) values(?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            //preparedStatement.setInt(1, );
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getLink());
            //необходимо провести преобразование LocalDateTime в TimeStamp
            preparedStatement.setTimestamp(4, timeConvert(post));
            preparedStatement.execute();
            try (ResultSet resultSetKey = preparedStatement.getGeneratedKeys()) {
                if (resultSetKey.next()) {
                    post.setId(resultSetKey.getInt(1));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Метод getAll() - позволяет извлечь все объявления(topics) из базы данных
     *
     * @return List<Post> всех объектов находящися на момет вызова в БД
     * @throws Exception
     */
    @Override
    public List<Post> getAll() throws Exception {
        List<Post> postList = new ArrayList<>();
        int count = 0;
        String sql = "select count(id) from post";
        try (Statement statement = cnn.createStatement()) {
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
                //System.out.println("общее число сущностей по id : " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= count; i++) {
            postList.add(findById(i)); //write object Post in to List
        }
        return postList;
    }

    /**
     * Метод findById(int id) - позволяет извлечь объявление из Базы Данных по id.
     *
     * @param id топика в БД(располжен Post - int id)
     * @return объект Post from DB
     */
    @Override
    public Post findById(int id) {
        if (id == 0) {
            throw new IllegalArgumentException("Id don't be a 0(null)");
        }
        Post post = new Post(); // модель данных
        try (PreparedStatement preparedStatement =
                     cnn.prepareStatement("select * from post where id = ?")) {
            preparedStatement.setInt(1, id);
            var ryt = preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            System.out.println(" resultSet : " + ryt);
            while (resultSet.next()) {
                int id1 = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String text = resultSet.getString("text");
                String link = resultSet.getString("link");
                LocalDateTime created = timeConvertToLocal(resultSet.getTimestamp("created"));
                post.setId(id1);
                post.setTitle(name);
                post.setDescription(text);
                post.setLink(link);
                post.setCreated(created);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * Метод проводит конвертацию даты из LocalDateTime(java) to TimeStame(sql-java)
     *
     * @param post объект после парсинга топика сайта
     * @return конвертированное время
     */
    private Timestamp timeConvert(Post post) {
        var time = post.getCreated();
        Timestamp timestamp = Timestamp.valueOf(time);
        return timestamp;
    }

    /**
     * Метод проводит обратныую конвертацию.
     * Полученный из БД TimeStamp in to LocalDateTime
     *
     * @param timestamp
     * @return LocalDateTime
     */
    private LocalDateTime timeConvertToLocal(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    /**
     * Метод закрывает соединение с базой
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) throws Exception {
        Post post1;
        String name = "Fedor";
        String text = "Ivanov";
        String link = "https://www.sql.ru/forum/job-watsUp";
        LocalDateTime localDateTime = LocalDateTime.now();
        Post postN = new Post();
        postN.setTitle(name);
        postN.setLink(link);
        postN.setDescription(text);
        postN.setCreated(localDateTime);
        System.out.println("to chto v novom post : " + postN.toString());

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("./cfg.properties"))) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PsqlStore psqlStore = new PsqlStore(props);
        psqlStore.save(postN); //save object in DB
        post1 = psqlStore.findById(1); //find object by index(id )
        System.out.println("to chto nashel FindById : " + post1.toString());
        List<Post> postList = psqlStore.getAll();
        for (Post post : postList) {
            System.out.println("to chto zashlo v list : " + post);
        }
        psqlStore.close();
    }
}
