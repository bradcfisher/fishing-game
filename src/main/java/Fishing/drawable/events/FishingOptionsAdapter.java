
package Fishing.drawable.events;

/**
 * Convenience class implementing the {@link FishingOptionsListener} interface
 * with all methods defined as NOPs.
 *
 * @author Brad
 */
public class FishingOptionsAdapter
    implements FishingOptionsListener
{

    @Override
    public void maxFishChanged( ValueChangedEvent e ) { }

    @Override
    public void maxFishSpeedChanged( ValueChangedEvent e ) { }

    @Override
    public void minFishSpeedChanged( ValueChangedEvent e ) { }

    @Override
    public void enabledFishSpeciesChanged( ValueChangedEvent e ) { }

    @Override
    public void timeLimitChanged( ValueChangedEvent e ) { }

    @Override
    public void titleChanged( ValueChangedEvent e ) { }

    @Override
    public void targetFishSpeciesChanged( ValueChangedEvent e ) { }

} // class FishingOptionsAdapter
