
package Fishing.drawable;

import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

/**
 * A JPanel subclass that renders a Drawable scene for its content.
 * 
 * @author Brad
 */
public class DrawablePanel
    extends JPanel
    implements MouseListener, MouseMotionListener, KeyListener
{

    /**
     * The target frame rate at which to update the displayed scene within
     * this panel.
     */
    private double frameRate = 30;

    /**
     * Timer scheduled to trigger at the specified {@code frameRate}.
     */
    private final Timer displayTimer;

    /**
     * The root Drawable stage that is being rendered into this panel.
     */
    private final Stage stage;

    /**
     * Timestamp when the last frame started rendering.
     */
    private long lastFrameTime;

    /**
     * The last computed frame rate value.  This value is only updated at most
     * once every half-second when {@code showFrameRate} is {@code true}.
     */
    private double lastFrameRate = 0;

    /**
     * The number of frames that have been rendered since the last time the
     * {@code lastFrameRate} was computed.  This value is only updated when
     * {@code showFrameRate} is {@code true}.
     */
    private int frameCount = 0;

    /**
     * Whether to display the computed actual frame rate in the bottom-left
     * corner of this panel.
     */
    private boolean showFrameRate = false;

    /**
     * The font to use for rendering the frame rate when it is visible.
     * This font is generated the first time the {@code showFrameRate} property
     * is set to {@code true}.
     */
    private GeneratedFont frameRateFont;

    /**
     * Constructs a new instance with the specified target {@code frameRate}.
     * 
     * @param   frameRate   The target frame rate at which to update the
     *                      displayed scene within this panel.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public DrawablePanel( double frameRate ) {
        displayTimer = new Timer();

        setFrameRate(frameRate);
        //setCoordinateBounds(0, 0, 1024, 768);

        stage = new Stage(this);
        stage.setUnscaledSize( 100, 100 );

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Add handler to resize the active screen whenever the window is resized
        addComponentListener(
            new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (getHeight() == 0)
                        return;

                    stage.setUnscaledSize( getWidth(), getHeight() );
                }
            }
        );
    } // DrawablePanel()

    /**
     * Determines whether the computed actual frame rate is displayed in the
     * bottom-left corner of this panel.
     * 
     * @return  {@code true} if the frame rate is being displayed, {@code false}
     *          if the frame rate is not visible.
     */
    public boolean isShowingFrameRate() {
        return showFrameRate;
    } // isShowingFrameRate()

    /**
     * Sets whether the computed actual frame rate is displayed in the
     * bottom-left corner of this panel.
     * 
     * @param   value   {@code true} if the frame rate should be displayed,
     *                  {@code false} if the frame rate should not be visible.
     */
    public void setShowFrameRate( boolean value ) {
        if (showFrameRate == value)
            return;

        showFrameRate = value;

        if (showFrameRate) {
            if (frameRateFont == null) {
                frameRateFont = new GeneratedFont( new Font("Arial", Font.BOLD, 30) );
                frameRateFont.setColor(Color.WHITE);
                frameRateFont.setOutlineColor(Color.BLACK);
//                frameRateFont.setOutlineWidth();
            }

            lastFrameTime = System.currentTimeMillis();
            frameCount = 0;
        }
    } // setShowFrameRate( boolean value )

    /**
     * Sets the target frame rate at which to update the displayed scene within
     * this panel.
     * 
     * @param   value   The new target frame rate value.  Must be greater than
     *                  0 and less than 1000.
     * 
     * @throws  IllegalArgumentException if the {@code value} is less than or
     *          equal to 0 or greater than 1000.
     */
    public final synchronized void setFrameRate( double value ) {
        if ((value <= 0) || (value > 1000))
            throw new IllegalArgumentException("The frameRate must be greater than 0 and no greater than 1000.");

        frameRate = value;

        displayTimer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    repaint();
                }
            },
            0,
            (long)(1000 / frameRate)
        );
    } // setFrameRate( double rate )

    /**
     * Retrieves the target frame rate at which the displayed scene within this
     * panel should be updated.
     * 
     * @return  The target frame rate at which the displayed scene within this
     *          panel should be updated.
     */
    public double getFrameRate() {
        return frameRate;
    } // getFrameRate()

    /**
     * Retrieves the root Drawable stage that is being rendered into this panel.
     * 
     * @return  The root Drawable stage that is being rendered into this panel.
     */
    public Stage getStage() {
        return stage;
    } // getStage()

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        stage.draw(g2);

        if (showFrameRate) {
            ++frameCount;

            long curTime = System.currentTimeMillis();
            double rate = (curTime - lastFrameTime);

            if (rate > 500) {
                lastFrameRate = 1000 / (rate / frameCount);
                lastFrameTime = curTime;
                frameCount = 0;
            }

            frameRateFont.drawText(
                g2,
                getWidth() - 10, getHeight() - 35,
                String.format("%.1f / %.1f", lastFrameRate, frameRate),
                BitmapFont.ALIGN_RIGHT
            );
        }
    } // paintComponent(Graphics g)

    /**
     * Processes an AWT MouseEvent, by routing it to the embedded Drawable
     * stage if it has not already been consumed.
     * 
     * @param   e   The AWT MouseEvent to process.
     */
    private void _processMouseEvent( MouseEvent e ) {
        if (e.isConsumed())
            return;

        Point2D pt = new Point2D.Double(e.getX(), e.getY());

        boolean processed = false;
        try {
            if (stage.processMouseEvent( stage.getTransform().inverseTransform(pt, null), e ) != null) {
                // The mouse event was processed by an object in the scene.
                processed = true;
            }
        } catch (NoninvertibleTransformException ex) {
            //Logger.getLogger(Drawable.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!processed && ((e.getID() == MouseEvent.MOUSE_MOVED) || (e.getID() == MouseEvent.MOUSE_DRAGGED)))
            Drawable.dispatchEnterLeaveEvents( null, e );
    } // _processMouseEvent( MouseEvent e )

    /**
     * AWT mouse click events are not processed by this listener.
     * This method exists only to satisfy the MouseListener interface.
     * 
     * @param   e   The AWT MouseEvent that occurred.
     */
    @Override
    public void mouseClicked(MouseEvent e) { }

    /**
     * AWT mouse entered events are not processed by this listener.
     * This method exists only to satisfy the MouseListener interface.
     * 
     * @param   e   The AWT MouseEvent that occurred.
     */
    @Override
    public void mouseEntered(MouseEvent e) { }

    /**
     * Captures AWT mouse exit events and ensures the enter/leave events on
     * the embedded stage are processed accordingly.
     * 
     * @param   e   The AWT MouseEvent that occurred.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        Drawable.dispatchEnterLeaveEvents( null, e );
    } // mouseExited(MouseEvent e)

    /**
     * Captures AWT mouse button press events and routes them to the embedded
     * stage as appropriate.
     * 
     * @param   e   The AWT MouseEvent that occurred.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        _processMouseEvent(e);
    } // mousePressed(MouseEvent e)

    /**
     * Captures AWT mouse button release events and routes them to the embedded
     * stage as appropriate.
     * 
     * @param   e   The AWT MouseEvent that occurred.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        _processMouseEvent(e);
    } // mouseReleased(MouseEvent e)

    /**
     * Captures AWT mouse dragged events and routes them to the embedded
     * stage as appropriate.
     * 
     * @param   e   The AWT MouseEvent that occurred.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        _processMouseEvent(e);
    } // mouseDragged(MouseEvent e)

    /**
     * Captures AWT mouse move events and routes them to the embedded
     * stage as appropriate.
     * 
     * @param   e   The AWT MouseEvent that occurred.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        _processMouseEvent(e);
    } // mouseMoved(MouseEvent e)

    /**
     * Processes an AWT KeyEvent, by routing it to the embedded Drawable
     * stage if it has not already been consumed.
     * 
     * @param   e   The AWT KeyEvent to process.
     */
    private void _processKeyEvent( KeyEvent e ) {
        if (e.isConsumed())
            return;

        stage.processKeyEvent( e );
    } // _processKeyEvent( MouseEvent e )

    /**
     * Captures AWT key typed events and routes them to the embedded
     * stage as appropriate.
     * 
     * @param   e   The AWT KeyEvent that occurred.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        _processKeyEvent(e);
    } // keyTyped(KeyEvent e)

    /**
     * Captures AWT key pressed events and routes them to the embedded
     * stage as appropriate.
     * 
     * @param   e   The AWT KeyEvent that occurred.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        _processKeyEvent(e);
    } // keyPressed(KeyEvent e)

    /**
     * Captures AWT key released events and routes them to the embedded
     * stage as appropriate.
     * 
     * @param   e   The AWT KeyEvent that occurred.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        _processKeyEvent(e);
    } // keyReleased(KeyEvent e)

} // class DrawablePanel
