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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

@Component("PropertyMapper")
public class PropertyMapper {

    @Autowired
    Properties deLng;
    @Autowired
    Properties enLng;
    Properties properties;

    public HashMap<String, Object> startWith(String qualifier, String startWith) {
        return startWith(qualifier, startWith, false);
    }

    public HashMap<String, Object> startWith(String qualifier, String startWith, boolean removeStartWith) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        setProperties(qualifier);

        if (properties != null) {
            for (Entry<Object, Object> e : properties.entrySet()) {
                Object oKey = e.getKey();
                if (oKey instanceof String) {
                    String key = (String) oKey;
                    if (((String) oKey).startsWith(startWith)) {
                        if (removeStartWith)
                            key = key.substring(startWith.length());
                        result.put(key, e.getValue());
                    }
                }
            }
        }
        return result;
    }

    public void setProperties(String qualifier) {
        if (qualifier.equals("deLng")) {
            this.properties = deLng;
            return;
        }
        this.properties = enLng;
    }
}
