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
package org.learning.core.io.locate;

import java.net.URL;
import java.nio.file.FileSystem;
import org.learning.core.io.FileLocator;


/**
 * <p>
 * An interface allowing applications to customize the process of locating a
 * file.
 * </p>
 * <p>
 * {@link FileHandler} uses {@link FileLocator} objects for referencing files.
 * These objects are not guaranteed to identify a file in a unique way. For
 * instance, if only a file name is defined, this could mean a relative file
 * name in the current directory, the name of a resource to be loaded from the
 * class path, or something else. Before the file described by a
 * {@code FileLocator} can be actually accessed, an unambiguous URL pointing to
 * this file has to be obtained. This is the job of a
 * {@code FileLocationStrategy}.
 * </p>
 * <p>
 * This interface defines a method for locating a file provided as a
 * {@code FileLocator} object. If location is successful, a URL is returned. A
 * concrete implementation can perform arbitrary actions to search for the file
 * in question at various places. There will also be an implementation allowing
 * the combination of multiple {@code FileLocationStrategy} implementations; so
 * a file can be searched using multiple strategies until one of them is
 * successful.
 * </p>
 *
 * @version $Id: FileLocationStrategy.java 1624601 2014-09-12 18:04:36Z oheger $
 * @since 2.0
 */
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
