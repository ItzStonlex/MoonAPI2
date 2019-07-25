package ru.stonlex.api.java.utility;

import java.util.Map;
import java.util.Random;

public final class IntegerUtil {

    /**
     * Опять же, этот код старый, и переписывать его мне было
     * попросту лень, да и тем более, он прекрасно работает.
     *
     * Если кому-то он неудобен, то система как бы не особо сложная, 
     * поэтому можно и самому ее написать
     */

    private static final Random random = new Random();

    public static String spaced(int i) {
        return spaced(i, ",");
    }

    public static String spaced(int i, String symbol) {
        final String integer = String.valueOf(i);
        final StringBuilder builder = new StringBuilder();

        for (int a = 0; a < integer.length(); a++) {
            builder.append(integer.split("")[a]);
            if ((integer.length() - a + 2) % 3 != 0) {
                continue;
            }
            builder.append(symbol);
        }
        final String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }

    public static int toRadians(int i, int radian) {
        for (int count = 0; i < radian; i++) {
            i *= i;
        }
        return i;
    }

    public static int random(int min, int max) {
        return min + random.nextInt(max - min);
    }

    public static <T> T getRandom(Map.Entry<Integer, T>[] pairs) {
        final Map.Entry<Integer, T> one = pairs[random.nextInt(pairs.length - 1)],
                two = pairs[random.nextInt(pairs.length - 1)];
        if (one.equals(two)) {
            return one.getValue();
        }
        final int randomPercent = random.nextInt(100);

        if (one.getKey() > two.getKey()) {
            if (randomPercent >= one.getKey() && (randomPercent <= two.getKey())) {
                return two.getValue();
            } else {
                return one.getValue();
            }
        } else if (one.getKey() < two.getKey()) {
            if (randomPercent >= two.getKey() && (randomPercent <= one.getKey())) {
                return one.getValue();
            } else {
                return two.getValue();
            }
        }
        return null;
    }

    public static String formatting(int i, String one, String two, String three) {
        if (i % 100 > 10 && i % 100 < 15) {
            return i + " " + three;
        }
        switch (i % 10) {
            case 1: {
                return i + " " + one;
            }
            case 2:
            case 3:
            case 4: {
                return i + " " + two;
            }
            default: {
                return i + " " + three;
            }
        }
    }

    public static String formatting(int i, TimeUnit unit) {
        return formatting(i, unit.getOne(), unit.getTwo(), unit.getThree());
    }

    public static String getTime(int seconds) {
        int minutes = 0;
        int hours = 0;
        int days = 0;
        int weeks = 0;
        int months = 0;
        int years = 0;
        if (seconds >= 60) {
            final int i = seconds / 60;
            seconds -= 60 * i;
            minutes += i;
        }
        if (minutes >= 60) {
            final int i = minutes / 60;
            minutes -= 60 * i;
            hours += i;
        }
        if (hours >= 24) {
            int i = hours / 24;
            hours -= 24 * i;
            days += i;
        }
        if (days >= 7) {
            int i = days / 7;
            days -= 7 * i;
            weeks += i;
        }
        if (weeks >= 4) {
            int i = weeks / 4;
            weeks -= 4 * i;
            months += i;
        }
        if (months >= 12) {
            final int i = months / 12;
            months -= 12 * i;
            years += i;
        }
        final StringBuilder builder = new StringBuilder();
        if (years != 0) {
            builder.append(formatting(years, TimeUnit.YEARS)).append(" ");
        }
        if (months != 0) {
            builder.append(formatting(months, TimeUnit.MONTHS)).append(" ");
        }
        if (weeks != 0) {
            builder.append(formatting(weeks, TimeUnit.WEEKS)).append(" ");
        }
        if (days != 0) {
            builder.append(formatting(days, TimeUnit.DAYS)).append(" ");
        }
        if (hours != 0) {
            builder.append(formatting(hours, TimeUnit.HOURS)).append(" ");
        }
        if (minutes != 0) {
            builder.append(formatting(minutes, TimeUnit.MINUTES)).append(" ");
        }
        if (seconds != 0) {
            builder.append(formatting(seconds, TimeUnit.SECONDS));
        }
        return builder.toString();
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isLong(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isByte(String string) {
        try {
            Byte.parseByte(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isShort(String string) {
        try {
            Short.parseShort(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public enum TimeUnit {
        SECONDS("секунда", "секунды", "секунд"),
        MINUTES("минута", "минуты", "минут"),
        HOURS("час", "часа", "часов"),
        DAYS("день", "дня", "дней"),
        WEEKS("неделя", "недели", "недель"),
        MONTHS("месяц", "месяца", "месяцев"),
        YEARS("год", "года", "лет");

        private String one;
        private String two;
        private String three;

        TimeUnit(final String one, final String two, final String three) {
            this.one = one;
            this.two = two;
            this.three = three;
        }

        public String getOne() {
            return this.one;
        }

        public String getTwo() {
            return this.two;
        }

        public String getThree() {
            return this.three;
        }
    }

}
