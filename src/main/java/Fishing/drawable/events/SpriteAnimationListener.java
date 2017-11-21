
package Fishing.drawable.events;

import java.util.EventListener;

/**
 *
 * @author Brad
 */
public interface SpriteAnimationListener
    extends EventListener
{

    public void spriteAnimationStarted( SpriteAnimationEvent e );
    public void spriteAnimationStopped( SpriteAnimationEvent e );
    public void spriteAnimationEnteredFrame( SpriteAnimationEvent e );

} // interface SpriteAnimationListener
