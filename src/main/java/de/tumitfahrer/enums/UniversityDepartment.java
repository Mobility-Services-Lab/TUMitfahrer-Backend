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

package de.tumitfahrer.enums;


import javax.xml.bind.annotation.XmlEnum;
import java.util.stream.Stream;

@XmlEnum(String.class)
public enum UniversityDepartment {
    // from http://www.tum.de/en/research/departments/ retrieved at 20.11.2015

    UNKNOWN("N/A"),
    ARCHITECTURE("Architecture"),
    CHEMISTRY("Chemistry"),
    CIVIL_GEO("Civil, Geo and Environmental Engineering"),
    ELECTRICAL("Electrical and Computer Engineering"),
    INFORMATICS("Informatics"),
    LIFE_FOOD("TUM School of Life Sciences Weihenstephan"),
    MATHEMATICS("Mathematics"),
    MECHANICAL("Mechanical Engineering"),
    MEDICINE("TUM School of Medicine"),
    PHYSICS("Physics"),
    SPORT_AND_HEALTH("Sport and Health Science"),
    TUM_SCHOOL_OF_EDUCATION("TUM School of Education"),
    TUM_SCHOOL_OF_MANAGEMENT("TUM School of Management");

    private final String friendlyName;

    UniversityDepartment(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public static String[] names() {
        return Stream.of(UniversityDepartment.values()).map(UniversityDepartment::name).toArray(String[]::new);
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
