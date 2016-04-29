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

package de.tumitfahrer.services;

import org.dozer.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Mapper implements org.dozer.Mapper {

    @Autowired
    protected org.dozer.Mapper mapper;

    @Override
    public <T> T map(Object source, Class<T> destinationClass) throws MappingException {
        if (source == null) {
            // return null; // TODO: discuss
        }
        return mapper.map(source, destinationClass);
    }

    @Override
    public void map(Object source, Object destination) throws MappingException {
        mapper.map(source, destination);
    }

    @Override
    public <T> T map(Object source, Class<T> destinationClass, String mapId) throws MappingException {
        if (source == null) {
            // return null; // TODO: discuss
        }
        return mapper.map(source, destinationClass, mapId);
    }

    @Override
    public void map(Object source, Object destination, String mapId) throws MappingException {
        mapper.map(source, destination, mapId);
    }
}
