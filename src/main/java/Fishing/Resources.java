
package Fishing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides utility functions for retrieving resources from the class path.
 * 
 * @author Brad
 */
public final class Resources {

    /**
     * This utility class cannot be instantiated.
     */
    private Resources() {
    }
    
    /**
     * Retrieves an InputStream to a resource.
     * 
     * @param resourcePath  The path of the resource to retrieve.  If this path
     *                 doesn't start with a slash (/), it will have the text
     *                 "/Fishing/" prepended to it.
     * 
     * @return An InputStream for the specified resource.
     * 
     * @throws IOException if the resource could not be found.
     */
    static public InputStream getStream(String resourcePath)
        throws IOException
    {
        if (!resourcePath.startsWith("/"))
            resourcePath = "/Fishing/" + resourcePath;

        InputStream instream = Resources.class.getResourceAsStream(resourcePath);
        if (instream == null) {
            throw new IOException("Unable to find resource: "+ resourcePath);
        }

        // Wrap the stream in a BufferedInputStream before returning to avoid
        // a "mark/reset not supported" IOException when attempting to load
        // audio files from a JAR resource.
        return new BufferedInputStream(instream);
    }

} // Resources
