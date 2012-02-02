package uk.ac.ed.inf.sdp2012.group7.vision;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class NormaliseRGB{

    /*
    Takes in an image and normalises the RGB values.
    Probably wont use it as affects frame rate too much, even 
    when only doing it within the bounds.
    */

    public BufferedImage normalise(BufferedImage image){
        for (int i = 0; i < 640; i++){
            for (int j = 0; j < 480; j++){
                Color rgb = new Color(image.getRGB(i,j));
                float a = rgb.getAlpha();
                float r = rgb.getRed();
                float g = rgb.getGreen();
                float b = rgb.getBlue();
                r = (r/(r+g+b+1))*255;
                g = (g/(r+g+b+1))*255;
                b = (b/(r+g+b+1))*255;
				int argb = ((int)a << 24) + (((int)r & 0xFF) << 16) + (((int)g & 0xFF) << 8) + ((int)b & 0xFF);
                image.setRGB(i,j,argb);
            }
        }
        return image;
    }
}