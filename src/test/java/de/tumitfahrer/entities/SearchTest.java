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

package de.tumitfahrer.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class SearchTest {

    private Search search;

    @Before
    public void setUp() throws Exception {
        search = new Search();

        final Calendar cal = Calendar.getInstance();
        cal.set(2015, Calendar.OCTOBER, 1, 14, 34);
        search.setDepartureTime(cal.getTime());

        search.setDepartureTimeOffsetBefore(0);
        search.setDepartureTimeOffsetAfter(0);
    }

    @Test
    public void testGetEarliestDepartureTime_zeroOffset() throws Exception {
        Date earliestDate = search.getEarliestDepartureTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(earliestDate);
        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, cal.get(Calendar.MINUTE));
    }

    @Test
    public void testGetEarliestDepartureTime_customOffset() throws Exception {
        final int beforeMin = 12;
        search.setDepartureTimeOffsetBefore(beforeMin);

        Date earliestDate = search.getEarliestDepartureTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(earliestDate);

        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(22, cal.get(Calendar.MINUTE));
    }

    @Test
    public void testGetLatestDepartureTime_zeroOffset() throws Exception {
        Date latestDate = search.getLatestDepartureTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(latestDate);

        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, cal.get(Calendar.MINUTE));
    }

    @Test
    public void testGetLatestDepartureTime_customOffset() throws Exception {
        final int beforeMin = 61;
        search.setDepartureTimeOffsetAfter(beforeMin);

        Date latestDate = search.getLatestDepartureTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(latestDate);

        assertEquals(2015, cal.get(Calendar.YEAR));
        assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(35, cal.get(Calendar.MINUTE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEarliestDepartureTime_nullOffset() throws Exception {
        search.setDepartureTimeOffsetBefore(-1);
        search.getEarliestDepartureTime();

        search.setDepartureTimeOffsetBefore(null);
        search.getEarliestDepartureTime();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLatestDepartureTime_nullOffset() throws Exception {
        search.setDepartureTimeOffsetAfter(-1);
        search.getLatestDepartureTime();

        search.setDepartureTimeOffsetAfter(null);
        search.getLatestDepartureTime();
    }
}
