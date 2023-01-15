package app.vacancies.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Черновик по работе конвертирования LocalDateTime в String
 */
public class Temp3 {

    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "01"),
            Map.entry("фев", "02"),
            Map.entry("мар", "03"),
            Map.entry("апр", "04"),
            Map.entry("май", "05"),
            Map.entry("июн", "06"),
            Map.entry("июл", "07"),
            Map.entry("авг", "08"),
            Map.entry("сен", "09"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12")
    );

    private static String correctMonth(String date) {
        for (String key : MONTHS.keySet()) {
            if (date.contains(key)) {
                return date.replace(key, MONTHS.get(key));
            }
            System.err.println("ДАТУ не нужно корректировать");

        }
        return date;
    }


    public static void main(String[] args) {
        var inputDate = "23 апр 21, 17:16";
        var yourDate = correctMonth(inputDate);
        System.out.println("yourDate = " + yourDate);

        var dtf = DateTimeFormatter.ofPattern("dd MM yy, HH:mm");

        var localDateTime = LocalDateTime.parse(yourDate, dtf);
        System.out.println("lmfl;sdmf---" + localDateTime);

        System.out.println("day = " + localDateTime.getDayOfMonth());
        System.out.println("month = " + localDateTime.getMonth());
        System.out.println("year = " + localDateTime.getYear());
        System.out.println("min = " + localDateTime.getMinute());
        System.out.println("sec = " + localDateTime.getSecond());

    }
}
