
package Fishing.drawable.events;

/**
 * Convenience class implementing the {@link SpriteAnimationListener} interface
 * with all methods defined as NOPs.
 *
 * @author Brad
 */
public class SpriteAnimationAdapter
    implements SpriteAnimationListener
{

    @Override
    public void spriteAnimationStarted(SpriteAnimationEvent e) { }

    @Override
    public void spriteAnimationStopped(SpriteAnimationEvent e) { }

    @Override
    public void spriteAnimationEnteredFrame(SpriteAnimationEvent e) { }
    
} // class SpriteAnimationAdapter
