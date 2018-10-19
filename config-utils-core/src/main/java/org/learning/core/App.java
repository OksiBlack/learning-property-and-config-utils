package org.learning.core;



import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        PropertiesConfiguration configuration;

      //  Configuration.of()
        System.out.println("Hello World!");
    }

    public interface ConfigurationSource  {



        String get(String key);

        default ConfigurationSource or(ConfigurationSource source) {
            ConfigurationSource configurationSource = ((key) ->{
              return   Optional
                .ofNullable(this.get(key))
                .orElseGet(() -> source.get(key));

            });
            return configurationSource;
        }
    }

}
