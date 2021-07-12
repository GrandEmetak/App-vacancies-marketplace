package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 * 1. Quartz [#175122]
 * Топик : 2.3.5. Проект. Агрегатор Java
 */
public class AlertRabbit {
    private Properties properties = new Properties();
    private String path;


    public AlertRabbit(String path) {
        this.path = path;
        load();
    }

    private void load() {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            this.properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /* private String path;
    private final Map<String, Integer> values = new HashMap<>();

    public AlertRabbit(String path) {
        this.path = path;
        load();
    }

    public void load() {
        final int[] i = new int[1];
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(this.path))) {
            bufferedReader.lines().forEach(str -> {
                String[] strings = str.split("=");
                if (strings.length <= 1) {
                    throw new IllegalArgumentException();
                } else {
                    try {
                        i[0] = Integer.parseInt(strings[1].trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    values.put(strings[0], i[0]);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getInteger(String key) {
        return values.get(key);
    }*/

    public static void main(String[] args) {
        try {
            //Планировщик(Scheduler) - основной API для взаимодействия с планировщиком фреймворка.
            //Прежде чем мы сможем использовать Планировщик , необходимо создать его экземпляр.
            // Для этого мы можем использовать фабрику SchedulerFactory :
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            //Однако планировщик не будет действовать на любые триггеры ,
            // пока не было начато с запуска () метод :
            scheduler.start();
            //JobDetail - используется для определения экземпляров Job s
            //Объект JobDetail создается клиентом Quartz во время добавления задания в планировщик.
            // По сути, это определение экземпляра задания
            AlertRabbit alertRabbit = new AlertRabbit("./appRab.properties");
           /* int i = alertRabbit.getInteger("rabbit.interval");
           это для варианта решения класса AlterRabbit что выше заккоментен*/
            String res = alertRabbit.properties.getProperty("rabbit.interval");
            int i = Integer.parseInt(res);
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(i)
                    .repeatForever();
            //Триггер - компонент, определяющий расписание, по которому будет выполняться данное задание.
            //Объекты- триггеры используются для запуска выполнения заданий .
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    //Job - интерфейс, который будет реализован компонентами, которые мы хотим выполнить.
    //Когда срабатывает триггер задания ,
// метод execute () вызывается одним из рабочих потоков планировщика.
    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}
