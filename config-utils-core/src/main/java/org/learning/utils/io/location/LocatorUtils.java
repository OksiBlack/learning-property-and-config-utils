package org.learning.utils.io.location;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.learning.utils.io.location.FileLocator.FileLocatorBuilder;
import org.learning.utils.io.location.strategies.AbsoluteNameLocationStrategy;
import org.learning.utils.io.location.strategies.BasePathLocationStrategy;
import org.learning.utils.io.location.strategies.ClasspathLocationStrategy;
import org.learning.utils.io.location.strategies.CombinedLocationStrategy;
import org.learning.utils.io.location.strategies.FileLocationStrategy;
import org.learning.utils.io.location.strategies.FileSystemLocationStrategy;
import org.learning.utils.io.location.strategies.ProvidedURLLocationStrategy;
import org.learning.utils.langext.ObjectUtils;
import org.learning.utils.langext.PathUtils;
import org.learning.utils.langext.StringUtils;

/**
 * Created by oksana_cherniavskaia on 25.10.2018.
 */
public class LocatorUtils {

    public static final Logger logger = LogManager.getLogger();
    /**
     * Constant for the default {@code FileLocationStrategy}. This strategy is
     * used by the {@code locate()} method if the passed in {@code FileLocator}
     * does not define its own location strategy.
     */
    public static final FileLocationStrategy DEFAULT_LOCATION_STRATEGY =
        initDefaultLocationStrategy();
    private static final FileSystem DEFAULT_FILE_SYSTEM = FileSystems.getDefault();

    public static FileSystem obtainFileSystem(FileLocator locator) {
        return (locator != null) ? ObjectUtils.defaultIfNull(
            locator.getFileSystem(), DEFAULT_FILE_SYSTEM)
            : DEFAULT_FILE_SYSTEM;
    }

    public static boolean isLocationDefined(FileLocator locator) {
        return (locator != null)
            && (locator.getFileName() != null || locator.getSourceURL() != null);
    }

    /**
     * Returns a {@code FileLocator} object based on the passed in one whose
     * location is fully defined. This method ensures that all components of the
     * {@code FileLocator} pointing to the file are set in a consistent way. In
     * detail it behaves as follows:
     * <ul>
     * <li>If the {@code FileLocator} has already all components set which
     * define the file, it is returned unchanged. <em>Note:</em> It is not
     * checked whether all components are really consistent!</li>
     * </ul>
     *
     * @param locator the {@code FileLocator} to be completed
     * @return a {@code FileLocator} with a fully initialized location if
     *     possible or <b>null</b>
     */
    public static FileLocator fullyInitializedLocator(FileLocator locator) {
        if (isFullyInitialized(locator)) {
            // already fully initialized
            return locator;
        }

        URL url = locate(locator);
        return (url != null) ? createFullyInitializedLocatorFromURL(locator,
            url) : null;
    }

    /**
     * Returns a flag whether all components of the given {@code FileLocator}
     * describing the referenced file are defined. In order to reference a file,
     * it is not necessary that all components are filled in (for instance, the
     * URL alone is sufficient). For some use cases however, it might be of
     * interest to have different methods for accessing the referenced file.
     * Also, depending on the filled out properties, there is a subtle
     * difference how the file is accessed: If only the file name is set (and
     * optionally the base path), each time the file is accessed a
     * {@code locate()} operation has to be performed to uniquely identify the
     * file. If however the URL is determined once based on the other components
     * and stored in a fully defined {@code FileLocator}, it can be used
     * directly to identify the file. If the passed in {@code FileLocator} is
     * <b>null</b>, result is <b>false</b>.
     *
     * @param locator the {@code FileLocator} to be checked (may be <b>null</b>)
     * @return a flag whether all components describing the referenced file are
     *     initialized
     */
    public static boolean isFullyInitialized(FileLocator locator) {
        if (locator == null) {
            return false;
        }
        return locator.getBasePath() != null && locator.getFileName() != null
            && locator.getSourceURL() != null;
    }

