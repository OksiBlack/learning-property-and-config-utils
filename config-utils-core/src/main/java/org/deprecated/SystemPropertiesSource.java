package org.deprecated;

/**
 * Created by oksana_cherniavskaia on 22.10.2018.
 */
public class SystemPropertiesSource extends AbstractPropertiesSource {

public SystemPropertiesSource() {
    super(System.getProperties());
}
}
