/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deprecated.depr;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.nio.file.FileSystem;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.logging.LogFactory;
import org.learning.utils.io.FileLocator;
import org.learning.utils.io.FileLocator.FileLocatorBuilder;
import org.deprecated.depr.locate.FileLocationStrategy;
import org.deprecated.depr.locate.FileLocatorUtils;


/**
 * <p>
 * A class that manages persistence of an associated {@link } object.
 * </p>
 * <p>
 * Instances of this class can be used to load and save arbitrary objects
 * implementing the {@code } interface in a convenient way from and to
 * various locations. At construction time the {@code } object to
 * manage is passed in. Basically, this object is assigned a location from which
 * it is loaded and to which it can be saved. The following possibilities exist
 * to specify such a location:
 * </p>
 * <ul>
 * <li>URLs: With the method {@code setURL()} a full URL to the configuration
 * source can be specified. This is the most flexible way. Note that the
 * {@code save()} methods support only <em>file:</em> URLs.</li>
 * <li>Files: The {@code setFile()} method allows to specify the configuration
 * source as a file. This can be either a relative or an absolute file. In the
 * former case the file is resolved based on the current directory.</li>
 * <li>As file location in string form: With the {@code setPath()} method a full
 * path to a configuration file can be provided as a string.</li>
 * <li>Separated as base path and file name: The base path is a string defining
 * either a local directory or a URL. It can be set using the
 * {@code setBasePath()} method. The file name, non surprisingly, defines the
 * name of the configuration file.</li>
 * </ul>
 * <p>
 * An instance stores a location. The {@code load()} and {@code save()} methods
 * that do not take an argument make use of this internal location.
 * Alternatively, it is also possible to use overloaded variants of
 * {@code load()} and {@code save()} which expect a location. In these cases the
 * location specified takes precedence over the internal one; the internal
 * location is not changed.
 * </p>
 * <p>
 * The actual position of the file to be loaded is determined by a
 * {@link FileLocationStrategy} based on the location information that has been
 * provided. By providing a custom location strategy the algorithm for searching
 * files can be adapted. Save operations require more explicit information. They
 * cannot rely on a location strategy because the file to be written may not yet
 * exist. So there may be some differences in the way location information is
 * interpreted by load and save operations. In order to avoid this, the
 * following approach is recommended:
 * </p>
 * <ul>
 * <li>Use the desired {@code setXXX()} methods to define the location of the
 * file to be loaded.</li>
 * <li>Call the {@code locate()} method. This method resolves the referenced
 * file (if possible) and fills out all supported location information.</li>
 * <li>Later on, {@code save()} can be called. This method now has sufficient
 * information to store the file at the correct location.</li>
 * </ul>
 * <p>
 * When loading or saving a {@code } object some additional
 * functionality is performed if the object implements one of the following
 * interfaces:
 * </p>
 * <ul>
 * <li>{@code FileLocatorAware}: In this case an object with the current file
 * location is injected before the load or save operation is executed. This is
 * useful for {@code } objects that depend on their current location,
 * e.g. to resolve relative path names.</li>
 * <li>{@code SynchronizerSupport}: If this interface is implemented, load and
 * save operations obtain a write lock on the {@code } object before
 * they access it. (In case of a save operation, a read lock would probably be
 * sufficient, but because of the possible injection of a {@link FileLocator}
 * object it is not allowed to perform multiple save operations in parallel;
 * therefore, by obtaining a write lock, we are on the safe side.)</li>
 * </ul>
 * <p>
 * This class is thread-safe.
 * </p>
 *
 * @version $Id: FileHandler.java 1790899 2017-04-10 21:56:46Z ggregory $
 * @since 2.0
 */
public class FileHandler
{
    /** Constant for the URI scheme for files. */
    private static final String FILE_SCHEME = "file:";

    /** Constant for the URI scheme for files with slashes. */
    private static final String FILE_SCHEME_SLASH = FILE_SCHEME + "//";

   

    /** A reference to the current {@code FileLocator} object. */
    private final AtomicReference<FileLocator> fileLocator;
    /**
     * The file-based object managed by this handler.
     */
    private final FileBased content;


    /**
     * Creates a new instance of {@code FileHandler} which is not associated
     * with a {@code FileBased} object and thus does not have a content. Objects
     * of this kind can be used to define a file location, but it is not
     * possible to actually getProperties or save data.
     */
    public FileHandler() {
        this(null);
    }

