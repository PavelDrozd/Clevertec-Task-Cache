package ru.clevertec.config;


import org.yaml.snakeyaml.Yaml;
import ru.clevertec.exception.service.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This enum used as util for get properties from yaml configuration file.
 * Enum values are available globally, and used as a singleton.
 */
public enum ConfigurationYamlManager {
    INSTANCE;

    /** application.yml stores information about project configurations. */
    private static final String CONFIG_YAML_FILE = "/application.yml";
    /** This map stores all yaml properties in application. */
    private final Map<?, ?> property;

    /**
     * Constructor open InputStream for read yaml configuration file, initialize Yaml object from snakeYaml dependency
     * and load all properties in Map.
     */
    ConfigurationYamlManager() {
        try (InputStream is = getClass().getResourceAsStream(CONFIG_YAML_FILE)) {
            Yaml yaml = new Yaml();
            property = yaml.load(is);
        } catch (IOException e) {
            throw new NotFoundException("Can't find yaml configuration file in path: " + CONFIG_YAML_FILE);
        }
    }

    /**
     * Method allows to get property from application.yml file.
     *
     * @param key expected String tag of property in yaml.
     * @return String value of property.
     */
    public String getProperty(String key) {
        return processYaml(key);
    }

    private String processYaml(String key) {
        String[] keyParts = key.split("\\.");
        if (keyParts.length == 1 || keyParts[1].isBlank() || keyParts[1].isEmpty()) {
            return property.get(keyParts[0]).toString();
        } else {
            return processMap(keyParts);
        }
    }

    private String processMap(String[] keyParts) {
        String result = "";
        Map<?, ?> map = property;
        for (int i = 0; i < keyParts.length; i++) {
            if (i == keyParts.length - 1) {
                return map.get(keyParts[i]).toString();
            }
            map = processMapParts(keyParts[i], map);
        }
        return result;
    }

    private Map<?, ?> processMapParts(String part, Map<?, ?> map) {
        return (Map<?, ?>) map.get(part);
    }
}
