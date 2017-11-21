
package Fishing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for managing high scores.
 * @author Brad
 */
public class HighScoreManager {

    /**
     * Path to the file containing the serialized high score data.
     * This file will be both read and written by the application.
     */
    private static final String highScoreFile = "highScores.ser";

    /**
     * Map of type ("beginner", "intermediate", "expert") to list of high
     * scores for that type.
     */
    private static HashMap<String,HighScore[]> highScores = null;

    /**
     * Retrieves the list of high scores for a specified type.
     * 
     * If no entry exists yet for the specified type, an new empty entry
     * will be created and returned.
     * 
     * @param type  The type to retrieve the high scores for (eg.
     *              "beginner", "intermediate" or "expert")
     *
     * @return  The list of high scores for the specified type.
     */
    private static HighScore[] getScores( String type ) {
        if (highScores == null)
            readHighScores();

        type = type.toLowerCase();
        HighScore[] highScore = highScores.get(type);
        if (highScore == null) {
            highScore = new HighScore[10];
            highScores.put(type, highScore);
        }

        return highScore;
    } // getScores( String type )

    /**
     * Inserts the specified score and initials to the list of high scores for a
     * given type, iff the score is higher than an existing entry.
     * 
     * If the new score is inserted, the entry with the previously lowest score,
     * or an unassigned entry if one exists, will be removed from the list.
     * 
     * @param type      The type of high score to insert.
     * @param score     The score value to insert.
     * @param initials  The player initials to associate for the score.
     */
    public static void setHighScore( String type, long score, String initials ) {
        HighScore[] highScore = getScores( type );

        // Find insertion position in the high score list
        int p = getInsertionPos( score, highScore );
        if (p == -1)    // Doesn't make the cut
            return;

        // Move items down
        for (int i = highScore.length - 1; i > p; --i) {
            highScore[i] = highScore[i - 1];
        } // for

        // Insert the new item
        highScore[p] = new HighScore( score, initials );

        writeHighScores();
    } // setMaxScore( String type, long score, String initials )

    /**
     * Determines at what position an entry with the specified score should be
     * inserted into the specified array.
     * 
     * @param score     The score to find the insertion position for.
     * @param highScore The list of HighScore entries to search.
     * 
     * @return  The insertion point where an entry with the given score should
     *          be inserted, or -1 if there are no unassigned entries and the
     *          score is not higher than an existing entry.
     */
    private static int getInsertionPos( long score, HighScore[] highScore ) {
        int p = -1;
        for (int i = 0; i < highScore.length; ++i) {
            HighScore s = highScore[i];
            if ((s == null) || (s.getScore() < score)) {
                p = i;
                break;
            }
        } // for
        return p;
    } // getInsertionPos( long score, HighScore[] highScore )

    /**
     * Returns the rank at which the given score places, or 0 if it doesn't
     * make the cut.
     * 
     * @param type  The type of high score.
     * @param score The score to find the rank for.
     * 
     * @return  The rank at which the given score places, or 0 if it doesn't
     *          make the cut.
     */
    public static int getRankForScore( String type, long score )  {
        HighScore[] highScore = getScores( type );

        // Find insertion position in the high score list
        return getInsertionPos( score, highScore ) + 1;
    } // getRankForScore( String type, long score )

    /**
     * Retrieves the recorded high scores for the specified type.
     *
     * @param type  The type to retrieve the high scores for (eg.
     *              "beginner", "intermediate" or "expert")
     *
     * @return 
     */
    public static List<HighScore> getHighScores( String type ) {
        return Arrays.asList(getScores( type ));
    } // getHighScores( String type )

    /**
     * Writes the current state of the managed high score tables to the high
     * score file.
     */
    public synchronized static void writeHighScores() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(highScoreFile))) {
            out.writeObject(highScores);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(HighScoreManager.class.getName()).log(
                Level.SEVERE,
                "IOException while writing high score file '"+ highScoreFile +"'",
                ex
            );
        }
    } // writeHighScores()

    /**
     * Reads any previously recorded high score data from the
     * {@code highScoreFile} and caches it in a static property.
     */
    private synchronized static void readHighScores() {
        try {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(highScoreFile))) {
                @SuppressWarnings("unchecked")
                HashMap<String,HighScore[]> scores = (HashMap<String,HighScore[]>) in.readObject();

                highScores = scores;
            } catch (ClassNotFoundException|ClassCastException ex) {
                // This generally shouldn't happen, but may be possible if the
                // high score file is corrupted
                Logger.getLogger(HighScoreManager.class.getName()).log(
                    Level.SEVERE,
                    "Unable to read high score file",
                    ex
                );
                highScores = new HashMap<>();
            }
        } catch (IOException ex) {
            //Logger.getLogger(HighScoreManager.class.getName()).log(Level.SEVERE, "Unable to read high score file", ex);
            highScores = new HashMap<>();
        }
    } // readHighScores()

    /**
     * Prevent instantiation of this utility class.
     */
    private HighScoreManager() {
    } // HighScoreManager()

} // class HighScoreManager
