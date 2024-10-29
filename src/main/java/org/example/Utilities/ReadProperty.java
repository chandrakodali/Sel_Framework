package org.example.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperty {
    private static ReadProperty instance;
    private Properties properties;

    private ReadProperty() {
        properties = new Properties();
        try {
            // Try multiple locations for properties file
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("test.properties");
            if (inputStream == null) {
                // Try relative to project root
                inputStream = getClass().getClassLoader()
                        .getResourceAsStream("/test.properties");
            }

            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            } else {
                throw new RuntimeException("test.properties not found in classpath");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test.properties", e);
        }
    }

    public static ReadProperty getInstance() {
        if (instance == null) {
            instance = new ReadProperty();
        }
        return instance;
    }

    public String readProperties(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            System.out.println("Warning: Property '" + key + "' not found or empty in test.properties");
            return "";
        }
        return value;
    }
}