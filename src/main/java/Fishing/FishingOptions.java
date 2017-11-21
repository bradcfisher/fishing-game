
package Fishing;

import Fishing.drawable.SpriteFrameSet;
import Fishing.drawable.SpriteSheet;
import Fishing.drawable.controls.Fish;
import Fishing.drawable.events.EventBase;
import Fishing.drawable.events.FishingOptionsListener;
import Fishing.drawable.events.ValueChangedEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class encapsulates the data necessary to control the various options
 * needed by the {@link Fishing.screens.FishTankScreen}.
 *
 * @author Brad
 */
public class FishingOptions {

    /**
     * The title to display at the top of the game screen.
     */
    private String title;

    /**
     * The number of seconds to run the game.
     */
    private int timeLimit = 120;

    /**
     * Maximum number of fish to allow on the display at any one time.
     */
    private int maxFish = 5;

    /**
     * Maximum movement speed for a fish.
     */
    private double maxFishSpeed = 4;

    /**
     * Minimum speed for a fish.
     */
    private double minFishSpeed = 2;

    /**
     * Map of fish species names to the FishFrameset instances containing the
     * properties for the respective species.
     * 
     * Possible key values are: "discus", "clown", "angel", "guppy", "tetra",
     * "tigerbarb", "zebra", "cod", and "shark"
     */
    private final Map<String, FishFrameset> fishFramesets;

    // Max # of fish species to use
    private double fishSpeciesWeightFactor;

    /**
     * The set of all fish species that are enabled.  At least one species must
     * be enabled.
     * 
     * Only species matching an entry in {@code fishFramesets} are permitted in
     * this list.
     */
    private List<String> enabledFishSpecies;

    /**
     * Set of fish species that the user is targeting and which will provide
     * positive points when caught.
     * 
     * Only species matching an entry in {@code fishFramesets} are permitted in
     * this list.
     */
    private List<String> targetFishSpecies;

    /**
     * The probability that a new fish will be added during an animation update.
     */
    private double newFishProbability = 0.02;

    /**
     * The final score achieved by the user before time ran out.
     */
    private long finalScore = 0;

    /**
     * The registered FishingOptionListeners.
     */
    private List<FishingOptionsListener> listeners;

    /**
     * Class encapsulating the data describing a species of fish that may be
     * represented in the game.
     */
    class FishFrameset {
        /**
         * The name of the fish species that this frameset is defined for.
         */
        String species;

        /**
         * The sprite animation for this fish species.
         */
        SpriteFrameSet frameset;

        /**
         * Horizontal scaling factor to apply when rendering this fish species.
         */
        double scaleX;

        /**
         * Vertical scaling factor to apply when rendering this fish species.
         */
        double scaleY;

        /**
         * Score for capturing a fish of this species.
         * If the species is listed in {@code targetFishSpecies}, this score
         * will be added to the user's current score.  Otherwise, the score
         * will be subtracted from the current score.
         */
        int score;

        /**
         * Weight to apply to influence the probability of a new fish of this
         * species being added when a new fish is added to the display.
         */
        double probabilityWeight;

        /**
         * The number of fish of this species that has been captured.
         */
        int numCaptured = 0;

        /**
         * Constructs a new instance.
         * 
         * @param species   The name of the fish species that this frameset is
         *                  defined for.
         * @param frameset  The sprite animation for this fish species.
         * @param score     Score for capturing a fish of this species.
         * @param scale     Scaling factor to apply to both the horizontal and
         *                  vertical axes when rendering a fish of this species.
         * @param flip      Whether to flip the image on the horizontal axis for
         *                  this species.  When flip is {@code false}, it is
         *                  assumed that the sprite images are drawn with the
         *                  fish facing to the right.  A value of {@code true}
         *                  indicates the fish faces to the left in the
         *                  sprite images, and should be flipped horizontally to
         *                  normalize the images so they point to the right.
         */
        public FishFrameset(String species, SpriteFrameSet frameset, int score, double scale, boolean flip) {
            this.species = species;
            this.frameset = frameset;
            this.scaleX = (flip ? -scale : scale);
            this.scaleY = scale;
            this.score = score;
            this.probabilityWeight = 1;
        } // FishFrameset(SpriteFrameSet frameset, int score, double scale, boolean flip)

    } // class FishFrameset

