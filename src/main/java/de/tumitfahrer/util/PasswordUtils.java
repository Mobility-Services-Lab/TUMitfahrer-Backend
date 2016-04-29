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

import de.tumitfahrer.entities.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    private static int PASSWORD_LENGTH = 8;
    private static int SALT_LENGTH = 8;

    public static boolean isAuthenticated(User user, String tryPassword) {
        if (user.getPassword() == null || user.getPassword().length() == 0 || user.getSalt() == null || user.getSalt().length() == 0) {
            return false;
        }
        String saltedPassword = tryPassword + user.getSalt();
        String hashedPassword = sha512(saltedPassword);

        return hashedPassword.equals(user.getPassword());
    }

    public static String sha512(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] sha1hash;
            md.update(text.getBytes("UTF-8"), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "false"; // return any non-sha512 string
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * @return an 8-character long random Password
     */
    public static String generatePassword() {
        String password = RandomStringUtils.randomAlphanumeric(PASSWORD_LENGTH);
        return password.replace("I", "i").replace("l", "L");
    }

    /**
     * @return an 8-character long random salt
     */
    public static String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(SALT_LENGTH);
    }

}