    /**
     * Locates the provided {@code FileLocator}, returning a URL for accessing
     * the referenced file. This method uses a {@link FileLocationStrategy} to
     * locate the file the passed in {@code FileLocator} points to. If the
     * {@code FileLocator} contains itself a {@code FileLocationStrategy}, it is
     * used. Otherwise, the default {@code FileLocationStrategy} is applied. The
     * strategy is passed the locator and a {@code FileSystem}. The resulting
     * URL is returned. If the {@code FileLocator} is <b>null</b>, result is
     * <b>null</b>.
     *
     * @param locator the {@code FileLocator} to be resolved
     * @return the URL pointing to the referenced file or <b>null</b> if the
     *     {@code FileLocator} could not be resolved
     * @see #DEFAULT_LOCATION_STRATEGY
     */
    public static URL locate(FileLocator locator) {
        if (locator == null) {
            return null;
        }

        FileLocationStrategy fileLocationStrategy = obtainLocationStrategy(locator);

        /*return fileLocationStrategy.locate(
            FileSystems.getDefault(), locator);
*/

        return null;

    }

    /**
     * Creates a fully initialized {@code FileLocator} based on the specified
     * URL.
     *
     * @param src the source {@code FileLocator}
     * @param url the URL
     * @return the fully initialized {@code FileLocator}
     */
    private static FileLocator createFullyInitializedLocatorFromURL(FileLocator src,
        URL url) {
        FileLocatorBuilder fileLocatorBuilder = fileLocator(src);
        if (src.getSourceURL() == null) {
            fileLocatorBuilder.sourceURL(url);
        }
        if (StringUtils.isBlank(src.getFileName())) {
            fileLocatorBuilder.fileName(getFileName(url));


        }
        if (PathUtils.isEmpty(
            src.getBasePath())) {
            fileLocatorBuilder.basePath(getBasePath(url));
        }
        return fileLocatorBuilder.build();
    }

    /**
     * Obtains a non <b>null</b> {@code FileLocationStrategy} object from the
     * passed in {@code FileLocator}. If the {@code FileLocator} is not
     * <b>null</b> and has a {@code FileLocationStrategy} defined, this strategy
     * is returned. Otherwise, result is the default
     * {@code FileLocationStrategy}.
     *
     * @param locator the {@code FileLocator}
     * @return the {@code FileLocationStrategy} for this {@code FileLocator}
     */
    public static FileLocationStrategy obtainLocationStrategy(FileLocator locator) {
        return (locator != null) ? ObjectUtils.defaultIfNull(
            locator.getLocationStrategy(), DEFAULT_LOCATION_STRATEGY)
            : DEFAULT_LOCATION_STRATEGY;
    }

    /**
     * Returns a {@code FileLocatorBuilder} which is already initialized with
     * the properties of the passed in {@code FileLocator}. This builder can
     * be used to create a {@code FileLocator} object which shares properties
     * of the original locator (e.g. the {@code FileSystem} or the encoding),
     * but points to a different file. An example use case is as follows:
     *
     * @param src the source {@code FileLocator} (may be <b>null</b>)
     * @return an initialized builder object for defining a {@code FileLocator}
     */
    public static FileLocatorBuilder fileLocator(FileLocator src) {
        return new FileLocator.FileLocatorBuilder(src);
    }

    private static String getFileName(URL url) {

        Path path = getPathFromURL(url);

        return Optional.ofNullable(path.getFileName())
            .map(s -> s.toString())
            .orElse(null);

    }

    private static Path getBasePath(URL url) {

        Path path = getPathFromURL(url);
        Path res = null;
        if (path != null) {
            if (Files.isDirectory(path)) {
                res = path;
            } else {
                res = path.getParent();
            }
        }

        return res;
    }

    private static Path getPathFromURL(URL url) {
        if (url == null) {
            return null;
        }

        Path path;

        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            return null;
        }

