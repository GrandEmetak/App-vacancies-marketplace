package app.vacancies.grabber.utils;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * 3. Архитектура проекта - Аргегатор Java Вакансий [#260359]
 * Периодический запуск.
 * В этом проекты мы будем использовать quartz для запуска парсера. Но напрямую мы не будем его использовать.
 * Абстрагируемся через интерфейс - ru.job4j.grabber.Grab
 */
public interface Grab {
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