    /**
     * Constructs a new instance with default values for all options.
     * @throws  IOException if a fish species frameset cannot be loaded.
     */
    public FishingOptions()
        throws IOException
    {
        enabledFishSpecies = new ArrayList<>();
        targetFishSpecies = new ArrayList<>();
        fishFramesets = new HashMap<>();

        addFishFrameset( "discus", 1, "assets/fish/discus2_spritesheet.png", 250, 272, 4, 8, 0.6 /*0.9675*/, false );
        addFishFrameset( "clown", 60, "assets/fish/clownfish3_spritesheet.png", 250, 128, 4, 8, 0.7 /*1.0*/, false );
        addFishFrameset( "angel", 50, "assets/fish/angelfish_spritesheet.png", 117, 148, 4, 8, 1.3, true ); // flip
        addFishFrameset( "guppy", 200,"assets/fish/guppy2_spritesheet.png", 109, 67, 3, 6, 0.957, true ); //flip
        addFishFrameset( "tetra", 325, "assets/fish/tetra_spritesheet.png", 94, 44, 4, 8, 0.957, false );
        addFishFrameset( "tigerbarb", 250, "assets/fish/tigerbarb_spritesheet.png", 96, 57, 4, 8, 1.406, true ); // flip
        addFishFrameset( "zebra", 500, "assets/fish/zebra_spritesheet.png", 138, 45, 4, 8, 0.9 /*0.5*/, true ); // flip
        addFishFrameset( "cod", 5, "assets/fish/cod2_spritesheet.png", 266, 104, 4, 8, 1.0 /*1.6*/, false );
        addFishFrameset( "shark", -100, "assets/fish/shark3_spritesheet.png", 484, 164, 4, 8, 1.5 /*2.0*/, false );
    } // FishingOptions()

    /**
     * Retrieves the title to display at the top of the game screen.
     * @return The title to display at the top of the game screen.
     */
    public String getTitle() {
        return title;
    } // getTitle()

    /**
     * Sets the title to display at the top of the game screen.
     *
     * After the {@code title} value is updated, this method will invoke the
     * {@code titleChanged} method on all registered
     * {@link FishingOptionsListener}s.
     * 
     * @param value The title to display at the top of the game screen.
     */
    public synchronized void setTitle( String value ) {
        String old = title;
        title = value;

        EventBase.notifyListeners(
            listeners,
            new ValueChangedEvent(this, old, title),
            FishingOptionsListener::titleChanged
        );
    } // setValue( String value )

    /**
     * Retrieves the final score achieved by the user before time ran out.
     * @return The final score achieved by the user before time ran out.
     */
    public long getFinalScore() {
        return finalScore;
    } // getFinalScore()

    /**
     * Sets the final score achieved by the user before time ran out.
     * @param value The new {@code finalScore} value.
     */
    public void setFinalScore( long value ) {
        finalScore = value;
    } // setFinalScore( long value )

    /**
     * Resets the statistics captured from a prior round back to 0.
     * 
     * This resets the {@code finalScore} property of this object, as well as
     * the {@code numCaptured} property for each defined fish species.
     */
    public void resetStatistics() {
        finalScore = 0;

        for (String species : fishFramesets.keySet()) {
            FishFrameset f = fishFramesets.get(species);
            f.numCaptured = 0;
        } // for
    } // resetStatistics()

    /**
     * Retrieves the probability that a new fish will be added during an
     * animation update.
     *
     * @return  The probability that a new fish will be added during an
     *          animation update.
     */
    public double getNewFishProbability() {
        return newFishProbability;
    } // getNewFishProbability()
    
    /**
     * Sets the probability that a new fish will be added during an
     * animation update.
     *
     * @param   value   The new probability that a new fish will be added
     *                  during an animation update.  This value must be between
     *                  0 and 1, inclusive.
     *
     * @throws  IllegalArgumentException if the new {@code value} is outside
     *          the range of 0 to 1.
     */
    public void setNewFishProbability( double value ) {
        if ((value < 0) || (value > 1))
            throw new IllegalArgumentException("The newFishProbability cannot be less than 0 or greater than 1");

        newFishProbability = value;
    } // setNewFishProbability( double value )

    /**
     * Retrieve the maximum number of fish to allow on the display at any one
     * time.
     * 
     * @return  The maximum number of fish to allow on the display at any one
     *          time.
     */
    public double getMaxFish() {
        return maxFish;
    } // getMaxFish()

