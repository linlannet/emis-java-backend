/*
 * emis-support - support支撑模块
 * Copyright © 2020-2025 Linlan (open@linlan.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.linlan.emis.support.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static java.math.BigInteger.ZERO;

/**
 * 时间工具类
 *
 * @author Linlan
 */
public class DateUtils {

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static LocalTime dateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    public static Date localDateTimeToLocalDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date localDateToLocalDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date localTimeToLocalDate(LocalTime localTime, LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime stringToLocalDateTime(String gmt) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.US);
        Instant instant = formatter.parse(gmt, new ParsePosition(0)).toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * get the now of string
     * @return the current time of String
     */
    public static String getNowString() {
        Calendar calendar = Calendar.getInstance();
        /** The buffer. */
        StringBuffer buffer = new StringBuffer();
        buffer.delete(0, buffer.capacity());
        buffer.append(calendar.get(Calendar.YEAR));

        if (calendar.get(Calendar.MONTH) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(calendar.get(Calendar.MONTH));

        if (calendar.get(Calendar.DATE) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(calendar.get(Calendar.DATE));
        if (calendar.get(Calendar.HOUR) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(calendar.get(Calendar.HOUR));
        if (calendar.get(Calendar.MINUTE) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(calendar.get(Calendar.MINUTE));
        if (calendar.get(Calendar.SECOND) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(calendar.get(Calendar.SECOND));
        return buffer.toString();
    }

}
