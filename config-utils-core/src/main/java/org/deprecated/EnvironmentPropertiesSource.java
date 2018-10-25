package org.deprecated;

/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public abstract class EnvironmentPropertiesSource extends AbstractPropertiesSource {

public EnvironmentPropertiesSource() {
    super(System.getenv());
}

}