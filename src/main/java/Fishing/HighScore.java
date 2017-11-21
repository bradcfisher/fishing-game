
package Fishing;

import java.io.Serializable;

/**
 * Represents a high score entry in the hall of fame.
 * @author Brad
 */
public class HighScore
    implements Serializable
{

    /**
     * The score the player achieved.
     */
    private final long score;

    /**
     * The initials of the player.
     */
    private final String initials;

    /**
     * Constructs a new instance.
     * 
     * @param score     The score the player achieved.
     * @param initials  The initials of the player.
     */
    public HighScore( long score, String initials ) {
        this.score = score;
        this.initials = initials;
    } // HighScore( int score, String intials )

    /**
     * Retrieves the score the player achieved.
     * @return The score the player achieved.
     */
    public long getScore() {
        return score;
    } // getScore()

    /**
     * Retrieves the initials of the player.
     * @return The initials of the player.
     */
    public String getInitials() {
        return initials;
    } // getInitials()

} // class HighScore
