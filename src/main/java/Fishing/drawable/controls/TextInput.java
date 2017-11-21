
package Fishing.drawable.controls;

import Fishing.drawable.Drawable;
import Fishing.drawable.events.DrawableFocusEvent;
import Fishing.drawable.events.DrawableFocusListener;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableKeyListener;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.events.DrawableMouseListener;
import Fishing.drawable.events.ValueChangedEvent;
import Fishing.drawable.events.ValueChangedListener;
import Fishing.drawable.text.BitmapFont;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


/**
 *
 * @author Brad
 */
public class TextInput
    extends Drawable
    implements DrawableMouseListener, DrawableKeyListener, DrawableFocusListener
{

    private BitmapFont font;
    private String text = "";
    private double padding = 3;
    private Color backgroundColor = Color.WHITE;
    private Color borderColor = Color.DARK_GRAY;
    private double borderWidth = 1;
    private int caretPosition = 0;
    private boolean insertMode = true;
    private int maxChars = -1;
    private String allowedChars = null;

    private boolean mouseOver = false;  // Whether to draw the item highlighted or not
    private boolean hasFocus = false;   // Whether the control is the current focus or not


    public TextInput( BitmapFont font ) {
        setFont(font);
        setMouseEnabled(true);
        setFocusable(true);

        setUnscaledWidth( font.getFontSize() * 10 + 2 * padding );

        addKeyListener(this);
        addMouseListener(this);
        addFocusListener(this);
    } // TextInput( BitmapFont font )


    private ArrayList<ValueChangedListener> valueChangedListeners;

    public synchronized void addValueChangedListener( ValueChangedListener listener ) {
        if (listener == null)
            throw new IllegalArgumentException("The listener parameter cannot be null");

        if (valueChangedListeners == null)
             valueChangedListeners = new ArrayList<ValueChangedListener>();
        else if (valueChangedListeners.contains(listener))
            return;
        else
            valueChangedListeners = (ArrayList<ValueChangedListener>) valueChangedListeners.clone();

        valueChangedListeners.add(listener);
    } // addValueChangedListener( ValueChangedListener listener )

    public synchronized void removeValueChangedListener( ValueChangedListener listener ) {
        if (valueChangedListeners == null)
            return;

        ArrayList<ValueChangedListener> l = (ArrayList<ValueChangedListener>) valueChangedListeners.clone();
        if (l.remove(listener)) {
            if (l.isEmpty())
                valueChangedListeners = null;
            else
                valueChangedListeners = l;
        }
    } // removeValueChangedListener( ValueChangedListener listener )


    public synchronized void setFont( BitmapFont value ) {
        if (value == null)
            throw new IllegalArgumentException("The font canot be null.");

        font = value;

        setUnscaledHeight( font.getFontSize() + 2 * padding );
    } // setFont( BitmapFont value )


    public boolean getInsertMode() {
        return insertMode;
    } // getInsertMode();


    public void setInsertMode( boolean value ) {
        insertMode = value;
    } // setInsertMode( value )


    public int getMaxChars() {
        return maxChars;
    } // getMaxChars()

    
    public void setMaxChars( int value ) {
        if (value < -1)
            throw new IllegalArgumentException("The maxChars cannot be set less than -1.");

        maxChars = value;
    } // setMaxChars( int value )


    public String getAllowedChars() {
        return allowedChars;
    } // getAllowedChars()


    public void setAllowedChars( String value ) {
        allowedChars = value;
    } // setAllowedChars( String value )


    public BitmapFont getFont() {
        return font;
    } // getFont()


    public String getText() {
        return text;
    } // getText()


    public synchronized void setText(String value) {
        if (value == null)
            value = "";

        String oldText = text;
        text = value;
        if (caretPosition > text.length())
            caretPosition = text.length();

        if (valueChangedListeners != null) {
            ValueChangedEvent evt = new ValueChangedEvent(this, oldText, text);

            for (ValueChangedListener listener : valueChangedListeners) {
                listener.valueChanged(evt);
                if (evt.isImmediatePropagationStopped())
                    break;
            } // for
        }
    } // setText(String value)


    public int getCaretPosition() {
        return caretPosition;
    } // 


    public synchronized void setCaretPosition( int value ) {
        if (caretPosition < 0)
            throw new IllegalArgumentException("The caret position cannot be less than 0.");

        if (caretPosition > text.length())
            throw new IllegalArgumentException("The caret position cannot exceed the length of the text.");

        caretPosition = value;
    } // setCaretPosition( int value )


    public double getPadding() {
        return padding;
    } // getPadding()


    public void setPadding( double value ) {
        if (value < 0)
            throw new IllegalArgumentException("The padding cannot be less than 0.");

        padding = value;
    } // setPadding( double value )


    public Color getBackgroundColor() {
        return backgroundColor;
    } // getBackgroundColor()


    public void setBackgroundColor( Color value ) {
        backgroundColor = value;
    } // setBackgroundColor( Color value )


    public double getBorderWidth() {
        return borderWidth;
    } // getBorderWidth()


    public void setBorderWidth( double value ) {
        if (value < 0)
            throw new IllegalArgumentException("The borderWidth cannot be less than 0.");

        borderWidth = value;
    } // setBorderWidth( double value )


    public int getCharIndexFromPoint( Point2D pt ) {
System.out.println("TODO: Implement me!: getCharIndexFromPoint");
        return text.length();
    } // getCharIndexFromPoint( Point2D pt )


    @Override
    public void paint( Graphics2D g ) {
        double w = getUnscaledWidth();
        double h = getUnscaledHeight();

        // Draw the frame
        Rectangle2D r = new Rectangle2D.Double( borderWidth / 2, borderWidth / 2, w - borderWidth, h - borderWidth );

        if (backgroundColor != null) {
            Color c = (mouseOver || hasFocus ? backgroundColor.brighter() : backgroundColor);
            g.setColor( c );
            g.fill( r );
        }

        if (borderColor != null) {
            g.setColor( borderColor );
            g.draw( r );
        }

        // Set clipping
        Shape s = g.getClip();

        g.setClip( new Rectangle2D.Double( borderWidth, borderWidth, w - borderWidth * 2, h - borderWidth * 2 ) );

// TODO: If implementing selection, then should probably draw it here

        // Draw the text

// TODO: Could implement different alignments here

        double x = padding;
        int align = BitmapFont.ALIGN_LEFT;

        font.drawText(g, x, padding, text, align);

        // Draw the caret if focused
        if (hasFocus) {
            x = padding + font.getCharacterOffset( text, caretPosition );

            double caretSize = 4;
            if (!insertMode)
                caretSize = font.getFontSize() / 2;

// Using XOR mode here makes performance really suck...
            g.setColor( backgroundColor != null
                        ? new Color(((backgroundColor.getRGB() & 0xffffff) ^ 0xffffff))
                        : Color.BLACK );
//            g.setXORMode( Color.WHITE );
            g.fill( new Rectangle2D.Double( x, padding, caretSize, font.getFontSize() - 1 ) );
//            g.setPaintMode();
        }

        // Restore clipping
        g.setClip(s);
    } // paint( Graphics2D g )


    private void insertChars( int index, String value ) {
        String t = text.substring(0, index);

        if (value != null)
            t += value;

        if (index < text.length())
            t += text.substring(index);

        setText(t);
    } // insertChars( int position, String value )


    private void replaceChars( int fromIndex, int toIndex, String value ) {
        String t = text.substring(0, fromIndex);

        if (value != null)
            t += value;

        if (toIndex < text.length() - 1)
            t += text.substring(toIndex + 1);

        setText(t);
    } // replaceChars( int fromIndex, int toIndex, String value )


    private void removeChars( int fromIndex, int toIndex ) {
        replaceChars( fromIndex, toIndex, null );
    } // removeChars( int fromIndex, int toIndex )


    public void drawableMouseMoved(DrawableMouseEvent e) { }
    public void drawableMouseClicked(DrawableMouseEvent e) { }

    public void drawableMouseDragged(DrawableMouseEvent e) {
        // TODO: Could do some sort of selection here..
    } // drawableMouseDragged(DrawableMouseEvent e)

    public void drawableMouseReleased(DrawableMouseEvent e) {
        // TODO: If doing selection, then end the selection mode here
    } // drawableMouseReleased(DrawableMouseEvent e)

    public void drawableMousePressed(DrawableMouseEvent e) {
        setFocus();

        int pos = getCharIndexFromPoint( e.getPosition() );
        if (pos != -1)
            setCaretPosition(pos);
    } // drawableMousePressed(DrawableMouseEvent e)

    public void drawableMouseEntered(DrawableMouseEvent e) {
        mouseOver = true;
    } // drawableMouseEntered(DrawableMouseEvent e)

    public void drawableMouseExited(DrawableMouseEvent e) {
        mouseOver = false;
    } // drawableMouseExited(DrawableMouseEvent e)



    public void drawableKeyReleased(DrawableKeyEvent e) { }


    public synchronized void drawableKeyPressed(DrawableKeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (caretPosition > 0)
                    setCaretPosition( caretPosition - 1 );
                break;
            case KeyEvent.VK_RIGHT:
                if (caretPosition < text.length())
                    setCaretPosition( caretPosition + 1 );
                break;
            case KeyEvent.VK_HOME:
                setCaretPosition(0);
                break;
            case KeyEvent.VK_END:
                setCaretPosition( text.length() );
                break;
            case KeyEvent.VK_BACK_SPACE:
                if (caretPosition > 0) {
                    setCaretPosition(caretPosition - 1);
                    removeChars(caretPosition, caretPosition);
                }
                break;
            case KeyEvent.VK_DELETE:
                if (caretPosition < text.length())
                    removeChars(caretPosition, caretPosition);
                break;
            case KeyEvent.VK_INSERT:
                insertMode = !insertMode;
                break;
        } // switch
    } // drawableKeyPressed(DrawableKeyEvent e)

    public void drawableKeyTyped(DrawableKeyEvent e) {
        char c = e.getKeyChar();

        switch (Character.getType(c)) {
            case Character.CONTROL:
            case Character.FORMAT:
                // Ignore control and formatting characters?
                return;
        } // switch

        int len = text.length();
        if (insertMode) {
            if ((maxChars == -1) || (len < maxChars)) {
                insertChars( caretPosition, ""+c );
                setCaretPosition( caretPosition + 1 );
            }
        } else if ((maxChars == -1) || (caretPosition < len) || (len < maxChars)) {
            replaceChars( caretPosition, caretPosition, ""+c );
            setCaretPosition( caretPosition + 1 );
        }
    } // drawableKeyTyped(DrawableKeyEvent e)


    public void drawableFocusGained(DrawableFocusEvent e) {
        hasFocus = true;
    } // drawableFocusGained(DrawableFocusEvent e)

    public void drawableFocusLost(DrawableFocusEvent e) {
        hasFocus = false;
    } // drawableFocusLost(DrawableFocusEvent e)


} // class TextInput