    /**
     * Sets the maximum number of fish to allow on the display at any one time.
     * 
     * After the {@code maxFish} value is updated, this method will invoke the
     * {@code maxFishChanged} method on all registered
     * {@link FishingOptionsListener}s.
     * 
     * @param   value   The new maximum number of fish to allow.
     * 
     * @throws  IllegalArgumentException if the new {@code value} is less
     *          than 0.
     */
    public synchronized void setMaxFish( int value ) {
        if (value < 0)
            throw new IllegalArgumentException("The maxFish property cannot be less than 0.");

        int old = maxFish;
        maxFish = value;

        EventBase.notifyListeners(
            listeners,
            new ValueChangedEvent(this, old, maxFish),
            FishingOptionsListener::maxFishChanged
        );
    } // setMaxFish()

    /**
     * Retrieves the maximum movement speed for a fish.
     * 
     * @return The maximum movement speed for a fish.
     */
    public double getMaxFishSpeed() {
        return maxFishSpeed;
    } // getMaxFishSpeed()

    /**
     * Sets the maximum movement speed for a fish.
     * 
     * After the {@code maxFishSpeed} value is updated, this method will invoke
     * the {@code maxFishSpeedChanged} method on all registered
     * {@link FishingOptionsListener}s.
     * 
     * @param   value   The new maximum movement speed for a fish.
     * 
     * @throws  IllegalArgumentException if {@value} is not greater than or
     *          equal to the {@code minFishSpeed}.
     */
    public synchronized void setMaxFishSpeed( double value ) {
        if (value <= minFishSpeed)
            throw new IllegalArgumentException("The maxFishSpeed must be greater than the minFishSpeed.");

        double old = maxFishSpeed;
        maxFishSpeed = value;

        EventBase.notifyListeners(
            listeners,
            new ValueChangedEvent(this, old, maxFishSpeed),
            FishingOptionsListener::maxFishSpeedChanged
        );
    } // setMaxFishSpeed( double value )

    /**
     * Retrieves the minimum movement speed for a fish.
     * @return The minimum movement speed for a fish.
     */
    public double getMinFishSpeed() {
        return minFishSpeed;
    } // getMinFishSpeed()

    /**
     * Sets the minimum movement speed for a fish.
     * 
     * After the {@code minFishSpeed} value is updated, this method will invoke
     * the {@code minFishSpeedChanged} method on all registered
     * {@link FishingOptionsListener}s.
     * 
     * @param   value   The new minimum movement speed for a fish.
     * 
     * @throws  IllegalArgumentException if {@value} is not greater than 0 or
     *          is greater than {@code maxFishSpeed}.
     */
    public synchronized void setMinFishSpeed( double value ) {
        if (value <= 0)
            throw new IllegalArgumentException("The minFishSpeed must be greater than 0.");

        if (value > maxFishSpeed)
            throw new IllegalArgumentException("The minFishSpeed must be less than the maxFishSpeed.");

        double old = minFishSpeed;
        minFishSpeed = value;

        EventBase.notifyListeners(
            listeners,
            new ValueChangedEvent(this, old, minFishSpeed),
            FishingOptionsListener::minFishSpeedChanged
        );
    } // setMinFishSpeed( double value )

    /**
     * Retrieves the number of seconds to run the game.
     * 
     * After the {@code timeLimit} value is updated, this method will invoke
     * the {@code timeLimitChanged} method on all registered
     * {@link FishingOptionsListener}s.
     * 
     * @return The number of seconds to run the game.
     */
    public int getTimeLimit() {
        return timeLimit;
    } // getTimeLimit()

    /**
     * Sets the number of seconds to run the game.
     * 
     * @param   value   The new number of seconds to run the game.
     * 
     * @throws  IllegalArgumentException if {@value} is not greater than 0.
     */
    public synchronized void setTimeLimit( int value ) {
        if (timeLimit < 0)
            throw new IllegalArgumentException("The timeLimit must be greater than 0.");

        int old = timeLimit;
        timeLimit = value;

        EventBase.notifyListeners(
            listeners,
            new ValueChangedEvent(this, old, timeLimit),
            FishingOptionsListener::timeLimitChanged
        );
    } // setTimeLimit( int value )

