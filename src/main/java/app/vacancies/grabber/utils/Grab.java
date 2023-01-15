package app.vacancies.grabber.utils;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Периодический запуск.
 * В этом проекты будет использовать quartz для запуска парсера.
 * Но напрямую мы не будем его использовать.
 * Абстрагируемся через интерфейс - Grab
 */
public interface Grab {

    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
