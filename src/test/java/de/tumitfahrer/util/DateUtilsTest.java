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

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void testConvertDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2015, Calendar.FEBRUARY, 3, 16, 18, 2);

        String convertedDate = DateUtils.convertDate(cal.getTime());
        Assert.assertEquals("2015-02-03 16:18", convertedDate);
    }

    @Test(expected=NullPointerException.class)
    public void testConvertDate_nullDate() throws Exception {
        String convertedDate = DateUtils.convertDate(null);
    }
}