    /**
     * Creates and adds a new FishFrameset to the {@code fishFramesets} mapping.
     * 
     * @param name          The fish species name for the new frame set
     *                      definition.
     * @param score         The score to associate with new frame set.
     * @param resourcePath  The path to the spritesheet image for the frame set.
     * @param frameWidth    The width of an individual frame within the
     *                      spritesheet image.
     * @param frameHeight   The height of an individual frame within the
     *                      spritesheet image.
     * @param framesPerRow  The number of frames per row within the
     *                      spritesheet image.
     * @param numFrames     The number of frames in the spritesheet image.
     * @param scale         Scaling factor to apply to both the horizontal and
     *                      vertical axes when rendering a fish of this species.
     * @param flip          Whether to flip the image on the horizontal axis for
     *                      this species.  When flip is {@code false}, it is
     *                      assumed that the sprite images are drawn with the
     *                      fish facing to the right.  A value of {@code true}
     *                      indicates the fish faces to the left in the
     *                      sprite images, and should be flipped horizontally to
     *                      normalize the images so they point to the right.
     * 
     * @throws  IOException if a require resource could not be loaded.
     * @throws  IllegalArgumentException if an entry already exists for the
     *          specified {@code name}.
     */
    public synchronized final void addFishFrameset(
                String name, int score,
                String resourcePath, int frameWidth, int frameHeight, int framesPerRow, int numFrames,
                double scale, boolean flip
            )
        throws IOException
    {
        if (fishFramesets.containsKey(name)){
            throw new IllegalArgumentException("An entry already exists for species '"+ name +"'");
        }

        SpriteSheet sheet;
        try {
            sheet = new SpriteSheet( Resources.getStream(resourcePath) );
        } catch (IOException ex) {
           throw new IOException("Unable to load resource: "+ resourcePath, ex);
        }

        fishFramesets.put(name,
                    new FishFrameset(
                            name,
                            new SpriteFrameSet(sheet, frameWidth, frameHeight, framesPerRow, numFrames, true, 0, 0),
                            score,
                            scale,
                            flip
                        )
                );
    } // addFishFrameset( ... )

    /**
     * Updates the set of fish species that the user is targeting and which
     * will provide positive points when caught.
     * 
     * After the {@code targetFishSpecies} value is updated, this method
     * will invoke the {@code targetFishSpeciesChanged} method on all
     * registered {@link FishingOptionsListener}s.
     * 
     * @param   value   The new list of targeted fish species names.  Only
     *                  species that have an entry in {@code fishFramesets} are
     *                  permitted in this list.  Any duplicate names
     *                  encountered will be reduced to a single instance of the
     *                  name.
     *
     * @throws  IllegalArgumentException if a name is found in {@code value}
     *          that is not listed in {@code fishFramesets}.
     */
    public synchronized void setTargetFishSpecies( List<String> value ) {
        if (value == null || value.isEmpty()) {
            targetFishSpecies.clear();
        } else {
            List<String> targets = new ArrayList<>();
            for (String name : value) {
                if ( !fishFramesets.containsKey(name) )
                    throw new IllegalArgumentException(
                        "Unknown species name '"+ name +"'"
                    );

                if (!targets.contains(name))
                    targets.add(name);
            } // for

            targetFishSpecies = targets;
        }

        EventBase.notifyListeners(
            listeners,
            new ValueChangedEvent(this, null, null),
            FishingOptionsListener::targetFishSpeciesChanged
        );
    } // setTargetFishSpecies( ArrayList<String> value )

    /**
     * Retrieves the set of fish species that the user is targeting and which
     * will provide positive points when caught.
     * 
     * @return  The set of fish species that the user is targeting and which
     *          will provide positive points when caught.
     */
    public List<String> getTargetFishSpecies() {
        return new ArrayList<>(targetFishSpecies);
    } // getTargetFishSpecies()

    /**
     * Adds a new species to the set of fish species that the user is targeting
     * and which will provide positive points when caught.
     *
     * After the {@code targetFishSpecies} value is updated, this method
     * will invoke the {@code targetFishSpeciesChanged} method on all
     * registered {@link FishingOptionsListener}s.
     * 
     * @param   species The name of the species to add to the
     *                  {@code targetFishSpecies} list. Only species names that
     *                  have an entry in {@code fishFramesets} are permitted.
     *                  If the specified name is already in
     *                  {@code targetFishSpecies}, no new entry is added.
     *
     * @throws  IllegalArgumentException if the specified {@code species} name
     *          is not listed in {@code fishFramesets}.
     */
    public void addTargetFishSpecies( String species ) {
        List<String> n = getTargetFishSpecies();
        if (!n.contains(species)) {
            n.add(species);
            setTargetFishSpecies(n);
        }
    } // addTargetFishSpecies( String species )

