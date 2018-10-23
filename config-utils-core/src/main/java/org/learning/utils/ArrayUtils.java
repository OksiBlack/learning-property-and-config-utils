package org.learning.utils;

import java.lang.reflect.Array;

/**
 * Created by oksana_cherniavskaia on 23.10.2018.
 */
public class ArrayUtils {
    /**
     * <p>Checks if an array of Objects is empty or {@code null}.
     *
     * @param array  the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final Object[] array) {
        return getLength(array) == 0;
    }

    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }
}
