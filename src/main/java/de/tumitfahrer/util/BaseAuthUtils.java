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

import org.glassfish.jersey.internal.util.Base64;

import java.nio.charset.Charset;

public class BaseAuthUtils {

    public static String getUsername(String baseAuth) {
        String[] encodedString = getEncodedString(baseAuth);
        if (encodedString.length == 0) {
            return null;
        }
        return encodedString[0];
    }

    public static String getPassword(String baseAuth) {
        String[] encodedString = getEncodedString(baseAuth);
        if (encodedString.length <= 1) {
            return null;
        }
        return encodedString[1];
    }

    private static String[] getEncodedString(String baseAuth) {
        String base64Credentials = baseAuth.substring("Basic".length()).trim();
        String credentials = new String(Base64.decode(base64Credentials.getBytes()), Charset.forName("UTF-8"));
        return credentials.split(":", 2);
    }

}