    /**
     * Removes a species from the set of fish species that the user is
     * targeting.
     *
     * After the {@code targetFishSpecies} value is updated, this method
     * will invoke the {@code targetFishSpeciesChanged} method on all
     * registered {@link FishingOptionsListener}s.
     * 
     * @param   species The name of the species to remove from the
     *                  {@code targetFishSpecies} list.  If the specified name
     *                  is not found in the list, no updates are made.
     */
    public synchronized void removeTargetFishSpecies( String species ) {
        if (targetFishSpecies.remove(species)) {
            EventBase.notifyListeners(
                listeners,
                new ValueChangedEvent(this, null, null),
                FishingOptionsListener::targetFishSpeciesChanged
            );
        }
    } // removeTargetFishSpecies( String species )

    /**
     * Removes all entries from the set of fish species that the user is
     * targeting.
     * 
     * After the {@code targetFishSpecies} value is updated, this method
     * will invoke the {@code targetFishSpeciesChanged} method on all
     * registered {@link FishingOptionsListener}s.
     */
    public synchronized void removeAllTargetFishSpecies() {
        if (!targetFishSpecies.isEmpty()) {
            targetFishSpecies.clear();
            EventBase.notifyListeners(
                listeners,
                new ValueChangedEvent(this, null, null),
                FishingOptionsListener::targetFishSpeciesChanged
            );
        }
    } // removeAllTargetFishSpecies()

    /**
     * Determines whether or not the specified fish species name is in the set
     * of {@code targetFishSpecies}.
     * 
     * @param species   The fish species name to check for.
     * 
     * @return  {@code true} if the specified fish species is in the
     *          {@code targetFishSpecies} set, {@code false} otherwise.
     */
    public boolean isTargetFishSpecies(String species) {
        return targetFishSpecies.contains(species);
    } // isTargetFishSpecies(String species)

    /**
     * Updates the set of fish species that are enabled.
     * 
     * After the {@code enabledFishSpecies} value is updated, this method
     * will invoke the {@code enabledFishSpeciesChanged} method on all
     * registered {@link FishingOptionsListener}s.
     * 
     * @param   value   The new list of enabled fish species names.  Only
     *                  species that have an entry in {@code fishFramesets} are
     *                  permitted in this list.  Any duplicate names
     *                  encountered will be reduced to a single instance of the
     *                  name.
     *
     * @throws  IllegalArgumentException if a name is found in {@code value}
     *          that is not listed in {@code fishFramesets}.
     */
    public synchronized void setEnabledFishSpecies( List<String> value ) {
        if (value == null || value.isEmpty()) {
            enabledFishSpecies.clear();
        } else {
            for (String name : value) {
                if ( !fishFramesets.containsKey(name) )
                    throw new IllegalArgumentException("Unknown species name '"+ name +"'");
            } // for

            enabledFishSpecies = new ArrayList<>(value);
        }

        updateFishSpeciesWeightFactor();
    } // setEnabledFishSpecies( ArrayList<String> value )

    /**
     * Computes the {@code fishSpeciesWeightFactor} value as the sum of the
     * weight factors for all enabled fish species.
     * 
     * After the {@code fishSpeciesWeightFactor} value is updated, this method
     * will invoke the {@code enabledFishSpeciesChanged} method on all registered
     * {@link FishingOptionsListener}s.
     */
    private void updateFishSpeciesWeightFactor() {
        double totalProbabilityWeight = 0d;

        for (String n : enabledFishSpecies) {
            FishFrameset f = fishFramesets.get(n);
            totalProbabilityWeight += f.probabilityWeight;
        }

        fishSpeciesWeightFactor = totalProbabilityWeight;

        EventBase.notifyListeners(
            listeners,
            new ValueChangedEvent(this, null, null),
            FishingOptionsListener::enabledFishSpeciesChanged
        );
    } // updateFishSpeciesWeightFactor()

