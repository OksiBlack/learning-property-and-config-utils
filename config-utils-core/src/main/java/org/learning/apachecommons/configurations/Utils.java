package org.learning.apachecommons.configurations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;

/**
 * Created by oksana_cherniavskaia on 22.10.2018.
 */
public class Utils {


/**
 * Constant for the name of the clone() method.
 */
private static final String METHOD_CLONE = "clone";

/**
 * Helper method for converting the type of the {@code Properties} object
 * to a supported map type. As stated by the comment of the constructor,
 * we expect the {@code Properties} object to contain only String key;
 * therefore, it is safe to do this cast.
 *
 * @param props the {@code Properties} to be copied
 * @return a newly created map with all string keys of the properties
 */
@SuppressWarnings("unchecked")
public static Map<String, Object> convertPropertiesToMap(Properties props) {
    @SuppressWarnings("rawtypes")
    Map map = props;
    return map;
}

public static Map<String, Object> convertToMapWithStringKeys(Map<Object, Object> properties) {
    return properties.entrySet()
        .stream()
        .collect(Collectors.toMap(String::valueOf, (v) -> v == null ? null : Function.identity()));
}

public static <K, V> Map<K, V> convertToTypedMap(Map<Object, Object> properties, Function<Object, K> keyMapper,
    Function<Object, V> valueMapper) {
    return properties.entrySet()
        .stream()
        .collect(Collectors.toMap(keyMapper, i -> i==null? null: valueMapper.apply(i)));
}

/**
 * An internally used helper method for cloning objects. This implementation
 * is not very sophisticated nor efficient. Maybe it can be replaced by an
 * implementation from Commons Lang later. The method checks whether the
 * passed in object implements the {@code Cloneable} interface. If
 * this is the case, the {@code clone()} method is invoked by
 * reflection. Errors that occur during the cloning process are re-thrown as
 * runtime exceptions.
 *
 * @param obj the object to be cloned
 * @return the cloned object
 * @throws CloneNotSupportedException if the object cannot be cloned
 */
public static Object clone(Object obj) throws CloneNotSupportedException {
    if (obj instanceof Cloneable) {
        try {
            Method m = obj.getClass()
                .getMethod(METHOD_CLONE);
            return m.invoke(obj);
        } catch (NoSuchMethodException nmex) {
            throw new CloneNotSupportedException(
                "No clone() method found for class"
                    + obj.getClass()
                    .getName());
        } catch (IllegalAccessException iaex) {
            throw new ConfigurationRuntimeException(iaex);
        } catch (InvocationTargetException itex) {
            throw new ConfigurationRuntimeException(itex);
        }
    } else {
        throw new CloneNotSupportedException(obj.getClass()
            .getName()
            + " does not implement Cloneable");
    }
}
}
