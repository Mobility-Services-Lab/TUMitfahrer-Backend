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

import de.tumitfahrer.enums.Language;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class LanguageUtils {

    @Value("#{PropertyMapper.startWith('deLng', 'de.')}")
    private HashMap<String, String> de;
    @Value("#{PropertyMapper.startWith('enLng', 'en.')}")
    private HashMap<String, String> en;

    public String getTranslation(Language lng, String key) {
        key = lng.toString().toLowerCase() + "." + key;

        switch (lng) {
            case DE:
                return de.get(key);
            case EN:
            default:
                return en.get(key);
        }
    }
}
