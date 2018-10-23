/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.learning.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.learning.core.io.locate.FileLocatorUtils;

/**
 * This class is a subset of org.apache.commons.io.FileUtils,
 * git-svn-id: https://svn.apache.org/repos/asf/commons/proper/io/trunk@1423916 13f79535-47bb-0310-9956-ffa450edef68.
 * The subset is determined by {@link FileLocatorUtils}.
 * The copied constants and methods are <em>literally</em> copied.<br />
 * <p>
 * See CONFIGURATION-521 for a discussion.
 *
 * @version $Id: FileUtils.java 1624601 2014-09-12 18:04:36Z oheger $
 */
public class FileUtils {

    public static final Logger logger = LogManager.getLogger();
    /**
     * The UTF-8 character set, used to decode octets in URLs.
     */
    private static final Charset UTF8 = Charset.forName("UTF-8");

    //-----------------------------------------------------------------------

    /**
     * Convert from a <code>URL</code> to a <code>File</code>.
     * <p>
     * From version 1.1 this method will decode the URL.
     * Syntax such as <code>file:///my%20docs/file.txt</code> will be
     * correctly decoded to <code>/my docs/file.txt</code>. Starting with version
     * 1.5, this method uses UTF-8 to decode percent-encoded octets to characters.
     * Additionally, malformed percent-encoded octets are handled leniently by
     * passing them through literally.
     *
     * @param url the file URL to convert, {@code null} returns {@code null}
     * @return the equivalent <code>File</code> object, or {@code null}
     *     if the URL's protocol is not <code>file</code>
     */
    public static File toFile(final URL url) {
        if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
            return null;
        } else {
            String filename = url.getFile()
                .replace('/', File.separatorChar);
            filename = decodeUrl(filename);
            return new File(filename);
        }
    }

    /**
     * Decodes the specified URL as per RFC 3986, i.e. transforms
     * percent-encoded octets to characters by decoding with the UTF-8 character
     * set. This function is primarily intended for usage with
     * {@link URL} which unfortunately does not enforce proper URLs. As
     * such, this method will leniently accept invalid characters or malformed
     * percent-encoded octets and simply pass them literally through to the
     * result string. Except for rare edge cases, this will make unencoded URLs
     * pass through unaltered.
     *
     * @param url The URL to decode, may be {@code null}.
     * @return The decoded URL or {@code null} if the input was
     *     {@code null}.
     */
    static String decodeUrl(final String url) {
        String decoded = url;
        if (url != null && url.indexOf('%') >= 0) {
            final int n = url.length();
            final StringBuffer buffer = new StringBuffer();
            final ByteBuffer bytes = ByteBuffer.allocate(n);
            for (int i = 0; i < n; ) {
                if (url.charAt(i) == '%') {
                    try {
                        do {
                            final byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
                            bytes.put(octet);
                            i += 3;
                        } while (i < n && url.charAt(i) == '%');
                        continue;
                    } catch (final RuntimeException e) {
                        // malformed percent-encoded octet, fall through and
                        // append characters literally
                    } finally {
                        if (bytes.position() > 0) {
                            bytes.flip();
                            buffer.append(UTF8.decode(bytes)
                                .toString());
                            bytes.clear();
                        }
                    }
                }
                buffer.append(url.charAt(i++));
            }
            decoded = buffer.toString();
        }
        return decoded;
    }



    public static InputStream getInputStream(URL url) throws RuntimeException {
        // throw an exception if the target URL is a directory
        File file = FileLocatorUtils.fileFromURL(url);
        if (file != null && file.isDirectory()) {
            throw new RuntimeException("Cannot get a configuration from a directory");
        }

        try {
            return url.openStream();
        } catch (Exception e) {
            throw new RuntimeException("Unable to the configuration from the URL " + url, e);
        }
    }

    public static OutputStream getOutputStream(URL url) throws RuntimeException {
        // file URLs have to be converted to Files since FileURLConnection is
        // read only (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4191800)
        File file = FileLocatorUtils.fileFromURL(url);
        if (file != null) {
            return getOutputStream(file);
        } else {
            // for non file URLs save through an URLConnection
            OutputStream out;
            try {
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);

                // use the PUT method for http URLs
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection conn = (HttpURLConnection) connection;
                    conn.setRequestMethod("PUT");
                }

                out = connection.getOutputStream();

                // check the response code for http URLs and throw an exception if an error occured
                if (connection instanceof HttpURLConnection) {
                    out = new HttpOutputStream(out, (HttpURLConnection) connection);
                }
                return out;
            } catch (IOException e) {
                throw new RuntimeException("Could not save to URL " + url, e);
            }
        }
    }


    public static OutputStream getOutputStream(File file) throws RuntimeException {
        try {
            // create the file if necessary
            createPath(file);
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to save to file " + file, e);
        }
    }

    public static String getPath(File file, URL url, String basePath, String fileName) {
        String path = null;
        // if resource was loaded from jar file may be null
        if (file != null) {
            path = file.getAbsolutePath();
        }

        // try to see if file was loaded from a jar
        if (path == null) {
            if (url != null) {
                path = url.getPath();
            } else {
                try {
                    path = getURL(basePath, fileName).getPath();
                } catch (Exception e) {
                    // simply ignore it and return null
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Could not determine URL for "
                                + "basePath = %s, fileName = %s: %s", basePath,
                            fileName, e));
                    }
                }
            }
        }

        return path;
    }

    public static String getBasePath(String path) {
        URL url;
        try {
            url = getURL(null, path);
            return FileLocatorUtils.getBasePath(url);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFileName(String path) {
        URL url;
        try {
            url = getURL(null, path);
            return FileLocatorUtils.getFileName(url);
        } catch (Exception e) {
            return null;
        }
    }

    public static URL locateFromURL(String basePath, String fileName) {
        try {
            URL url;
            if (basePath == null) {
                return new URL(fileName);
                //url = new URL(name);
            } else {
                URL baseURL = new URL(basePath);
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
            if (logger.isDebugEnabled()) {
                logger.debug("Could not locate file " + fileName + " at " + basePath + ": " + e.getMessage());
            }
            return null;
        }
    }


    public static URL getURL(String basePath, String file) throws MalformedURLException {
        File f = new File(file);
        if (f.isAbsolute()) // already absolute?
        {
            return FileLocatorUtils.toURL(f);
        }

        try {
            if (basePath == null) {
                return new URL(file);
            } else {
                URL base = new URL(basePath);
                return new URL(base, file);
            }
        } catch (MalformedURLException uex) {
            return FileLocatorUtils.toURL(FileLocatorUtils.constructFile(basePath, file));
        }
    }

    /**
     * Create the path to the specified file.
     *
     * @param file the target file
     * @throws RuntimeException if the path cannot be created
     */
    private static void createPath(File file) throws RuntimeException {
        if (file != null) {
            // create the path to the file if the file doesn't exist
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    if (!parent.mkdirs()) {
                        throw new RuntimeException("Cannot create path: " + parent);
                    }
                }
            }
        }
    }

}
