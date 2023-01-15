package app.vacancies.grabber.utils;

import java.util.List;

/**

 * Хранилище.
 * Проект будет хранить данные в базе Postgresql.
 * Связь с базой будет осуществляться через интерфейс. -.Store.
 */
public interface Store {

    /**
     * Метод save() - сохраняет объявление в базе.
     * @param post
     */
    void save(Post post) throws Exception;

    /**
     * Метод getAll() - позволяет извлечь объявления из базы.
     * @return
     */
    List<Post> getAll() throws Exception;

    /**
     *Метод findById(int id) - позволяет извлечь объявление из базы по id.
     * @param id
     * @return
     */
    Post findById(int id) throws Exception;
}