    /**
     * Adds a new species to the set of enabled fish species and updates the
     * {@code score} and {@code probabilityWeight} of the species.
     *
     * After the {@code enabledFishSpecies} value is updated, this method
     * will invoke the {@code enabledFishSpeciesChanged} method on all
     * registered {@link FishingOptionsListener}s.
     * 
     * @param   species The name of the species to add to the
     *                  {@code enabledFishSpecies} list. Only species names that
     *                  have an entry in {@code fishFramesets} are permitted.
     *                  If the specified name is already in
     *                  {@code targetFishSpecies}, no new entry is added.
     * @param   score   The new score to apply to the specified species.  The
     *                  score associated with the species will be updated even
     *                  if the species was enabled before this method was
     *                  invoked.
     * @param   probabilityWeight The new probability weight to apply to the
     *                  specified species.  The weight associated with the
     *                  species will be updated even if the species was enabled
     *                  before this method was invoked.
     *
     * @throws  IllegalArgumentException if {@code species} is null, the
     *          specified {@code species} name is not listed in
     *          {@code fishFramesets}, or the {@code probabilityWeight} is less
     *          than 0.
     */
    public synchronized void enableFishSpecies( String species, int score, double probabilityWeight ) {
        if (species == null)
            throw new IllegalArgumentException("The species parameter cannot be null.");

        if (probabilityWeight < 0)
            throw new IllegalArgumentException("The probabilityWeight parameter cannot be less than 0.");

        FishFrameset f = fishFramesets.get(species);
        if (f == null)
            throw new IllegalArgumentException("Unknown species name '"+ species +"'");

        f.score = score;
        f.probabilityWeight = probabilityWeight;

        if (!enabledFishSpecies.contains(species)) {
            enabledFishSpecies.add(species);
        }

        updateFishSpeciesWeightFactor();
    } // enableFishSpecies( String species, int score, double probabilityWeight )

    /**
     * Retrieves the set of fish species that are enabled.
     * 
     * @return  The set of fish species that are enabled.
     */
    public List<String> getEnabledFishSpecies() {
        return new ArrayList<>(enabledFishSpecies);
    } // getEnabledFishSpecies()

    /**
     * Disables all fish species, removing them from the
     * {@code enabledFishSpecies} and {@code targetFishSpecies} sets.
     */
    public void disableAllFishSpecies() {
        setEnabledFishSpecies( null );
    } // disableAllFishSpecies()

    /**
     * Updates the score associated with the specified fish species.
     * 
     * @param species   The name of the species to update.
     * @param score     The new score to associate with the specified species.
     * 
     * @throws  IllegalArgumentException if the specified {@code species} name
     *          is not listed in {@code fishFramesets}.
     */
    public void setFishSpeciesScore( String species, int score ) {
        FishFrameset f = fishFramesets.get(species);
        if (f == null)
            throw new IllegalArgumentException("Unknown species name '"+ species +"'");

        f.score = score;
    } // setFishSpeciesScore( int score )

    /**
     * Increments the capture count associated with the specified fish species
     * by one.
     * 
     * @param species   The name of the species to update.
     * 
     * @throws  IllegalArgumentException if the specified {@code species} name
     *          is not listed in {@code fishFramesets}.
     */
    public void incrementFishSpeciesCaptureCount( String species ) {
        FishFrameset f = fishFramesets.get(species);
        if (f == null)
            throw new IllegalArgumentException("Unknown species name '"+ species +"'");
        ++f.numCaptured;
    } // incrementFishSpeciesCaptureCount( String species )

    /**
     * Retrieves the current capture count associated with the specified fish
     * species.
     * 
     * @param species   The name of the species to retrieve the count for.
     * 
     * @return  The current capture count associated with the specified fish
     *          species.
     * 
     * @throws  IllegalArgumentException if the specified {@code species} name
     *          is not listed in {@code fishFramesets}.
     */
    public int getFishSpeciesCaptureCount( String species ) {
        FishFrameset f = fishFramesets.get(species);
        if (f == null)
            throw new IllegalArgumentException("Unknown species name '"+ species +"'");
        return f.numCaptured;
    } // getFishSpeciesCaptureCount( String species )

