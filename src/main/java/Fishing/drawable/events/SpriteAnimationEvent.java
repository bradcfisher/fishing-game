
package Fishing.drawable.events;

import Fishing.drawable.Animation;


/**
 *
 * @author Brad
 */
public class SpriteAnimationEvent
    extends DrawableEvent
{

    public SpriteAnimationEvent( Animation source ) {
        super(source);
    } // SpriteAnimationEvent( Animation source )

    @Override
    public Animation getSource() {
        return (Animation) super.getSource();
    } // getSource()

} // class SpriteAnimationEvent
