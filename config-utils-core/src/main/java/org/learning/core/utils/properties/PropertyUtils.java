package org.learning.core.propsonly;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public class PropertyUtils {
  public static final  String LIST_SEPARATOR = ",";


    private Properties properties;
    private IPropertiesSource source;

    public PropertyUtils(IPropertiesSource source) {
        this.source = source;
        this.properties = source.get();
    }

    /**
     * Return the property value associated with the given key or the defaultValue if the key cannot be resolved.
     */
    public String get(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }

    /**
     * Return the property value associated with the given key.
     */
    public Optional<String> get(String key) {

        final Object value = properties.get(key);
        return value == null ? Optional.empty() : Optional.ofNullable(String.valueOf(value));

    }

    /**
     * Return the property value associated with the given key and apply the map function to it.
     */
    public <T> Optional<T> get(String key, Function<String, T> map) {
        return get(key).map(map);
    }

    /**
     * Return the property value associated with the given key and apply the map function to it. The defaultValue if the key cannot be
     * resolved.
     */
    public <T> T get(String key, T defaultValue, Function<String, T> map) {
        return get(key).map(map).orElse(defaultValue);
    }

    /**
     * Return the property value associated with the given key or the defaultValue if the key cannot be resolved.
     */
    public int getInt(String key, int defaultValue) {
        final Optional<Integer> optional = getInt(key);
        if (optional.isPresent()) {
            return optional.get();
        }
        return defaultValue;
    }

    /**
     * Return the property value associated with the given key.
     */
    public Optional<Integer> getInt(String key) {
        return get(key).map(Integer::parseInt);
    }

    /**
     * Return the property value associated with the given key or the defaultValue if the key cannot be resolved.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        final Optional<Boolean> optional = getBoolean(key);
        if (optional.isPresent()) {
            return optional.get();
        }
        return defaultValue;
    }

    /**
     * Return the property value associated with the given key.
     */
    public Optional<Boolean> getBoolean(String key) {
        return get(key).map(value -> {
            if ("true".equalsIgnoreCase(value)||"1".equals(value)) {
                return true;
            } else if ("false".equalsIgnoreCase(value)||"0".equals(value)) {
                return false;
            } else {
                throw new RuntimeException("Cannot parse boolean value: [" + value + "]");
            }
        });
    }

    /**
     * Return the property value associated with the given key or the defaultValue if the key cannot be resolved.
     */
    public double getDouble(String key, double defaultValue) {
        final Optional<Double> optional = getDouble(key);
        if (optional.isPresent()) {
            return optional.get();
        }
        return defaultValue;
    }

    /**
     * Return the property value associated with the given key.
     */
    public Optional<Double> getDouble(String key) {
        return get(key).map(Double::parseDouble);
    }

    /**
     * Return the property value associated with the given key or the defaultValue if the key cannot be resolved.
     */
    public float getFloat(String key, float defaultValue) {
        final Optional<Float> optional = getFloat(key);
        if (optional.isPresent()) {
            return optional.get();
        }
        return defaultValue;
    }

    /**
     * Return the property value associated with the given key.
     */
    public Optional<Float> getFloat(String key) {
        return get(key).map(Float::parseFloat);
    }

    /**
     * Return the property value associated with the given key or the defaultValue if the key cannot be resolved.
     */
    public long getLong(String key, long defaultValue) {
        final Optional<Long> optional = getLong(key);
        if (optional.isPresent()) {
            return optional.get();
        }
        return defaultValue;
    }

    /**
     * Return the property value associated with the given key.
     */
    public Optional<Long> getLong(String key) {
        return get(key).map(Long::parseLong);
    }

    /**
     * Return the property value associated with the given key or the defaultValue if the key cannot be resolved.
     */
    public <T extends Enum<T>> T getEnum(String key, T defaultValue) {
        final Optional<T> optional = getEnum(key, (Class<T>) defaultValue.getClass());
        if (optional.isPresent()) {
            return optional.get();
        }
        return defaultValue;
    }

    /**
     * Return the property value associated with the given key.
     */
    public <T extends Enum<T>> Optional<T> getEnum(String key, Class<T> type) {
        return get(key).map(value -> Enum.valueOf(type, value));
    }

    /**
     * Return the property value associated with the given key split with the default separator.
     */
    public List<String> getList(String key) {
        return Arrays.asList(getArray(key));
    }

    /**
     * Return the property value associated with the given key split with the default separator.
     */
    public String[] getArray(String key) {
        return getArray(key, LIST_SEPARATOR);
    }

    /**
     * Return the property value associated with the given key split with the specific separator.
     */
    public String[] getArray(String key, String separator) {
        return get(key).map(value -> value.split(separator))
            .orElse(new String[0]);
    }

    /**
     * Return the property value associated with the given key split with the default separator and apply the map function to elements in
     * the resulting list.
     */
    public <T> List<T> getList(String key, Function<String, T> map) {
        return getList(key, LIST_SEPARATOR, map);
    }

    /**
     * Return the property value associated with the given key split with the specific separator and apply the map function to elements in
     * the resulting list.
     */
    public <T> List<T> getList(String key, String separator, Function<String, T> map) {
        return getList(key, separator).stream()
            .map(map)
            .collect(Collectors.toList());
    }

    /**
     * Return the property value associated with the given key split with the specific separator.
     */
    public List<String> getList(String key, String separator) {
        return Arrays.asList(getArray(key, separator));
    }
}
