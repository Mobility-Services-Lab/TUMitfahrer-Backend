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

package de.tumitfahrer.assembler.util;

import de.tumitfahrer.entities.Ride;
import org.dozer.CustomConverter;
import org.dozer.MappingException;

public class RideIntegerConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {

        if (source == null) {
            return null;
        }

        if (source instanceof Ride) {
            Ride ride = (Ride) source;
            return ride.getId();
        }

        if (source instanceof Integer) {
            Ride ride = new Ride();
            ride.setId((Integer) source);
            return ride;
        }

        throw new MappingException("Arguments passed in were:" + destination + " and " + source);
    }
}