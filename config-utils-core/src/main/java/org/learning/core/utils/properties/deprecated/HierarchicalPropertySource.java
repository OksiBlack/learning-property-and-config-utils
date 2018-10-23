package org.learning.core.utils.properties.deprecated;

import static org.learning.core.properties.util.PropertiesSource.EMPTY_SOURCE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import java.util.Properties;
import java.util.stream.Collectors;
import org.learning.core.utils.properties.IPropertiesSource;

/**
 * Created by oksana_cherniavskaia on 19.10.2018.
 */
public class HierarchicalPropertySource extends AbstractPropertiesSource {

    List<IPropertiesSource> sources = new ArrayList<>();

    public HierarchicalPropertySource(IPropertiesSource... propertiesSources) {
        super();
        List<IPropertiesSource> list =
            new ArrayList<>(Arrays.asList(propertiesSources)).stream().sorted((Comparator.comparing((IPropertiesSource ps)->ps.getPriority()))).collect(Collectors.toList());
        this.sources = list;

        //higher priority sources in the end, properties will be rewritten if already inserted

        IPropertiesSource reduce = list.stream()
            .reduce(new HierarchicalPropertySource(), (ps1, ps2) -> {
                ps1.getProperties()
                    .putAll(ps2.getProperties());
                return ps1;
            });

        super.properties= reduce.getProperties();
    }

    public HierarchicalPropertySource(Properties properties) {
        super(properties);
    }
}
