
package Fishing.drawable.controls;

import Fishing.drawable.Drawable;
import Fishing.drawable.Animation;
import Fishing.drawable.SpriteFrameSet;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Brad
 */
public class Fish
    extends Animation
{

    private static final int DECIDE_NONE    = 0;
    private static final int DECIDE_REVERSE = 1;
    private static final int DECIDE_UP_DOWN = 2;

    private long decisionMakingFrequency = 1000;    // Number of milliseconds between choosing whether to make a decision
    private long decisionCooldownTime = 3000;       // Time after making a decision before another decision can be made
    private long decisionTime = 0;                  // Time when a decision will be made
    private long nextReverseDecisionTime = 0;       // Time when a decision to reverse can be made
    private long reverseDecisionCooldownTime = 2000;    // Time after reversing before another reverse decision can be made

    private double targetY = Double.NaN;    // If moving vertically, the position on the screen to move to
    private double deltaX = 0;              // Horizontal speed
    private double deltaY = 0;              // Vertical speed

    private String species;
    private int score;


    public Fish( SpriteFrameSet frameset, int score, String species ) {
        super(frameset);

        this.score = score;
        this.species = species;

        setFrameRate(1.0 / 1000);

        nextReverseDecisionTime = System.currentTimeMillis() + reverseDecisionCooldownTime;
    } // Fish( SpriteFrameSet frameset, int score, String species )

    
    public String getSpecies() {
        return species;
    } // getSpecies()


    public int getScore() {
        return score;
    } // getScore()

    
    public void setScore( int value ) {
        score = value;
    } // setScore( int value )

    public double getDeltaX() {
        return deltaX;
    } // getDeltaX()

    public void setDeltaX(double value) {
        if ((value < 0 ? -1 : 1) != (deltaX < 0 ? -1 : 1))
            setScaleX( getScaleX() * -1 );
        
        if (value > 10)
            value = 10;
        else if (value < -10)
            value = -10;

        deltaX = value;

        setFrameRate( 7 * Math.abs(deltaX) / 3 );
    } // setDeltaX(double value)

    public double getDeltaY() {
        return deltaY;
    } // getDeltaY()

    public void setDeltaY(double value) {
        if (value > 1)
            value = 1;
        else if (value < -1)
            value = -1;

        deltaY = value;

        // Assume change in direction was a decision
        decisionTime = System.currentTimeMillis() + decisionCooldownTime;
    } // setDeltaY(double value)

    public void animate( Rectangle2D bounds ) {
        double x = getX() + deltaX;
        double y = getY() + deltaY;
        setPosition( x, y );

        // Ensure the fish doesn't go past the target Y position
        if (!Double.isNaN(targetY)) {
            if (deltaY < 0) {
                if (targetY >= y) {
                    targetY = Double.NaN;
                    deltaY = 0;
                }
            } else if (deltaY > 0) {
                if (targetY <= y) {
                    targetY = Double.NaN;
                    deltaY = 0;
                }
            } else
                targetY = Double.NaN;
        }

        double minX = bounds.getMinX();
        double maxX = bounds.getMaxX();

/*
        // Reverse horizontal direction if needed
        if (deltaX < 0) {
            if ((x < minX) && (x < minX - getWidth() / 2))
                setDeltaX(deltaX * -1);
        } else if (x > maxX - getWidth() / 2)
            setDeltaX(deltaX * -1);
*/
        // Remove fish when they hit the edge so new fish can take their place
        boolean remove = false;
        if (deltaX < 0) {
            if ((x < minX) && (x < minX - getWidth() / 2))
                remove = true;
        } else if (x > maxX - getWidth() / 2)
            remove = true;
        Drawable p = getParent();
        if ((p != null) && remove)
            p.removeDrawable(this);

        long currentTime = System.currentTimeMillis();
        if (decisionTime <= currentTime) {
            // Make a decision
            int decision = (int)Math.round(Math.random() * 3);
            switch (decision) {
                case DECIDE_NONE:
                    break;

                case DECIDE_REVERSE:
                    if (nextReverseDecisionTime < currentTime) {
                        // The chance to actually reverse depends on how close to the center we are
//                        double cx = Math.abs((x - minX) - bounds.getWidth() / 2) / (bounds.getWidth() / 2);
//                        cx = Math.random() * (1 - cx);
//                        if (cx > 0.2) {
                            setDeltaX(deltaX * -1);
                            nextReverseDecisionTime = currentTime + reverseDecisionCooldownTime;
//                        } else
//                            decision = DECIDE_NONE;
                    } else
                        decision = DECIDE_NONE;
                    break;

                case DECIDE_UP_DOWN:
                    if (Double.isNaN(targetY)) {
                        // Choose where to go to
                        targetY = bounds.getX() + Math.random() * bounds.getHeight();

                        // Choose a speed
                        double d = deltaX * 0.25;
                        deltaY = (deltaX  + (Math.random() * d) - d/2) * 0.5;
                        if (targetY < y)
                            deltaY = -deltaY;
                    } else
                        decision = DECIDE_NONE;

                    break;
            } // switch

            decisionTime = System.currentTimeMillis() + (decision == DECIDE_NONE ? decisionMakingFrequency : decisionCooldownTime);
        }

    } // animate()

} // class Fish
