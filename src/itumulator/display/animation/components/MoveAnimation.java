package itumulator.display.animation.components;

import itumulator.display.animation.ObjectInformation;
import itumulator.display.utility.IsomorphicCoordinateFactory;
import itumulator.display.utility.Point2DInt;
import itumulator.world.Location;

/**
 * An animation used to indicate that an object is moving
 */
public class MoveAnimation extends Animation{
    private Point2DInt oldPixelPoint;
    private Point2DInt newPixelPoint;

    public MoveAnimation(ObjectInformation oi, Location l, int animationLength, Location newLocation) {
        super(oi, l, animationLength);
        this.oldPixelPoint = IsomorphicCoordinateFactory.Instance().getIsoLocation(location);
        this.newPixelPoint = IsomorphicCoordinateFactory.Instance().getIsoLocation(newLocation);
    }

    @Override
    protected AnimationFrame getFrame(int index) {
        return new AnimationFrame(oi, oldPixelPoint.interpolate(newPixelPoint, (1.00 * index) / animationLength));
    }
}
