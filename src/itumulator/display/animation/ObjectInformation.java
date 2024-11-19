package itumulator.display.animation;

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Random;
import java.awt.Color;
import java.awt.geom.AffineTransform;

import itumulator.display.utility.ImageResourceCache;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.NonBlocking;

/**
 * Wrapping class to associate more information with objects than simply their display information provider.
 */
public class ObjectInformation {
    private Object obj;
    private DisplayInformation di;
    private boolean implementsDynamicDisplayInformationProvider;
    private boolean isGroundObject;
    private BufferedImage image;
    private Color color;
    private int direction;

    public ObjectInformation(Object obj) {
        if(!(obj instanceof DynamicDisplayInformationProvider)) throw new IllegalArgumentException("Must implement dynamicDisplayInformationProvider");
        implementsDynamicDisplayInformationProvider = true;
        setup(obj);
    }

    public ObjectInformation(Object obj, DisplayInformation di){
        this.di = di;
        setup(obj);
    }

    public boolean isGroundObject(){
        return isGroundObject;
    }

    public BufferedImage getImage(){
        return buildImage();
    }

    public Color getColor(){
        return implementsDynamicDisplayInformationProvider ? getInformation().getColor() : color;
    }

    public DisplayInformation getInformation(){
        return implementsDynamicDisplayInformationProvider ? ((DynamicDisplayInformationProvider)obj).getInformation() : di;
    }

    private void setup(Object obj){
        this.obj = obj;
        if(obj instanceof NonBlocking) isGroundObject = true;
        buildImage();
        color = getInformation().getColor();
        if(getInformation().getRandomDirection()){
            direction = new Random().nextInt(5) * 90;
            setupRotation();
        }
    }

    private BufferedImage buildImage(){
        if (getInformation().getImageKey() != null){
            BufferedImage newImage = ImageResourceCache.Instance().getImage(getInformation().getImageKey());
            if (newImage != image){
                image = newImage;
                if(getInformation().getRandomDirection()){
                    setupRotation();
                }
            }
        }

        return image;
    }
    
    private void setupRotation(){
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(direction), image.getWidth() / 2, image.getHeight() / 2);
    }
}