    /**
     * Retrieves the score associated with the specified fish species.
     * 
     * @param species   The name of the species to retrieve the score for.
     * 
     * @return  The current score associated with the specified fish species.
     * 
     * @throws  IllegalArgumentException if the specified {@code species} name
     *          is not listed in {@code fishFramesets}.
     */
    public int getFishSpeciesScore( String species ) {
        FishFrameset f = fishFramesets.get(species);
        if (f == null)
            throw new IllegalArgumentException("Unknown species name '"+ species +"'");

        return f.score;
    } // getFishSpeciesScore()

    /**
     * Updates the probability weight associated with the specified fish
     * species.
     * 
     * @param species   The name of the species to update.
     * @param weight    The new probability weight to associate with the
     *                  specified species.
     * 
     * @throws  IllegalArgumentException if the specified {@code species} name
     *          is not listed in {@code fishFramesets}, or the
     *          {@code probabilityWeight} is less than 0.        
     */
    public void setFishSpeciesProbabilityWeight( String species, double weight ) {
        FishFrameset f = fishFramesets.get(species);
        if (f == null)
            throw new IllegalArgumentException("Unknown species name '"+ species +"'");

        if (weight < 0)
            throw new IllegalArgumentException("The probabilityWeight parameter cannot be less than 0.");

        f.probabilityWeight = weight;
    } // setFishSpeciesProbabilityWeight( int weight )

    /**
     * Retrieves the probability weight associated with the specified fish
     * species.
     * 
     * @param species   The name of the species to retrieve the probability
     *                  weight for.
     * 
     * @return  The current probability weight associated with the specified
     *          fish species.
     * 
     * @throws  IllegalArgumentException if the specified {@code species} name
     *          is not listed in {@code fishFramesets}.
     */
    public double getFishSpeciesProbabilityWeight( String species ) {
        FishFrameset f = fishFramesets.get(species);
        if (f == null)
            throw new IllegalArgumentException("Unknown species name '"+ species +"'");

        return f.probabilityWeight;
    } // getFishSpeciesProbabilityWeight()

    /**
     * Creates a new fish of a random enabled species.
     * 
     * @return  The newly created Fish instance.
     *
     * @throws  IllegalStateException if no enabled fish species has a non-zero
     *          probability weight.
     */
    public synchronized Fish createFish() {
        // Determine which fish species to create based on their relative weights
        double probability = Math.random();

        double currentFactor = 0d;
        for (String name : enabledFishSpecies) {
            FishFrameset ff = fishFramesets.get(name);
            currentFactor += ff.probabilityWeight / fishSpeciesWeightFactor;
            if (currentFactor >= probability) {
                Fish f = new Fish(ff.frameset, ff.score, ff.species);
                double scaleAdjust = (1 + 1/16 - (Math.random() / 8));
                f.setMouseEnabled(true);
                f.setScale( ff.scaleX * scaleAdjust, ff.scaleY * scaleAdjust );
                f.setRepeat(true);
                f.start();

                return f;
            }
        }

        throw new IllegalStateException("Unable to determine type of fish to construct");
    } // createFish()

    /**
     * Retrieves the FishFrameset for the species with the specified name.
     * 
     * @param species   The fish species name to retrieve the FishFrameset for.
     * 
     * @return  The FishFrameset for the species with the specified name.
     * 
     * @throws  IllegalArgumentException if the specified {@code species} name
     *          is not listed in {@code fishFramesets}.
     */
    public SpriteFrameSet getFishFrameset( String species ) {
        FishFrameset ff = fishFramesets.get(species);
        if (ff == null)
            throw new IllegalArgumentException("Unknown species name '"+ species +"'");

        return ff.frameset;
    } // getFishFrameset( String species )

    /**
     * Adds a new event listener to the set of registered listeners.
     * 
     * <p>If the specified listener already exists in the set of registered
     * listeners, it will not be added again.
     * 
     * @param listener The new listener instance to add.
     */
    public synchronized void addListener( FishingOptionsListener listener ) {
        listeners = EventBase.addListener(listeners, listener);
    } // addListener( FishingOptionsListener listener )

    /**
     * Removes a previously registered event listener from the set of
     * registered listeners.
     *
     * <p>If the specified listener is not found in the set of registered
     * listeners, the set of registered listeners will remain unchanged.
     *
     * @param listener The listener instance to remove.
     */
    public synchronized void removeListener( FishingOptionsListener listener ) {
        listeners = EventBase.removeListener(listeners, listener);
    } // removeListener( FishingOptionsListener listener )

} // class FishingOptions
