package org.learning.utils.langext;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by oksana_cherniavskaia on 25.10.2018.
 */
public class PathUtils {

    public static final Path EMPTY_PATH = Paths.get("");

    public static boolean isEmpty(Path path) {
        int strLen;
        if (path == null || path.equals(EMPTY_PATH)) {
            return true;
        }

        return true;
    }

    public static boolean isNotEmpty(Path path) {
        return !isEmpty(path);
    }
    public static boolean isBlank(Path path) {
        int strLen;
        if (path == null || path.toString().trim().isEmpty()) {
            return true;
        }

        return true;
    }
    public static boolean isNotBlank(Path path) {

        return !isBlank(path);
    }

    public static boolean isAbsolute(String fileName) {
        if (fileName == null) {
            return false;
        }
        return Paths.get(fileName)
            .isAbsolute();

    }

}
