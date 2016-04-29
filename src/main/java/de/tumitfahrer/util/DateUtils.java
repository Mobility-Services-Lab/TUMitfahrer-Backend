/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tumitfahrer.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    public static String SIMPLE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
    private static String STANDARD_DATE = "1970-01-01 00:00";


    public static Date getCurrentDate() {
        return new Date(Calendar.getInstance().getTimeInMillis());
    }

    public static Date getCurrentDatePlusOffsetInHours(Integer offsetInHours) {
        long date = Calendar.getInstance().getTimeInMillis();
        date += offsetInHours * 60 /* min */ * 60 /* sec */ * 1000 /* milisec */;
        return new Date(date);
    }

    public static Date getStandardDate() {
        return parseDatetime(STANDARD_DATE);
    }

    public static Date parseDate(String dateString) {
        return parse(dateString, SIMPLE_DATE_FORMAT);
    }

    public static Date parseDatetime(String dateString) {
        return parse(dateString, SIMPLE_DATETIME_FORMAT);
    }

    private static Date parse(String dateString, String format) {
        if (dateString == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(format);
        try {
            return new Date(df.parse(dateString).getTime());
        } catch (ParseException e) {
            /* Fallback strategies */
            try {
                df = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
                return new Date(df.parse(dateString).getTime());
            } catch (ParseException e1) {
                try {
                    df = new SimpleDateFormat(SIMPLE_DATETIME_FORMAT);
                    return new Date(df.parse(dateString).getTime());
                } catch (ParseException e2) {
                    // if everything fails, return null
                    return null;
                }
            }
        }
    }

    public static Date parseQueryString(String dateString) {
        if (dateString == null) {
            return getStandardDate();
        }

        dateString = dateString.replaceAll("'", "");
        return parseDatetime(dateString);
    }

    public static String convertDate(java.util.Date date) {
        DateFormat df = new SimpleDateFormat(SIMPLE_DATETIME_FORMAT);
        return df.format(date);
    }

    /**
     * Returns a string representation of the current date
     *
     * @return Current date as yyyy-MM-dd HH:mm string
     */
    public static String convertCurrentDate() {
        return convertDate(getCurrentDate());
    }
}
