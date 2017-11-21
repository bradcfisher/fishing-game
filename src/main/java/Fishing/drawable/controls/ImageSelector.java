
package Fishing.drawable.controls;

import Fishing.GraphicsUtil;
import Fishing.Resources;
import Fishing.drawable.BehaviorUtil;
import Fishing.drawable.Drawable;
import Fishing.drawable.FocusManager;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableMouseAdapter;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.events.EventBase;
import Fishing.drawable.events.MenuEvent;
import Fishing.drawable.events.MenuListener;
import Fishing.drawable.events.TimerEvent;
import Fishing.drawable.events.TimerListener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * A control that enables the user to select an image from a set of images.
 * 
 * @author Brad
 */
public class ImageSelector
    extends Drawable
    implements TimerListener
{
    
    private static final int ITEM_NONE         = -1;

    private static final int ITEM_LEFT         = 0;

    private static final int ITEM_THUMBNAIL    = 1;

    private static final int ITEM_RIGHT        = 2;

    private BufferedImage image;

    private int imageFrameWidth;

    private int imageFrameHeight;

    private int numFrames;

    private int selectedItem = 0;

    /**
     * The width to draw the buttons on either side of the control.
     */
    private int buttonWidth = 25;

    private int frameWidth;

    private int frameHeight;

    private int mouseOverItem = ITEM_NONE;

    private int frameOffset = 0;

    private int targetFrameOffset = -1;

    private int timerTaskHandle = 0;

    /**
     * The registered menu listeners.
     */
    private List<MenuListener> menuListeners;

    /**
     * Constructs a new instance.
     * 
     * The following defaults are applied to the new object:
     * <ul>
     *  <li>frameWidth = 125
     *  <li>frameHeight = 94
     *  <li>buttonWidth = 25
     * </ul>
     */
    public ImageSelector() {
        getMouseListeners().addDefault(
            new DrawableMouseAdapter() {
                /**
                 * Handles mouse over/out effects for the side buttons.
                 * @param e The event that was received.
                 */
                @Override
                public void drawableMouseMoved( DrawableMouseEvent e ) {
                    if (e.getX() < buttonWidth) {
                        if (selectedItem > 0) {
                            mouseOverItem = ITEM_LEFT;
                        }
                    } else if (e.getX() > getUnscaledWidth() - buttonWidth) {
                        if (selectedItem < numFrames - 1) {
                            mouseOverItem = ITEM_RIGHT;
                        }
                    } else {
                        mouseOverItem = ITEM_THUMBNAIL;
                    }
                } // drawableMouseMoved( DrawableMouseEvent e )

                /**
                 * Removes mouse over effects.
                 * @param e The event that was received.
                 */
                @Override
                public void drawableMouseExited( DrawableMouseEvent e ) {
                    mouseOverItem = ITEM_NONE;
                } // drawableMouseExited( DrawableMouseEvent e )

                /**
                 * Handles mouse clicks on the side buttons to navigate through
                 * the available options.
                 * @param e The event that was received.
                 */
                @Override
                public void drawableMouseClicked( DrawableMouseEvent e ) {
                    if (mouseOverItem == ITEM_LEFT) {
                        if (selectedItem > 0)
                            setSelectedItem( selectedItem - 1);
                    } else if (mouseOverItem == ITEM_RIGHT) {
                        if (selectedItem < numFrames - 1)
                            setSelectedItem( selectedItem + 1);
                    }
                } // drawableMouseClicked( DrawableMouseEvent e )
            }
        );

        getKeyListeners().addDefault(
            new DrawableKeyAdapter() {
                /**
                 * Handles left & right arrow key presses to navigate through
                 * the available options.
                 * @param e The event that was received.
                 */
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    int modifiersMask = KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK | KeyEvent.META_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;
                    if ((e.getModifiers() & modifiersMask) == 0) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_LEFT:
                                e.consume();
                                if (selectedItem > 0)
                                    setSelectedItem( selectedItem - 1);
                                break;

                            case KeyEvent.VK_RIGHT:
                                e.consume();
                                if (selectedItem < numFrames - 1)
                                    setSelectedItem( selectedItem + 1);
                                break;
                        }
                    }
                }
            }
        );

        BehaviorUtil.addFocusableBehavior(this);

        frameWidth = 125;
        frameHeight = 94;

        // Enable once keyboard controls are implemented
        setFocusable(true);
        setTabIndex(0);
        setMouseEnabled(true);
    } // ImageSelector()

    /**
     * Constructs a new instance using an image resource as the source of the
     * available image selections.
     * 
     * <p>The {@code frameWidth} will be initially computed as the width of
     * the image divided by the specified number of frames.
     * 
     * @param imageResource The path to the image resource to load.  The image
     *                      should contain the individual frames which can be
     *                      selected from arranged horizontally in a single
     *                      row.
     * @param frames        The number of frames in the image. 
     * 
     * @throws IOException if an I/O error occurs while reading the resource.
     */
    public ImageSelector( String imageResource, int frames )
        throws IOException
    {
        this();
        setImage( imageResource, frames );
    } // ImageSelector( String imageResource, int frames )

    /**
     * Constructs a new instance using an image as the source of the
     * available image selections.
     * 
     * <p>The {@code frameWidth} will be initially computed as the width of
     * the image divided by the specified number of frames.
     * 
     * @param image     The image containing the frames/images that can be
     *                  selected from.  The image should contain the individual
     *                  frames which can be selected from arranged horizontally
     *                  in a single row.
     * @param frames    The number of frames in the provided image. 
     */
    public ImageSelector( BufferedImage image, int frames ) {
        this();
        setImage( image, frames );
    } // ImageSelector( BufferedImage image, int frames )

    /**
     * Retrieves the number of frames in the assigned image.
     * 
     * @return  The number of frames in the assigned image.
     */
    public int getNumFrames() {
        return numFrames;
    } // getNumFrames()

    /**
     * Retrieves the width of an individual frames in the assigned image.
     * 
     * @return  The width of an individual frames in the assigned image.
     */
    public int getFrameWidth() {
        return frameWidth;
    } // getFrameWidth()

