package ru.stonlex.api.java.utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("All")
public final class DateUtil {

    /**
     * Опять же, этот код старый, и переписывать его мне было
     * попросту лень, да и тем более, он прекрасно работает.
     *
     * Если кому-то он неудобен, то система как бы не особо сложная,
     * поэтому можно и самому ее написать
     */

    public static String getDate(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String getDate() {
        return getDate("dd/MM/yyyy");
    }

    public static String getDateWithTime() {
        return getDate("dd/MM/yyyy HH:mm:ss");
    }

    public static Timestamp getTimestamp(long l) {
        return new Timestamp(l);
    }

    public static long fromTimestamp(Timestamp timestamp) {
        return timestamp.getTime();
    }

}
