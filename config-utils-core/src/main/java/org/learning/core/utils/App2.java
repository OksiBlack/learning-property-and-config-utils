package org.learning.core.utils;



import java.nio.file.FileSystems;
import java.util.Optional;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedBuilderProperties;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.BasePathLocationStrategy;



/**
 * Hello world!
 */
public class App2 {

    public static void main(String[] args) throws ConfigurationException {

        PropertiesBuilderParameters parameters = new Parameters().properties();
        parameters.setFileName("./config.properties");
       // BasePathLocationStrategy strategy = new BasePathLocationStrategy();
  //      strategy.locate(FileSystems.getDefault(), )
    //    parameters.setLocationStrategy(strategy);
        FileBasedBuilderProperties properties;
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(parameters);

        builder.getConfiguration();

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
