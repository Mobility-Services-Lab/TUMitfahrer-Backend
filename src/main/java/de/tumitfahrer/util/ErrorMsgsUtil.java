package de.tumitfahrer.util;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Properties;

@Service
public class ErrorMsgsUtil {

    @Resource(name = "validationMessages")
    private Properties properties;

    public String get(final String key) {
        return properties.getProperty(key);
    }
}
