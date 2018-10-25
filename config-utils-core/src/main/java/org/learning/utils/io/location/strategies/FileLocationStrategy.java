package org.learning.utils.io.location.strategies;

import java.net.URL;
import java.nio.file.FileSystem;
import org.learning.utils.io.location.FileLocator;

public interface FileLocationStrategy
{
    /**
     * Tries to locate the specified file. The method also expects the
     * {@code FileSystem} to be used. Note that the {@code FileLocator} object
     * may also contain a {@code FileSystem}, but this is optional. The passed
     * in {@code FileSystem} should be used, and callers must not pass a
     * <b>null</b> reference for this argument. A concrete implementation has to
     * evaluate the properties stored in the {@code FileLocator} object and try
     * to match them to an existing file. If this can be done, a corresponding
     * URL is returned. Otherwise, result is <b>null</b>. Implementations should
     * not throw an exception (unless parameters are <b>null</b>) as there might
     * be alternative strategies which can find the file in question.
     *
     * @param locator the object describing the file to be located
     * @return a URL pointing to the referenced file if location was successful;
     *         <b>null</b> if the file could not be resolved
     */
    URL locate(FileSystem fileSystem, FileLocator locator);
}
