package org.learning.core.config.fluent;

import java.util.Optional;
import java.util.function.Function;

public interface ConfigurationPropertyRetriever extends Function<String, String> {

    ConfigurationPropertyRetriever FIND_NOTHING = (key) -> null;


    String get(String key);

    /**
     *
     * @param s the function argument
     * @return the function result
     */
    @Override
   default String apply(String s){
        return get(s);
    }

    default ConfigurationPropertyRetriever or(ConfigurationPropertyRetriever source) {
        return (key) -> Optional
            .ofNullable(this.get(key))
            .orElseGet(() -> source.get(key));
    }
}