package itumulator.display.utility;

import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * A class to allow caching of all images in resource folder. Strips any subdirectory and only maintains name (thus they must be unique even in subdirectories)
 */
public class ImageResourceCache {
    /* This is Singleton pattern */
    private static ImageResourceCache instance;

    public static ImageResourceCache Instance(){
        if (instance == null){
            instance = new ImageResourceCache();
        }
        return instance;
    }
    /* This is Singleton pattern */


    private Map<String, BufferedImage> cache;

    public BufferedImage getImage(String cacheString){
        BufferedImage result = cache.get(cacheString);
        if (result == null){
            System.out.println(cacheString + " is null");
        }
        return result;
    }

    private ImageResourceCache(){
        cache = new HashMap<String, BufferedImage>();

        loadFolder("./resources/");
        loadFolder("../resources");
    }

    private void loadFolder(String folderPath){
        try {
            File folder = new File(folderPath);
            
            if (folder.listFiles() == null) {
                return;
            }
            for (File element : folder.listFiles()) {
                if (element.getName().endsWith(".png") || 
                element.getName().endsWith(".jpg") || 
                element.getName().endsWith(".jpeg")){
                    String elementName = element.getName().substring(0, element.getName().lastIndexOf('.'));
                    if (cache.containsKey(elementName)){
                        throw new IllegalArgumentException("Image names in resource folder must be unique - " + elementName);
                    } else {
                        BufferedImage loadedImg = ImageIO.read(element);
                        
                        cache.put(elementName, squareImage(loadedImg));
                    }
                } else {
                    loadFolder(folderPath+element.getName()+"/");
                }
            }
        } catch (Exception e){
            System.out.println("Cache loader error: " + e.getMessage());
        }
    }

    private BufferedImage squareImage(BufferedImage loadedImg) {
        int maxPixel = Math.max(loadedImg.getWidth(), loadedImg.getHeight());
        boolean maxWidth = maxPixel == loadedImg.getWidth();
        int padding = 0;
        if (maxWidth){
            padding = (maxPixel - loadedImg.getHeight()) / 2;
        } else {
            padding = (maxPixel - loadedImg.getWidth()) / 2;
        }

        BufferedImage newImg = new BufferedImage(maxPixel, maxPixel, loadedImg.getType());

        Graphics2D g2 = newImg.createGraphics();

        if (maxWidth){
            g2.drawImage(loadedImg, 0, padding, null);
        } else {
            g2.drawImage(loadedImg, padding, 0, null);
        }

        g2.dispose();

        return newImg;
    }
}
