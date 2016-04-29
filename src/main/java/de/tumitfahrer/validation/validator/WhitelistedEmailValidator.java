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

package de.tumitfahrer.validation.validator;

import de.tumitfahrer.validation.annotations.WhitelistedEmail;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WhitelistedEmailValidator implements ConstraintValidator<WhitelistedEmail, String> {

    @Value("${valid.mails}")
    private String validMails;

    @Override
    public void initialize(WhitelistedEmail whitelistedEmail) {
    }

    @Override
    public boolean isValid(String emailField, ConstraintValidatorContext cxt) {

        if (emailField == null) {
            return false;
        }

        String[] validMailsArray = validMails.split(", ");
        StringBuilder regexp = new StringBuilder();
        regexp.append("(");
        for (int i = 0; i < validMailsArray.length; i++) {

            // This replacement is done to allow the usage of the asterisk * symbol in the properties configuration
            // without resorting to regex expression which would be returned via error message in the API
            String part = validMailsArray[i].replaceAll("\\*", "[a-z0-9]+").replaceAll("\\.", "[.]");

            regexp.append("^[A-Z0-9._-]+@" + part + "$");
            if ((i + 1) < validMailsArray.length) {
                regexp.append("|");
            }
        }
        regexp.append(")");

        Pattern pattern = Pattern.compile(regexp.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailField);
        boolean isValid = matcher.find();

        if (!isValid) {
            cxt.disableDefaultConstraintViolation();

            // FIXME: should use the template {de.tumitfahrer.validation.WhitelistedEmail.message} for error message
            cxt.buildConstraintViolationWithTemplate("Not a valid mail address. Use one of these: " + validMails)
                    .addConstraintViolation();
        }

        return isValid;
    }

}
