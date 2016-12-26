package eu.greitai.paradox.mobilesecurity.helpers;

import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {

    public static Calendar getStarOfDayInUtc() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}
