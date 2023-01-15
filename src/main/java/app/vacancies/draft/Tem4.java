package app.vacancies.draft;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * Черновик работы с LocalDateTime
 */
public class Tem4 {
    public static void main(String[] args) {
// Шаблон DateTimeFormater static method . ofPattern - создает шаблон для дальнейшего конвертирования LocalDateTime
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yy HH:mm");
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd MM yy");
        // в шаблоне нет времени т.к. в LocalDate нет времени

        //создание объекат LocalDateTime через метод .of() не модифицируем/конечен
        LocalDateTime localDateTime1 = LocalDateTime.of(21, Month.APRIL, 23, 17, 16);
        System.out.println(localDateTime1);
        String dateInString = "2 апр 21, 17:16";
        //format() преобразует экземпляр LocalDate по шаблону в Стринг
        String localDateTime = localDateTime1.format(dtf);
        System.out.println(localDateTime);
        LocalDateTime localDateTime2 = LocalDateTime.parse(localDateTime, dtf);

        // LocalDateTime localDateTime2 = LocalDateTime.parse(dateInString, dtf);
        //System.out.println(localDateTime2);
        String[] strarr = dateInString.split(" ");
        String[] fot = strarr[2].split(",");
        // String itog = strarr[0] + " " + strarr[1] + "." + " " + fot[0] + " " + strarr[3];
        String itog = "сегодня"; // если входящая строка содержит фразу за место даты(сегодня/вчера)
        //сегодняшняя дата
        LocalDate localDate = LocalDate.now();
        // через ряд методов LocalDate можно менять дату
        LocalDate localDate1 = (LocalDate.now()).minusDays(1);
        // перевели в формат Стринг метод .format() - Localdate
        String str = localDate.format(dtf1);
        // сконструировали новую Стринг строку /учили параметр дата для LocalDateTime
        String str1 = str + " " + "18:15";
        //static метод .parse() LocalDateTime работает со стринг + шабллон DateTimeFormatter
        LocalDateTime localDateTime3 = LocalDateTime.parse(str1, dtf);
        System.out.println(localDateTime3);
    }
}
