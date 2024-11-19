package itumulator.display.utility;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolTip;

/**
 * Utility to produce the display of day/night cycle
 */
public class DayNightHelper {
    private JLabel dayNightLabel;
    private boolean icon = false;
    private ImageIcon nightIcon, dayIcon;

    public JLabel initialize(int uiHeight){
        dayNightLabel = new JLabel("Daytime"){
                @Override
                public JToolTip createToolTip(){
                JToolTip tip = super.createToolTip();
                tip.setBackground(Color.WHITE);
                return tip;
    }
        };

        dayNightLabel.setToolTipText("The current Day/Night status");
        BufferedImage img = ImageResourceCache.Instance().getImage("sun");
        double ratio = (uiHeight * 1.0) / img.getHeight();
        BufferedImage scaledImg = ImageUtility.getScaledImage(img, (int)(ratio * img.getWidth()), uiHeight);
        dayIcon = new ImageIcon(scaledImg);

        img = ImageResourceCache.Instance().getImage("moon");
        ratio = (uiHeight * 1.0) / img.getHeight();
        scaledImg = ImageUtility.getScaledImage(img, (int)(ratio * img.getWidth()), uiHeight);
        
        nightIcon = new ImageIcon(scaledImg);

        dayNightLabel.setIcon(dayIcon);
        dayNightLabel.setOpaque(false);
        dayNightLabel.setBorder(null);
        dayNightLabel.setText("");
        icon = true;

        return dayNightLabel;
    }

    public void update(boolean isDayTime){
        if (icon){
            dayNightLabel.setIcon(isDayTime ? dayIcon : nightIcon);
        } else {
            dayNightLabel.setText(isDayTime ? "Daytime" : "Nighttime");
        }
    }
}
