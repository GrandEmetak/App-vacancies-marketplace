package app.vacancies.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 * 1.1. Job c параметрами
 * Топик : 2.3.5. Проект. Агрегатор Java вакансий
 * В проекте агрегатор будет использоваться база данных. Открыть и закрывать соединение с базой накладно.
 * Чтобы этого избежать коннект к базе будет создаваться при старте. Объект коннект будет передаваться в Job.
 * Quartz создает объект Job, каждый раз при выполнении работы.
 * 1.Доработайте класс AlertRabbit. Добавьте в файл app.properties настройки для базы данных.
 * 2. Создайте sql schema с таблицей rabbit и полем created_date.
 * 3. При старте приложения создайте connect к базе и передайте его в Job.
 * 4. В Job сделайте запись в таблицу, когда выполнена Job.
 * 5. Весь main должен работать 10 секунд.(Thread.sleep(10000);)
 * 6. Закрыть коннект нужно в блоке try-with-resources.
 */
public class AlertRabbitProp {

    private String path;
    private Properties properties = new Properties();

    public AlertRabbitProp(String path) {
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

    public static void main(String[] args) {
        AlertRabbitProp alertRabbitProp = new AlertRabbitProp("./appRab.properties");
        String str = alertRabbitProp.properties.getProperty("hibernate.connection.driver_class");
        try {
            Class.forName(str);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found");
            e.printStackTrace();
        }
        try (Connection connect =
                     DriverManager.getConnection(
                             alertRabbitProp.properties.getProperty("hibernate.connection.url"),
                             alertRabbitProp.properties.getProperty("hibernate.connection.username"),
                             alertRabbitProp.properties.getProperty("hibernate.connection.password"))) {
            try {
                List<Connection> store = new ArrayList<>();
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();
                String s = alertRabbitProp.properties.getProperty("rabbit.interval");
                int i = Integer.parseInt(s);
                System.out.println("интервал rabbit.interval : " + i);
                JobDataMap data = new JobDataMap();
                data.put("store", connect);
                JobDetail job = newJob(Rabbit.class)
                        .usingJobData(data)
                        .build();
                SimpleScheduleBuilder times = simpleSchedule()
                        .withIntervalInSeconds(i)
                        .repeatForever();
                Trigger trigger = newTrigger()
                        .startNow()
                        .withSchedule(times)
                        .build();
                scheduler.scheduleJob(job, trigger);
                Thread.sleep(10000);
                scheduler.shutdown();
                System.out.println(store);
            } catch (Exception se) {
                se.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            var store = (Connection) context.getJobDetail().getJobDataMap().get("store");
            try (PreparedStatement preparedStatement =
                         store.prepareStatement("insert into rabbit(created_date) values(?)")) {
                //запись в таблицу в формате timestamp without time zone
                preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                //в миллисекундах в таблицу запись- setInt(1, (int) System.currentTimeMillis());
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
