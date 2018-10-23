package org.learning.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Wraps the output stream so errors can be detected in the HTTP response.
 *
 * @author <a
 *     href="http://commons.apache.org/configuration/team-list.html">Commons Configuration team</a>
 * @since 1.7
 */
public class HttpOutputStream extends VerifiableOutputStream {

    /**
     * The wrapped OutputStream
     */
    private final OutputStream stream;

    /**
     * The HttpURLConnection
     */
    private final HttpURLConnection connection;

    public HttpOutputStream(OutputStream stream, HttpURLConnection connection) {
        this.stream = stream;
        this.connection = connection;
    }


    @Override
    public String toString() {
        return stream.toString();
    }

    public void verify() throws IOException {
        if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new IOException("HTTP Error " + connection.getResponseCode()
                + " " + connection.getResponseMessage());
        }
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular,
     *                     an <code>IOException</code> may be thrown if the
     *                     output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {

    }
}
