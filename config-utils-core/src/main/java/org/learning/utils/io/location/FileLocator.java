package org.learning.utils.io.location;

import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import org.learning.utils.io.location.strategies.FileLocationStrategy;

/**
 * Created by oksana_cherniavskaia on 25.10.2018.
 */
public class FileLocator {

    /**
     * The file name.
     */
    private final String fileName;

    /**
     * The base path.
     */
    private final Path basePath;

    /**
     * The source URL.
     */
    private final URL sourceURL;

    /**
     * The encoding.
     */
    private final String encoding;
    /**
     * The file system.
     */
    private final FileSystem fileSystem;
    /*** The file location strategy.*/
    private final FileLocationStrategy locationStrategy;

    private FileLocator(FileLocatorBuilder builder) {

        fileName = builder.fileName;
        basePath = builder.basePath;
        sourceURL = builder.sourceURL;
        encoding = builder.encoding;
        fileSystem = builder.fileSystem;
        locationStrategy = builder.locationStrategy;
    }


    public String getFileName() {
        return fileName;
    }

    public Path getBasePath() {
        return basePath;
    }

    public URL getSourceURL() {
        return sourceURL;
    }

    public String getEncoding() {
        return encoding;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public FileLocationStrategy getLocationStrategy() {
        return locationStrategy;
    }

    public static final class FileLocatorBuilder {

        private String fileName;
        private Path basePath;
        private URL sourceURL;
        private String encoding;
        private FileSystem fileSystem;
        private FileLocationStrategy locationStrategy;

        public FileLocatorBuilder() {
            this(null);
        }

        /**
         * Creates a new instance of {@code FileLocatorBuilder} and initializes
         * the builder's properties from the passed in {@code FileLocator}
         * object.
         *
         * @param src the source {@code FileLocator} (may be <b>null</b>)
         */
        public FileLocatorBuilder(FileLocator src) {
            if (src != null) {
                initBuilder(src);
            }
        }

        /**
         * Initializes the properties of this builder from the passed in locator
         * object.
         *
         * @param src the source {@code FileLocator}
         */
        private void initBuilder(FileLocator src) {
            basePath = src.getBasePath();
            fileName = src.getFileName();
            sourceURL = src.getSourceURL();
            encoding = src.getEncoding();
            fileSystem = src.getFileSystem();
            locationStrategy = src.getLocationStrategy();
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
         * <li>{@link #locate(FileLocator)} is called to determine a unique URL
         * pointing to the referenced file. If this is successful, a new
         * {@code FileLocator} is created as a copy of the passed in one, but with
         * all components pointing to the file derived from this URL.</li>
         * <li>Otherwise, result is <b>null</b>.</li>
         * </ul>
         *
         * @param locator the {@code FileLocator} to be completed
         * @return a {@code FileLocator} with a fully initialized location if
         *     possible or <b>null</b>
         */
 /*       public static FileLocator fullyInitializedLocator(FileLocator locator) {
            if (isFullyInitialized(locator)) {
                // already fully initialized
                return locator;
            }

            URL url = locate(locator);
            return (url != null) ? createFullyInitializedLocatorFromURL(locator,
                url) : null;
        }
*/
        /**
         * Returns a flag whether all components of the given {@code FileLocator}
         * describing the referenced file are defined. In order to reference a file,
         * it is not necessary that all components are filled in (for instance, the
         * URL alone is sufficient). If the passed in {@code FileLocator} is
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

        public FileLocatorBuilder locationStrategy(FileLocationStrategy locationStrategy) {
            this.locationStrategy = locationStrategy;
            return this;
        }

        public FileLocatorBuilder fileSystem(FileSystem fileSystem) {
            this.fileSystem = fileSystem;
            return this;
        }

        public FileLocatorBuilder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public FileLocatorBuilder sourceURL(URL sourceURL) {
            this.sourceURL = sourceURL;
            return this;
        }

        public FileLocatorBuilder basePath(Path basePath) {
            this.basePath = basePath;
            return this;
        }

        public FileLocatorBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public FileLocator build() {
            FileLocator pathLocator = new FileLocator(this);

            if (!LocatorUtils.isLocationDefined(pathLocator)) {
                throw new IllegalArgumentException("Not enough arguments to build locator");
            }

            return pathLocator;
        }
    }
}