// TODO: Document
    public void setFrameWidth( int value ) {
        setFrameSize( value, frameHeight );
    } // setFrameWidth( double value )

    /**
     * Retrieves the height of an individual frames in the assigned image.
     * 
     * @return  The height of an individual frames in the assigned image.
     */
    public int getFrameHeight() {
        return frameHeight;
    } // getFrameHeight()

// TODO: Document
    public void setFrameHeight( int value ) {
        setFrameSize( frameWidth, value );
    } // setFrameHeight( double value )

// TODO: Document
    public synchronized void setFrameSize( int width, int height ) {
        if (width < 0)
            throw new IllegalArgumentException("The frameWidth cannot be less than 0.");

        if (height < 0)
            throw new IllegalArgumentException("The frameHeight cannot be less than 0.");

        frameWidth = width;
        frameHeight = height;

        setUnscaledSize( frameWidth + 2 * buttonWidth, frameHeight );
    } // setFrameSize( double width, double height )

    /**
     * Retrieves the width to draw the buttons on either side of the control.
     * 
     * @return  The width to draw the buttons on either side of the control.
     */
    public double getButtonWidth() {
        return buttonWidth;
    } // getButtonWidth()

    /**
     * Sets the width used to draw the buttons on either side of the control.
     * 
     * @param value The new width to use when drawing the buttons on either
     *              side of the control.
     * 
     * @throws  IllegalArgumentException if the specified {@code value} is less
     *          than 0.
     */
    public void setButtonWidth( int value ) {
        if (value < 0)
            throw new IllegalArgumentException("The buttonWidth cannot be less than 0.");

        buttonWidth = value;

        setUnscaledWidth( frameWidth + 2 * buttonWidth );
    } // setButtonWidth()



    public synchronized final void setImage( BufferedImage image, int frames ) {
        if (image == null)
            throw new IllegalArgumentException("The image parameter cannot be null.");

        if (frames < 0)
            throw new IllegalArgumentException("The frames parameter must be greater than or equal to 0.");

        this.image = image;
        this.numFrames = frames;

        imageFrameWidth = this.image.getWidth() / Math.max(frames, 1);
        imageFrameHeight = this.image.getHeight();
    } // setImage( BufferedImage image, int frames )


    public final void setImage( String imageResource, int frames )
        throws IOException
    {
        if (imageResource == null)
            throw new IllegalArgumentException("The image parameter cannot be null.");
        
        if (frames <= 0)
            throw new IllegalArgumentException("The frames parameter must be greater than 0.");

        BufferedImage img = ImageIO.read( Resources.getStream(imageResource) );
        setImage(img, frames);
    } // setImage( String imageResource, int frames )


    public int getSelectedItem() {
        return selectedItem;
    } // getSelectedItem()


    public void setSelectedItem( int selection ) {
        synchronized (this) {
            if (selection == selectedItem)
                return;

            if ((selectedItem < 0)|| (selectedItem >= numFrames))
                throw new IllegalArgumentException("The selection parameter is outside the range 0 to "+ (numFrames - 1));

            selectedItem = selection;
            targetFrameOffset = selectedItem * imageFrameWidth;

            if (timerTaskHandle == 0)
                timerTaskHandle = ControlTimer.scheduleAtFixedRate(this, 1000/30);
        }

        EventBase.notifyListeners(
            menuListeners,
            new MenuEvent(this, selection, null),
            (listener, evt) -> { listener.menuSelected(evt); }
        );
    } // setSelectedItem( int selection )


    public void addMenuListener( MenuListener listener ) {
        menuListeners = EventBase.addListener(menuListeners, listener);
    } // addMenuListener( MenuListener listener )


    public void removeMenuListener( MenuListener listener ) {
        menuListeners = EventBase.removeListener(menuListeners, listener);
    } // removeMenuListener( MenuListener listener )

    @Override
    public void paint( Graphics2D g ) {
        int w = (int) getUnscaledWidth();
        int h = (int) getUnscaledHeight();

        if ((image == null) || (selectedItem < 0)) {
            // No image, or no selection
            g.setColor( Color.DARK_GRAY );
            g.fillRect(buttonWidth, 0, buttonWidth + frameWidth, frameHeight);
        } else {
            // Draw the thumbnail image
            g.drawImage(image,
                        buttonWidth, 0, buttonWidth + frameWidth, frameHeight,
                        frameOffset, 0, frameOffset + imageFrameWidth, imageFrameHeight,
                        null);
        }

        // Draw the focus rect around the thumbnail
        if (FocusManager.isFocused(this)) {
            Rectangle2D bounds = new Rectangle2D.Double(
                buttonWidth + 2, 2, frameWidth - 4, frameHeight - 4
            );
            GraphicsUtil.drawFocusRect( g, bounds );
        }

        Color clr1;
        Color clr2;

        // Draw < button
        if (selectedItem <= 0) {
            clr1 = Color.GRAY;
            clr2 = Color.DARK_GRAY;
        } else if (mouseOverItem == ITEM_LEFT) {
            clr1 = Color.LIGHT_GRAY;
            clr2 = Color.GRAY;
        } else {
            clr1 = Color.LIGHT_GRAY;
            clr2 = Color.BLACK;
        }

        g.setColor( clr1 );
        g.fill3DRect( 0, 0, buttonWidth, h, true );

        int hPad = (int)(0.20 * buttonWidth);
        int vPad = hPad;

        // Draw <
        g.setColor( clr2 );
        g.fillPolygon(
                    new int[] { hPad, buttonWidth - hPad, buttonWidth - hPad }, //x
                    new int[] { h / 2, vPad, h - vPad }, //y
                    3
                );

        // Draw > button
        if (selectedItem >= numFrames - 1) {
            clr1 = Color.GRAY;
            clr2 = Color.DARK_GRAY;
        } else if (mouseOverItem == ITEM_RIGHT) {
            clr1 = Color.LIGHT_GRAY;
            clr2 = Color.GRAY;
        } else {
            clr1 = Color.LIGHT_GRAY;
            clr2 = Color.BLACK;
        }
        
        g.setColor( clr1 );
        g.fill3DRect( w - buttonWidth, 0, buttonWidth, h, true );

        // Draw >
        g.setColor( clr2 );
        g.fillPolygon(
                    new int[] { w - hPad, w - buttonWidth + hPad, w - buttonWidth + hPad }, //x
                    new int[] { h / 2, vPad, h - vPad }, //y
                    3
                );
    } // paint( Graphics2D g )

    @Override
    public void timerFired(TimerEvent e) {
        int speed = 4;

        if (Math.abs(frameOffset - targetFrameOffset) < speed)
            frameOffset = targetFrameOffset;
        else if (frameOffset > targetFrameOffset)
            frameOffset -= speed;
        else if (frameOffset < targetFrameOffset)
            frameOffset += speed;

        if (frameOffset == targetFrameOffset) {
            ControlTimer.cancelTimerListener( e.getSource() );
            timerTaskHandle = 0;
        }
    } // timerFired(TimerEvent e)

} // class ImageSelector
