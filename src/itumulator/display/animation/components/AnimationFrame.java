package itumulator.display.animation.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import itumulator.display.animation.ObjectInformation;
import itumulator.display.utility.IsomorphicCoordinateFactory;
import itumulator.display.utility.IsomorphicUtility;
import itumulator.display.utility.Point2DInt;

/**
 * A class representing the drawing of a single object for a single frame (using {@link ObjectInformation}).
 */
public class AnimationFrame{
    private final float SHADOW_OPACITY = 0.5f;
    private ObjectInformation oi;
    private Point2DInt pixelPoint;
    private AlphaComposite alphaComp;
    private float opfloat;
    private BufferedImage img;

    public AnimationFrame(BufferedImage img, Point2DInt pixelPoint){
        this.img = img;
        this.pixelPoint = pixelPoint;
    }

    public AnimationFrame(ObjectInformation oi, Point2DInt pixelPoint){
        this(oi, pixelPoint, 255);
    }

    public AnimationFrame(ObjectInformation oi, Point2DInt pixelPoint, int opacity){
        this.oi = oi;
        this.pixelPoint = pixelPoint;
        opfloat = opacity > 0 ? (float)((opacity * 1.0)/255) : 0f;
        alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opfloat);
    }

    /**
     * Draws the part of the frame on a graphics element associated to the object it represents.
     * @param g
     */
    public void draw(Graphics g){
        double tileHeight = IsomorphicCoordinateFactory.Instance().getTileHeight();

        if (alphaComp != null){
            ((Graphics2D)g).setComposite(alphaComp);
        }

        if (oi == null){
            // We assume draw to exact location 
            g.drawImage(img, pixelPoint.getX(), pixelPoint.getY(), null);
        } else {
            if (oi.getImage() != null){
                if (oi.isGroundObject()){
                    BufferedImage scaledImg = IsomorphicCoordinateFactory.Instance().getScaledImage(oi.getImage());

                    double rotRadians = Math.toRadians(45);

                    double unitX = Math.abs(Math.cos(rotRadians));
                    double unitY = Math.abs(Math.sin(rotRadians));

                    int newWidth = (int)Math.floor(scaledImg.getWidth() * unitX + scaledImg.getHeight() * unitY);
                    int newHeight = (int)Math.floor(scaledImg.getHeight() * unitX + scaledImg.getWidth() * unitY);

                    BufferedImage rotatedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

                    AffineTransform transform = new AffineTransform();
                    transform.translate((newWidth - scaledImg.getWidth()) / 2, (newHeight - scaledImg.getHeight()) / 2);
                    transform.rotate(rotRadians, scaledImg.getWidth()/2, scaledImg.getHeight()/2);

                    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                    rotatedImg = op.filter(scaledImg, rotatedImg);

                    transform = new AffineTransform();
                    transform.scale(1.0, 0.5);

                    op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

                    BufferedImage squeezedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

                    squeezedImg = op.filter(rotatedImg, squeezedImg);
                    
                    g.drawImage(
                        squeezedImg, 
                        pixelPoint.getX()-(squeezedImg.getWidth()/2), 
                        pixelPoint.getY(),
                         null);
                } else {
                    BufferedImage scaledImg = IsomorphicCoordinateFactory.Instance().getScaledImage(oi.getImage());
                    // Draw shadow oval
                    float shadow = SHADOW_OPACITY * opfloat - 0.1f;
                    ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, shadow > 0 ? shadow : 0));
                    g.setColor(Color.BLACK);
                    g.fillOval(
                    pixelPoint.getX()-(int)(tileHeight/2),
                    pixelPoint.getY()+(int)((tileHeight/6)*2),
                    scaledImg.getWidth(),
                    scaledImg.getHeight()/5);
                    
                    if (alphaComp != null){
                        ((Graphics2D)g).setComposite(alphaComp);
                    } else {
                        ((Graphics2D)g).setComposite(AlphaComposite.SrcOver);
                    }
                
                    // Draw image
                    g.drawImage(
                    scaledImg,
                    pixelPoint.getX()-(scaledImg.getWidth()/2),
                    pixelPoint.getY()-(int)(tileHeight/2),
                    null);
                    }
                } else {
                    // Draw polygon with color
                    g.setColor(oi.getColor());
                    g.fillPolygon(IsomorphicUtility.getIsoPolygon(pixelPoint.getX(), pixelPoint.getY()+(int)tileHeight/2, (int)tileHeight, (int)tileHeight/2));
            }
        }
        ((Graphics2D)g).setComposite(AlphaComposite.SrcOver);
        img = null;
    }
}