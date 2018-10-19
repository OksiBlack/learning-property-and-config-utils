package org.learning.core.config;

import java.util.Optional;

public interface ConfigurationSource {

    public static final int DEFAULT_PRIORITY=1;

    ConfigurationSource FIND_NOTHING = new ConfigurationSource() {
        @Override
        public String get(String key) {
            return null;
        }

        @Override
        public String require(String key) {
            return null;
        }

        @Override
        public void reload() {

        }
    };


    String get(String key, boolean isReq);

   default String get(String key){
       return get(key, false);
   }
    default int getPriority(){
        return DEFAULT_PRIORITY;
    }
   default String  require(String key){
       return get(key, true);

   }
void reload();


    default ConfigurationSource or(ConfigurationSource source) {
        return new ConfigurationSource() {
            @Override
            public String get(String key, boolean isReq) {
                return null;
            }

            @Override
            public void reload() {

            }
        }

            (String key) -> Optional
            .ofNullable(this.get(key))

            if()
            (String key) -> {
            return Optional
                .ofNullable(this.get(key))
                .orElseGet(() -> source.get(key));
        };
    }
}



/*
  default ConfigurationSource or(ConfigurationSource source) {
        return (key) -> Optional
            .ofNullable(this.get(key))
            .orElseGet(() -> source.get(key));
    }
 */