    /**
     * Creates a new instance of {@code FileHandler} and sets the managed
     * {@code FileBased} object.
     *
     * @param obj the file-based object to manage
     */
    public FileHandler(FileBased obj) {
        this(obj, emptyFileLocator());
    }

    /**
     * Creates a new instance of {@code FileHandler} based on the given
     * {@code FileBased} and {@code FileLocator} objects.
     *
     * @param obj     the {@code FileBased} object to manage
     * @param locator the {@code FileLocator}
     */
    private FileHandler(FileBased obj, FileLocator locator) {
        content = obj;
        fileLocator = new AtomicReference<>(locator);
    }

    /**
     * Creates an uninitialized file locator.
     *
     * @return the locator
     */
    private static FileLocator emptyFileLocator() {
        return FileLocatorUtils.fileLocator()
            .build();
    }

    /**
     * Creates a new instance of {@code FileHandler} which is associated with
     * the given {@code FileBased} object and the location defined for the given
     * {@code FileHandler} object. A copy of the location of the given
     * {@code FileHandler} is created. This constructor is a possibility to
     * associate a file location with a {@code FileBased} object.
     *
     * @param obj the {@code FileBased} object to manage
     * @param c   the {@code FileHandler} from which to copy the location (must
     *            not be <b>null</b>)
     * @throws IllegalArgumentException if the {@code FileHandler} is
     *                                  <b>null</b>
     */
    public FileHandler(FileBased obj, FileHandler c) {
        this(obj, checkSourceHandler(c).getFileLocator());
    }


    

    /**
     * Return the name of the file. If only a URL is defined, the file name
     * is derived from there.
     *
     * @return the file name
     */
    public String getFileName()
    {
        FileLocator locator = getFileLocator();
        if (locator.getFileName() != null)
        {
            return locator.getFileName();
        }

        if (locator.getSourceURL() != null)
        {
            return FileLocatorUtils.getFileName(locator.getSourceURL());
        }

        return null;
    }



    /**
     * Return the base path. If no base path is defined, but a URL, the base
     * path is derived from there.
     *
     * @return the base path
     */
    public String getBasePath()
    {
        FileLocator locator = getFileLocator();
        if (locator.getBasePath() != null)
        {
            return locator.getBasePath();
        }

        if (locator.getSourceURL() != null)
        {
            return FileLocatorUtils.getBasePath(locator.getSourceURL());
        }

        return null;
    }



    /**
     * Returns the location of the associated file as a {@code File} object. If
     * the base path is a URL with a protocol different than &quot;file&quot;,
     * or the file is within a compressed archive, the return value will not
     * point to a valid file object.
     *
     * @return the location as {@code File} object; this can be <b>null</b>
     */
    public File getFile()
    {
        return createFile(getFileLocator());
    }




    /**
     * Returns the location of the associated file as a URL. If a URL is set,
     * it is directly returned. Otherwise, an attempt to locate the referenced
     * file is made.
     *
     * @return a URL to the associated file; can be <b>null</b> if the location
     *         is unspecified
     */
    public URL getURL()
    {
        FileLocator locator = getFileLocator();
        return (locator.getSourceURL() != null) ? locator.getSourceURL()
                : FileLocatorUtils.locate(locator);
    }



    /**
     * Returns a {@code FileLocator} object with the specification of the file
     * stored by this {@code FileHandler}. Note that this method returns the
     * internal data managed by this {@code FileHandler} as it was defined.
     * This is not necessarily the same as the data returned by the single
     * access methods like {@code getFileName()} or {@code getURL()}: These
     * methods try to derive missing data from other values that have been set.
     *
     * @return a {@code FileLocator} with the referenced file
     */
    public FileLocator getFileLocator()
    {
        return fileLocator.get();
    }



    /**
     * Tests whether a location is defined for this {@code FileHandler}.
     *
     * @return <b>true</b> if a location is defined, <b>false</b> otherwise
     */
    public boolean isLocationDefined()
    {
        return FileLocatorUtils.isLocationDefined(getFileLocator());
    }



    /**
     * Returns the encoding of the associated file. Result can be <b>null</b> if
     * no encoding has been set.
     *
     * @return the encoding of the associated file
     */
    public String getEncoding()
    {
        return getFileLocator().getEncoding();
    }



    /**
     * Returns the {@code FileSystem} to be used by this object when locating
     * files. Result is never <b>null</b>; if no file system has been set, the
     * default file system is returned.
     *
     * @return the used {@code FileSystem}
     */
    public FileSystem getFileSystem()
    {
        return FileLocatorUtils.obtainFileSystem(getFileLocator());
    }




