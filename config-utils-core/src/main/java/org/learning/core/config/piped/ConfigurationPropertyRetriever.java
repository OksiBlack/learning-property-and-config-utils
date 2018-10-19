package org.learning.core.config.piped;

import java.util.Optional;
import java.util.function.Function;

public interface ConfigurationRetriever extends Function<String, String> {

    ConfigurationRetriever FIND_NOTHING = (key) -> null;


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

    default ConfigurationRetriever or(ConfigurationRetriever source) {
        return (key) -> Optional
            .ofNullable(this.get(key))
            .orElseGet(() -> source.get(key));
    }
}