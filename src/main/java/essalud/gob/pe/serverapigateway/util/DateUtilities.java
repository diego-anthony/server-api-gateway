package essalud.gob.pe.serverapigateway.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class DateUtilities {

    public static Date currentDate() {
        ZoneId zoneId = ZoneId.of("America/Lima");
        ZonedDateTime zoneDateTime = ZonedDateTime.now(zoneId);
        return Date.from(zoneDateTime.toInstant());
    }

    public static String formatPart(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static int calculateAge(Date pBirthDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = pBirthDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        if (birthDate != null) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    public static String format(Date date, String format) {
        if (date == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static Date stringDateToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.parse(dateString);
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    public static long dateDiffInSeconds(Date first, Date second) {
        return (first.getTime() - second.getTime()) / 1000;
    }

    public static long dateDiffInDays(Date minDate, Date maxDate) {
        long difference = maxDate.getTime() - minDate.getTime();
        long daysBetween = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
        return daysBetween;
    }

    public static Date clearTime(Date date) {
        return new Date(date.getYear(),date.getMonth(),date.getDate());
    }

}
