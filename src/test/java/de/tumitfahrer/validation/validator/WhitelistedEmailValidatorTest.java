package de.tumitfahrer.validation.validator;

import de.tumitfahrer.validation.annotations.WhitelistedEmail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@WebAppConfiguration
public class WhitelistedEmailValidatorTest {

    @Autowired
    private Validator validator;

    @Test
    public void testIsValid_validEmails() throws Exception {
        String[] validEmails = new String[]{
                "test@tum.de",
                "test@cs.tum.edu",
                "test@mytum.de",
                "test@foo.tum.de",
                "test@in.tum.de"
        };

        for (String email : validEmails) {
            final TestClass clazz = new TestClass();
            clazz.email = email;

            final Set<ConstraintViolation<TestClass>> violations = validator.validate(clazz);
            assertTrue("The email '" + email + "' should be valid", violations.size() == 0);
        }
    }

    @Test
    public void testIsValid_invalidEmails() throws Exception {
        String[] invalidEmails = new String[]{
                "test@gmail.com",
                "test@tim.de",
                "test@imtum.de",
                "not_an_email",
                "test@test@tum.de",
                "testin.tum.de"
        };

        for (String email : invalidEmails) {
            final TestClass clazz = new TestClass();
            clazz.email = email;

            final Set<ConstraintViolation<TestClass>> violations = validator.validate(clazz);
            assertTrue("The email '" + email + "' should not be valid", violations.size() == 1);
        }
    }

    @Test
    public void testIsValid_nullValue() throws Exception {
        final TestClass clazz = new TestClass();
        clazz.email = null;

        final Set<ConstraintViolation<TestClass>> violations = validator.validate(clazz);
        assertTrue("Null should not be a valid email", violations.size() == 1);
    }

    private class TestClass {
        @WhitelistedEmail
        String email;
    }
}