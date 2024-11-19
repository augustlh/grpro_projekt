package itumulator.display.animation.components;

import itumulator.display.animation.ObjectInformation;
import itumulator.display.utility.IsomorphicCoordinateFactory;
import itumulator.world.Location;

/**
 * An animation used to show no change to an object.
 */
public class StillAnimation extends Animation{

    public StillAnimation(ObjectInformation oi, Location location, int animationLength) {
        super(oi, location, animationLength);
    }

    @Override
    protected AnimationFrame getFrame(int index) {
        return new AnimationFrame(oi, IsomorphicCoordinateFactory.Instance().getIsoLocation(location));
    }
}
