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

/**
 * Provides utility functions to estimate the distance between two lat long WGS84 coordinates
 */
public class LatLongUtil {

    /**
     * Approximately number of latitude degrees per kilometer for the area Munich/Bavaria
     */
    public static final float LATITUDE_DEGREE_PER_KILOMETER = 0.0089968f;

    /**
     * Approximately number of longitude degrees per kilometer for the area Munich/Bavaria
     */
    public static final float LONGITUDE_DEGREE_PER_KILOMETER = 0.0134315f;

    /**
     * Minimum latitude value
     * FIXME: move to properties file
     */
    public static final float LATITUDE_BOUNDS_MIN = 34.039745f;

    /**
     * Maximum latitude value
     * FIXME: move to properties file
     */
    public static final float LATITUDE_BOUNDS_MAX = 71.755312f;

    /**
     * Minimum longitude value
     * FIXME: move to properties file
     */
    public static final float LONGITUDE_BOUNDS_MIN = -15.625000f;

    /**
     * Maximum longitude value
     * FIXME: move to properties file
     */
    public static final float LONGITUDE_BOUNDS_MAX = 49.062500f;

    /**
     * Computes the lower value for a given latitude in WGS84 and a radius in kilometer
     *
     * @param lat      WGS84 representation of a latitude degree
     * @param distance Distance radius in kilometer
     * @return Estimated lower latitude degree
     */
    public static double getLowLat(double lat, double distance) {
        return lat - distance * LATITUDE_DEGREE_PER_KILOMETER;
    }

    /**
     * Computes the upper value for a given latitude in WGS84 and a radius in kilometer
     *
     * @param lat      WGS84 representation of a latitude degree
     * @param distance Distance radius in kilometer
     * @return Estimated upper latitude degree
     */
    public static double getHighLat(double lat, double distance) {
        return lat + distance * LATITUDE_DEGREE_PER_KILOMETER;
    }

    /**
     * Computes the lower value for a given longitude in WGS84 and a radius in kilometer
     *
     * @param lon      WGS84 representation of a longitude degree
     * @param distance Distance radius in kilometer
     * @return Estimated lower longitude degree
     */
    public static double getLowLong(double lon, double distance) {
        return lon - distance * LONGITUDE_DEGREE_PER_KILOMETER;
    }

    /**
     * Computes the upper value for a given longitude in WGS84 and a radius in kilometer
     *
     * @param lon      WGS84 representation of a longitude degree
     * @param distance Distance radius in kilometer
     * @return Estimated upper longitude degree
     */
    public static double getHighLong(double lon, double distance) {
        return lon + distance * LONGITUDE_DEGREE_PER_KILOMETER;
    }

    /**
     * Computes the estimated distance between two points represented as WGS84 latitude and longitude
     * degrees
     *
     * @param lat1 WGS84 representation of the first latitude degree
     * @param lng1 WGS84 representation of the first longitude degree
     * @param lat2 WGS84 representation of the second latitude degree
     * @param lng2 WGS84 representation of the second longitude degree
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (earthRadius * c) / 1000.0;  // kilometers
    }

    /**
     * Validates if the provided latitude is inside the application latitude bounds
     *
     * @param latitude The latitude to check
     */
    public static boolean isInLatitudeBounds(double latitude) {
        return LATITUDE_BOUNDS_MIN <= latitude && latitude <= LATITUDE_BOUNDS_MAX;
    }

    /**
     * Validates if the provided longitude is inside the application latitude bounds
     *
     * @param longitude The longitude to check
     */
    public static boolean isInLongitudeBounds(double longitude) {
        return LONGITUDE_BOUNDS_MIN <= longitude && longitude <= LONGITUDE_BOUNDS_MAX;
    }
}
