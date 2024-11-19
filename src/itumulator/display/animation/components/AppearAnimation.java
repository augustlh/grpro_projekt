package itumulator.display.animation.components;

import itumulator.display.animation.ObjectInformation;
import itumulator.display.utility.IsomorphicCoordinateFactory;
import itumulator.world.Location;

/**
 * An animation that makes an object appear.
 */
public class AppearAnimation extends Animation {
    int step;

    public AppearAnimation(ObjectInformation oi, Location location, int animationLength) {
        super(oi, location, animationLength);
        step = 255/animationLength;
    }

    @Override
    protected AnimationFrame getFrame(int index) {
        return new AnimationFrame(oi, IsomorphicCoordinateFactory.Instance().getIsoLocation(location), index == animationLength-1 ? 255 : (index * step));
    }
}
