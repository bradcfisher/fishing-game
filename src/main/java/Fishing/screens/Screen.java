
package Fishing.screens;

import Fishing.Resources;
import Fishing.drawable.Drawable;
import Fishing.SoundManager;
import Fishing.drawable.events.DrawableAdapter;
import Fishing.drawable.events.DrawableEvent;
import Fishing.drawable.events.DrawableListener;
import Fishing.drawable.events.DrawableTreeAdapter;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Brad
 */
public class Screen
    extends Drawable
{

    private BufferedImage background;
    private String backgroundMusic;

    private ArrayList<String> backgrounds = new ArrayList<>();
    private ArrayList<String> music = new ArrayList<>();

    private Drawable focusObject = null;
    private Drawable oldParent = null;
    private DrawableListener resizeListener;

    public Screen() {
        setUnscaledSize(1024, 768);
        setFocusable(true);

        addTreeListener(new DrawableTreeAdapter() {
                @Override
                public void drawableAdded( DrawableEvent e ) {
                    if (oldParent != null)
                        oldParent.removeDrawableListener(resizeListener);

                    resizeListener = new DrawableAdapter() {
                                    @Override
                                    public void drawableResized(DrawableEvent e) {
                                        resizeScreen();
                                    } // drawableResized(DrawableEvent e)
                                };

                    oldParent = getParent();
                    oldParent.addDrawableListener(resizeListener);

                    resizeScreen();
                } // drawableAdded( DrawableEvent e )

                @Override
                public void drawableRemoved( DrawableEvent e ) {
                    if (oldParent != null) {
                        oldParent.removeDrawableListener(resizeListener);
                        resizeListener = null;
                        oldParent = null;
                    }
                } // drawableRemoved( DrawableEvent e )
            });

        addDrawableListener(new DrawableAdapter() {
                @Override
                public void drawableShown(DrawableEvent e) {
                    // Hide all other siblings when this screen is shown
                    Drawable p = getParent();
                    for (int n = p.getNumDrawables() - 1; n >= 0; --n) {
                        Drawable d = p.getDrawableAt(n);
                        if (d != e.getSource()) {
                            d.setVisible(false);
                        }
                    } // for

                    // Start music and set focus
                    startBackgroundMusic();
                    if (focusObject != null)
                        focusObject.setFocus();
                    else
                        setFocus();
                } // drawableShown(DrawableEvent e)
            });
    } // Screen()


    public Screen( String backgroundImg ) {
        this(backgroundImg, null);
    } // Screen( String backgroundImg )


    public Screen( String backgroundImg, String backgroundMusic ) {
        this();

        this.backgroundMusic = backgroundMusic;

        setBackground(backgroundImg);

        setUnscaledSize( background.getWidth(), background.getHeight() );
    } // Screen( String backgroundImg, String backgroundMusic )


    public Drawable getFocusObject() {
        return focusObject;
    } // getFocusObject()


    public void setFocusObject(Drawable value) {
        focusObject = value;
    } // setFocusObject(Drawable value)


    public BufferedImage getBackground() {
        return background;
    } // getBackground()


    public void setBackground( String backgroundResource ) {
        if (backgroundResource == null) {
            background = null;
            return;
        }

        // Load background image
        try {
            background = ImageIO.read( Resources.getStream(backgroundResource) );
        } catch (IOException ex) {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // setBackground( InputStream backgroundImg )


    public void setBackground( BufferedImage img ) {
        background = img;
    } // setBackground( BufferedImage img )


    public void setBackground( int index ) {
        if ((index < 0) || (index > backgrounds.size()))
            throw new IllegalArgumentException("The index parameter is outside the range of 0 to "+ backgrounds.size());

        setBackground( backgrounds.get(index) );
    } // setBackground( int index )


    public void setBackgroundMusic( String backgroundMusic ) {
        this.backgroundMusic = backgroundMusic;
        startBackgroundMusic();
    } // setBackgroundMusic( String backgroundMusic )


    public void setBackgroundMusic( int index ) {
        if ((index < 0) || (index > music.size()))
            throw new IllegalArgumentException("The index parameter is outside the range of 0 to "+ backgrounds.size());

        setBackgroundMusic( music.get(index) );
    } // setBackgroundMusic( int index )


    public void clearRegisteredBackgrounds() {
        backgrounds.clear();
    } // clearRegisteredBackgrounds()


    public void registerBackground( String backgroundResource ) {
        backgrounds.add(backgroundResource);
    } // registerBackground( String backgroundResource )


    public void clearRegisteredBackgroundMusic() {
        music.clear();
    } // clearRegisteredBackgroundMusic()


    public void registerBackgroundMusic( String musicResource ) {
        music.add(musicResource);
    } // registerBackgroundMusic( String musicResource )


    private void startBackgroundMusic() {
        if (!isVisible())
            return;

        try {
            SoundManager.setBackgroundMusic( backgroundMusic );
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // startBackgroundMusic()


    @Override
    public void paint( Graphics2D g ) {
        if (background != null)
            g.drawImage(background, 0, 0, (int) getUnscaledWidth(), (int)getUnscaledHeight(), null);
    } // paint( Graphics2D g )


    // Scale the screen within the parent
    private void resizeScreen() {
        Drawable p = getParent();
        if ((p == null) || (p.getWidth() == 0) || (getWidth() == 0))
            return;

        double r1 = p.getWidth() / p.getHeight();
        double r2 = getUnscaledWidth() / getUnscaledHeight();

        double scale;
        if (r1 > r2) {
            scale = p.getHeight() / getUnscaledHeight();
        } else {
            scale = p.getWidth() / getUnscaledWidth();
        }

        setScale( scale, scale );

        if (r1 > r2) {
            setPosition( (p.getWidth() - getWidth()) / 2, 0 );
        } else {
            setPosition( 0, (p.getHeight() - getHeight()) / 2 );
        }
    } // resizeScreen()

} // class Screen
