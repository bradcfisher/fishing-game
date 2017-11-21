
package Fishing.drawable.controls;

import Fishing.Resources;
import Fishing.drawable.Drawable;
import Fishing.drawable.Animation;
import Fishing.drawable.SpriteFrameSet;
import Fishing.drawable.SpriteSheet;
import java.io.IOException;

/**
 * Implementation of a scrolling numeric scoring display.
 * 
 * @author Brad
 */
public class ScoreDisplay
    extends Drawable
{

// TODO: Figure out why score display doesn't seem to reset all the digits correctly

    /**
     * The width of each frame in the sprite sheet.
     */
    private static final int frameWidth = 40;

    /**
     * The height of each frame in the sprite sheet.
     */
    private static final int frameHeight = 52;

    /**
     * The number of frames per row in the sprite sheet.
     */
    private static final int framesPerRow = 5;

    /**
     * The current score value to display.
     */
    private long score = 0;

    /**
     * The number of digits to display.
     */
    private int numDigits;

    /**
     * The SpriteFrameSet being used for each digit of the scoring display.
     */
    private SpriteFrameSet frameset;

    /**
     * Constructs a new instance using the default sprite sheet
     * 'assets/controls/hud_numbers_big.png', an initial score value of 0
     * and displaying 5 digits.
     */
    public ScoreDisplay() {
        SpriteSheet sheet;
        try {
            sheet = new SpriteSheet( Resources.getStream("assets/controls/hud_numbers_big.png") );
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load hud sprite resource");
        } // try/catch

        frameset = new SpriteFrameSet(sheet, frameWidth, frameHeight, framesPerRow, 50, true, 0, 0);

        setNumDigits(5);
    } // ScoreDisplay()

    /**
     * Resets the current score value to 0 and starts an animation to update
     * the display to reflect the new value.
     */
    public void reset() {
        score = 0;
        setFrames(false, Animation.Direction.FORWARD);
    } // reset()

    /**
     * 
     * @param animate   Whether to animate the transition for each digit or
     *                  immediately jump to the appropriate frame.
     * @param direction The play direction to apply.
     */
    private synchronized void setFrames( boolean animate, Animation.Direction direction ) {
        // Update the animations to display the new values
        long s = score;
        for (int i = 0; i < numDigits; ++i) {
            Animation a = (Animation) getDrawableAt(i);
            a.setPlayDirection(direction);
            if (animate)
                a.runToFrameIndex( framesPerRow * (int)(s % 10) );
            else {
                a.gotoFrameIndex( framesPerRow * (int)(s % 10) );
            }
            s /= 10;

        } // for
    } // setFrames( boolean animate, int direction )

    /**
     * Retrieves the current score value.
     * 
     * @return The current score value associated with this instance.
     */
    public long getScore() {
        return score;
    } // getScore()

    /**
     * Sets the current score value, and starts an animation to display the
     * new value.
     * 
     * @param value The new score value to display.
     */
    public void setScore( long value ) {
        setScore(value, true);
    } // setScore( long value )

    /**
     * Sets the current score value, with or without animation.
     * 
     * @param value The new score value to display.
     * @param animate Whether to start an animation to update the display
     *              (true), or update the display immediately (false).
     */
    public synchronized void setScore( long value, boolean animate ) {
        if (value < 0)
            value = 0;

        long oldScore = score;
        score = value;
        setFrames(animate,
            (score < oldScore
                ? Animation.Direction.BACKWARD
                : Animation.Direction.FORWARD)
        );
    } // setScore( long value, boolean animate )

    /**
     * Adds a specified value to the current score for this instance, and
     * starts an animation to display the new value.
     * 
     * @param value The value to add to the current score.
     */
    public synchronized void addToScore( long value ) {
        value += score;
        if (value < 0)
            value = 0;

        setScore( value );
    } // addToScore( long value )

    @Override
    public void setUnscaledWidth( double value ) {
        throw new UnsupportedOperationException("ScoreDisplay doesn't support setting unscaled width");
    } // setUnscaledWidth( double value )

    @Override
    public void setUnscaledHeight( double value ) {
        throw new UnsupportedOperationException("ScoreDisplay doesn't support setting unscaled height");
    } // setUnscaledHeight( double value )

    @Override
    public void addDrawable( Drawable obj ) {
    } // addDrawable( Drawable obj )

    @Override
    public void removeDrawable( Drawable obj ) {
    } // removeDrawable( Drawable obj )

    @Override
    public void removeAllDrawables() {
    } // removeAllDrawables()

    /**
     * Retrieves the number of digits being displayed by this instance.
     * 
     * @return The number of digits being displayed by this instance.
     */
    public int getNumDigits() {
        return numDigits;
    } // getNumDigits()

    public synchronized void setNumDigits( int value ) {
        if ((value < 1) || (value > 18))
            throw new IllegalArgumentException("The digits parameter must be between 1 and 18");

        super.setUnscaledWidth(frameWidth * value);
        super.setUnscaledHeight(frameHeight);

        this.numDigits = value;

        super.removeAllDrawables();
        for (int i = 0; i < value; ++i) {
            Animation a = new Animation(frameset);
            a.setCenterX(0);
            a.setCenterY(0);
            a.setRepeat(true);
            a.setFrameRate(15);
            a.setX( getUnscaledWidth() - (i + 1) * frameWidth );
            super.addDrawable(a);
        } // for
    } // setNumDigits( int value)

    public synchronized void finishAnimation() {
        for (int i = 0; i < getNumDigits(); ++i) {
            Animation a = (Animation) getDrawableAt(i);

            int targetIndex = a.getTargetFrameIndex();
            if (targetIndex != -1)
                    a.gotoFrameIndex( targetIndex );
        } // for
    } // finishAnimation()

} // class ScoreDisplay
