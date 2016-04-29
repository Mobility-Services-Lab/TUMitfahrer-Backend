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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class BaseAuthUtilsTest {

    @Test
    public void testGetUsername() throws Exception {
        final String baseAuth = "Basic dGVzdEBteXR1bS5kZTpwYXNzd29ZA==";
        final String username = BaseAuthUtils.getUsername(baseAuth);
        assertEquals("test@mytum.de", username);
    }

    @Test
    public void testGetPassword() throws Exception {
        final String baseAuth = "Basic dGVzdEBteXR1bS5kZTpwYXNzd29yZA==";
        final String username = BaseAuthUtils.getPassword(baseAuth);
        assertEquals("password", username);
    }

    // FIXME: not implemented yet
    @Test(expected = InvalidParameterException.class) @Ignore
    public void testGetUsername_missingBasic() throws Exception {
        final String invalidBaseAuth = "dGVzdEBteXR1bS5kZTpwYXNzd29ZA==";
        BaseAuthUtils.getUsername(invalidBaseAuth);
    }
}