    /**
     * Returns the {@code FileLocationStrategy} to be applied when accessing the
     * associated file. This method never returns <b>null</b>. If a
     * {@code FileLocationStrategy} has been set, it is returned. Otherwise,
     * result is the default {@code FileLocationStrategy}.
     *
     * @return the {@code FileLocationStrategy} to be used
     */
    public FileLocationStrategy getLocationStrategy()
    {
        return FileLocatorUtils.obtainLocationStrategy(getFileLocator());
    }

   

    /**
     * Locates the referenced file if necessary and ensures that the associated
     * {@link FileLocator} is fully initialized. When accessing the referenced
     * file the information stored in the associated {@code FileLocator} is
     * used. If this information is incomplete (e.g. only the file name is set),
     * an attempt to locate the file may have to be performed on each access. By
     * calling this method such an attempt is performed once, and the results of
     * a successful localization are stored. Hence, later access to the
     * referenced file can be more efficient. Also, all properties pointing to
     * the referenced file in this object's {@code FileLocator} are set (i.e.
     * the URL, the base path, and the file name). If the referenced file cannot
     * be located, result is <b>false</b>. This means that the information in
     * the current {@code FileLocator} is insufficient or wrong. If the
     * {@code FileLocator} is already fully defined, it is not changed.
     *
     * @return a flag whether the referenced file could be located successfully
     * @see FileLocatorUtils#fullyInitializedLocator(FileLocator)
     */
    public boolean locate()
    {
        boolean result;
        boolean done;

        do
        {
            FileLocator locator = getFileLocator();
            FileLocator fullLocator =
                    FileLocatorUtils.fullyInitializedLocator(locator);
            if (fullLocator == null)
            {
                result = false;
                fullLocator = locator;
            }
            else
            {
                result =
                        fullLocator != locator
                                || FileLocatorUtils.isFullyInitialized(locator);
            }
            done = fileLocator.compareAndSet(locator, fullLocator);
        } while (!done);

        return result;
    }







    /**
     * Prepares a builder for a {@code FileLocator} which does not have a
     * defined file location. Other properties (e.g. encoding or file system)
     * are initialized from the {@code FileLocator} associated with this object.
     *
     * @return the initialized builder for a {@code FileLocator}
     */
    private FileLocatorBuilder prepareNullLocatorBuilder()
    {
        return FileLocatorUtils.fileLocator(getFileLocator()).sourceURL(null)
                .basePath(null).fileName(null);
    }











    /**
     * Normalizes URLs to files. Ensures that file URLs start with the correct
     * protocol.
     *
     * @param fileName the string to be normalized
     * @return the normalized file URL
     */
    private static String normalizeFileURL(String fileName)
    {
        if (fileName != null && fileName.startsWith(FILE_SCHEME)
                && !fileName.startsWith(FILE_SCHEME_SLASH))
        {
            fileName =
                    FILE_SCHEME_SLASH
                            + fileName.substring(FILE_SCHEME.length());
        }
        return fileName;
    }

    /**
     * A helper method for closing a stream. Occurring exceptions will be
     * ignored.
     *
     * @param cl the stream to be closed (may be <b>null</b>)
     */
    private static void closeSilent(Closeable cl)
    {
        try
        {
            if (cl != null)
            {
                cl.close();
            }
        }
        catch (IOException e)
        {
            LogFactory.getLog(FileHandler.class).warn("Exception when closing " + cl, e);
        }
    }

    /**
     * Creates a {@code File} object from the content of the given
     * {@code FileLocator} object. If the locator is not defined, result is
     * <b>null</b>.
     *
     * @param loc the {@code FileLocator}
     * @return a {@code File} object pointing to the associated file
     */
    private static File createFile(FileLocator loc)
    {
        if (loc.getFileName() == null && loc.getSourceURL() == null)
        {
            return null;
        }
        else if (loc.getSourceURL() != null)
        {
            return FileLocatorUtils.fileFromURL(loc.getSourceURL());
        }
        else
        {
            return FileLocatorUtils.getFile(loc.getBasePath(),
                    loc.getFileName());
        }
    }



    /**
     * Helper method for checking a file handler which is to be copied. Throws
     * an exception if the handler is <b>null</b>.
     *
     * @param c the {@code FileHandler} from which to copy the location
     * @return the same {@code FileHandler}
     */
    private static FileHandler checkSourceHandler(FileHandler c)
    {
        if (c == null)
        {
            throw new IllegalArgumentException(
                    "FileHandler to assign must not be null!");
        }
        return c;
    }

}
