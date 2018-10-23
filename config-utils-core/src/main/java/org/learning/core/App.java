package org.learning.core;


import java.io.File;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.combined.CombinedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;

/**
 * Hello world!
 */
public class App {

public static void main(String[] args) throws ConfigurationException {
    Parameters params = new Parameters();
    CombinedConfigurationBuilder builder = new CombinedConfigurationBuilder()
        .configure(params.fileBased()
            .setFile(new File("./config.properties")));
    CombinedConfiguration cc = builder.getConfiguration();

    PropertiesConfiguration configuration;

    //  Configuration.of()
    System.out.println("Hello World!");
}

public interface ConfigurationSource {


    default ConfigurationSource or(ConfigurationSource source) {
        ConfigurationSource configurationSource = ((key) -> {
            return Optional
                .ofNullable(this.get(key))
                .orElseGet(() -> source.get(key));

        });
        return configurationSource;
    }

    String get(String key);
}

}
