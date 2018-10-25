package org.deprecated.apachecommons;

import java.io.File;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Created by oksana_cherniavskaia on 22.10.2018.
 */
public class ConfigTest {

public static void main(String[] args) {
    Parameters params = new Parameters();
// Read data from this file
    String fName = "config.properties";

    File propertiesFile = new File(fName);

    FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
            .configure(params.fileBased().setBasePath(".")
                .setFileName(fName));

    try {
        Configuration config = builder.getConfiguration();

        System.out.println();
        // config contains all properties read from the file
    } catch (ConfigurationException e) {
        // loading of the configuration file failed
        e.printStackTrace();
    }
}

}
