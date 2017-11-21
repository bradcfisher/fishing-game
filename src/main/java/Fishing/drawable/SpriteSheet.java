
package Fishing.drawable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Brad
 */
public class SpriteSheet {

    private BufferedImage image;

    public SpriteSheet( InputStream instream )
        throws IOException
    {
        // Read the sprite file
        image = ImageIO.read( instream );
    } // SpriteSheet( InputStream instream )

    public SpriteSheet( BufferedImage img ) {
        image = img;
    } // SpriteSheet( BufferedImage img )

    public void setImage( BufferedImage img ) {
        if (img == null)
            throw new NullPointerException("The image cannot be null.");

        image = img;
    } // setImage( BufferedImage img )

    public BufferedImage getImage() {
        return image;
    } // getImage()

} // class SpriteSheet
