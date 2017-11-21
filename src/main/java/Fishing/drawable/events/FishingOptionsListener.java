
package Fishing.drawable.events;

import Fishing.FishingOptions;

/**
 * Interface that must be implemented by objects that listen for property
 * change events from a {@link FishingOptions} instance.
 * 
 * @author Brad
 */
public interface FishingOptionsListener {

    /**
     * Method invoked after the {@code maxFish} property value is changed.
     * 
     * @param e The event that occurred.
     * 
     * @see FishingOptions#setMaxFish(int)
     */
    void maxFishChanged( ValueChangedEvent e );

    /**
     * Method invoked after the {@code maxFishSpeed} property value is changed.
     * 
     * @param e The event that occurred.
     * 
     * @see FishingOptions#setMaxFishSpeed(double) 
     */
    void maxFishSpeedChanged( ValueChangedEvent e );

    /**
     * Method invoked after the {@code minFishSpeed} property value is changed.
     * 
     * @param e The event that occurred.
     * 
     * @see FishingOptions#setMinFishSpeed(double) 
     */
    void minFishSpeedChanged( ValueChangedEvent e );

    /**
     * Method invoked when the {@code enabledFishSpecies} property value is
     * changed.
     * 
     * @param e The event that occurred.
     * 
     * @see FishingOptions#setEnabledFishSpecies(java.util.ArrayList) 
     */
    void enabledFishSpeciesChanged( ValueChangedEvent e );

    /**
     * Method invoked after the {@code timeLimit} property value is changed.
     * 
     * @param e The event that occurred.
     * 
     * @see FishingOptions#setTimeLimit(int) 
     */
    void timeLimitChanged( ValueChangedEvent e );

    /**
     * Method invoked after the {@code title} property value is changed.
     * 
     * @param e The event that occurred.
     * 
     * @see FishingOptions#setTitle(java.lang.String) 
     */
    void titleChanged( ValueChangedEvent e );

    /**
     * Method invoked after the {@code targetFishSpecies} property value is
     * changed.
     * 
     * @param e The event that occurred.
     * 
     * @see FishingOptions#setTargetFishSpecies(java.util.ArrayList)
     */
    void targetFishSpeciesChanged( ValueChangedEvent e );

} // interface FishingOptionsListener
