package itumulator.display.animation.components;

import itumulator.display.animation.ObjectInformation;
import itumulator.display.utility.IsomorphicCoordinateFactory;
import itumulator.world.Location;

/**
 * An animation that makes an object dissapear (used on 'remove' within {@link itumulator.world.World}).
 */
public class HideAnimation extends Animation {
    int step;    

    public HideAnimation(ObjectInformation oi, Location location, int animationLength) {
        super(oi, location, animationLength);
        step = 255/animationLength;
    }

    @Override
    protected AnimationFrame getFrame(int index) {
        return new AnimationFrame(oi, IsomorphicCoordinateFactory.Instance().getIsoLocation(location), 255 - step*index);
    }
}