        return path;
    }

    /**
     * Helper method for constructing a file object from a base path and a
     * file name. This method is called if the base path passed to
     * {@code getURL()} does not seem to be a valid URL.
     *
     * @param basePath the base path
     * @param fileName the file name (must not be <b>null</b>)
     * @return the resulting file
     */
    public static Path constructFilePath(Path basePath, String fileName) {
        Path file;

        if (PathUtils.isBlank(basePath) || PathUtils.isAbsolute(fileName)) {
            file = Paths.get(fileName);
        } else {
            file = basePath.resolve(fileName);
        }

        return file;
    }

    /**
     * Tries to locate the file referenced by the passed in {@code FileLocator}.
     * If this fails, an exception is thrown. This method works like
     * {@link #locate(FileLocator)}; however, in case of a failed location
     * attempt an exception is thrown.
     *
     * @param locator the {@code FileLocator} to be resolved
     * @return the URL pointing to the referenced file
     * @throws RuntimeException if the file cannot be resolved
     */
    public static URL locateOrThrow(FileLocator locator)
        throws RuntimeException {
        URL url = locate(locator);
        if (url == null) {
            throw new RuntimeException("Could not locate: " + locator);
        }
        return url;
    }

    /**
     * Creates the default location strategy. This method creates a combined
     * location strategy as described in the comment of the
     * {@link #DEFAULT_LOCATION_STRATEGY} member field.
     *
     * @return the default {@code FileLocationStrategy}
     */
    private static FileLocationStrategy initDefaultLocationStrategy() {
        FileLocationStrategy[] subStrategies =
            new FileLocationStrategy[]{
                new ProvidedURLLocationStrategy(),
                new FileSystemLocationStrategy(),
                new AbsoluteNameLocationStrategy(),
                new BasePathLocationStrategy(),

                new ClasspathLocationStrategy()
            };
        return new CombinedLocationStrategy(Arrays.asList(subStrategies));
    }

    public static void main(String[] args) {

        Path path = Paths.get(".//");
        System.out.println(path);
        System.out.println();
        System.out.println(path.normalize());
        Path x = Paths.get("");

        Path path1 = Paths.get(null);
        System.out.println(x);


    }

    /**
     * Tries to find a resource with the given name in the classpath.
     *
     * @param resourceName the name of the resource
     * @return the URL to the found resource or <b>null</b> if the resource
     *     cannot be found
     */
 public    static URL locateFromClasspath(String resourceName) {
        URL url = null;
        // attempt to getProperties from the context classpath
        ClassLoader loader = Thread.currentThread()
            .getContextClassLoader();
        if (loader != null) {
            url = loader.getResource(resourceName);

            if (url != null) {
                logger.debug("Loading configuration from the context classpath (" + resourceName + ")");
            }
        }

        // attempt to getProperties from the system classpath
        if (url == null) {
            url = ClassLoader.getSystemResource(resourceName);

            if (url != null) {
                logger.debug("Loading configuration from the system classpath (" + resourceName + ")");
            }
        }
        return url;
    }

    public static URL locateFromFileSystemURL(Path basePath, String fileName) {

        try {
            URL url;
            if (basePath == null) {
                return new URL(fileName);
                //url = new URL(name);
            } else {

                ;
                URL baseURL = convertPathToURL(basePath);
                url = new URL(baseURL, fileName);

                // check if the file exists
                InputStream in = null;
                try {
                    in = url.openStream();
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                return url;
            }
        } catch (IOException e) {

            logger.debug("Could not locate file " + fileName + " at " + basePath + ": " + e.getMessage());

            return null;
        }


    }

    /**
     * Tries to convert the specified file to a URL. If this causes an
     * exception, result is <b>null</b>.
     *
     * @param file the file to be converted
     * @return the resulting URL or <b>null</b>
     */
    public static URL convertPathToURL(Path file) {
        return convertURIToURL(file.toUri());
    }

    /**
     * Tries to convert the specified URI to a URL. If this causes an exception,
     * result is <b>null</b>.
     *
     * @param uri the URI to be converted
     * @return the resulting URL or <b>null</b>
     */
    static URL convertURIToURL(URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}