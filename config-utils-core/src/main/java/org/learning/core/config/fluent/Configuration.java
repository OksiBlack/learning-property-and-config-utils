package org.learning.core.config.fluent;


import static org.learning.core.config.fluent.ConfigurationPropertyRetriever.FIND_NOTHING;

import co.unruly.config.SecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.learning.core.config.fluent.exceptions.ConfigurationPropertyNotFoundException;

public class Configuration {

    public static final Logger logger = LogManager.getLogger();
    private final ConfigurationPropertyRetriever func;

    public Configuration() {
        this(FIND_NOTHING);
    }

    public Configuration(ConfigurationPropertyRetriever map) {
        this.func = map;
    }

    public static Configuration from(ConfigurationPropertyRetriever func) {
        return new Configuration(func);
    }

    public static Configuration of(ConfigurationPropertyRetriever... sources) {
        return new Configuration(Stream.of(sources)
            .reduce(FIND_NOTHING, ConfigurationPropertyRetriever::or));
    }

    public static ConfigurationPropertyRetriever map(Map<String, String> map) {
        return map::get;
    }

    public static ConfigurationPropertyRetriever properties(String s) {
        Properties properties = new Properties();

        try {
            properties.load(new FileReader(s));
        } catch (IOException e) {
            logger.error(e);
        }

        return properties::getProperty;
    }

    public static ConfigurationPropertyRetriever properties(Properties properties) {
        return properties::getProperty;
    }

    public static ConfigurationPropertyRetriever systemProperties() {
        return System::getProperty;
    }

    public static ConfigurationPropertyRetriever environment() {
        return (key) -> System.getenv(key.toUpperCase());
    }

    public static ConfigurationPropertyRetriever secretsManager(String secretName, String region) {
        return new SecretsManager(secretName, region)::get;
    }

    public static ConfigurationPropertyRetriever secretsManager(String secretName, String region, AWSSecretsManager client) {
        return new SecretsManager(secretName, region, client)::get;
    }

    public String get(String s, String defaultValue) {
        return get(s).orElse(defaultValue);
    }

    public Optional<String> get(String s) {
        return Optional.ofNullable(func.get(s));
    }

    public String require(String s) {
        return get(s).orElseThrow(() -> new ConfigurationPropertyNotFoundException(s));
    }

    public Configuration or(ConfigurationPropertyRetriever next) {
        return new Configuration(this.func.or(next));
    }
}

