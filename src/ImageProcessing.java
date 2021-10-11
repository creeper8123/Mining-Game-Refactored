import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageProcessing {
    public static final Color NULL_COLOR = new Color(255, 82, 255, 255); //Use for green 32 in GIMP, for some reason it comes out as 82. Continue to use 32 in GIMP and 82 here.
    public static final Color NULL_COLOR_REPLACEMENT = new Color(0, 255, 255, 255);

    //TODO: Make method that flips image on an axis.
    public static BufferedImage getImageFromResources(String fileLocation) {
        return imageToBufferedImage(new ImageIcon("resources/" + fileLocation).getImage());
    }

    //TODO: Find a more efficient imageToBufferedImage method
    //USE SPARINGLY, excessive use leads to long loading times.
    public static BufferedImage imageToBufferedImage(Image inputImage){
        BufferedImage newBufferedImage = new BufferedImage(inputImage.getWidth(null), inputImage.getHeight(null), 6);
        Graphics2D g2d = newBufferedImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, null);
        g2d.dispose();
        return newBufferedImage;
    }

    public static BufferedImage bufferedImageToImage(BufferedImage inputBufferedImage){
        return inputBufferedImage;
    }

    public static BufferedImage rotateImageRandomly(BufferedImage inputBufferedImage, int rotation){
        //From here down is copied code. Source is https://blog.idrsolutions.com/2019/05/image-rotation-in-java/
        final double rads = Math.toRadians(rotation*90);

        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(inputBufferedImage.getWidth() * cos + inputBufferedImage.getHeight() * sin);
        final int h = (int) Math.floor(inputBufferedImage.getHeight() * cos + inputBufferedImage.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, inputBufferedImage.getType());
        final AffineTransform at = new AffineTransform();
        at.translate((double) w / 2, (double) h / 2);
        at.rotate(rads,0, 0);
        at.translate((double) -inputBufferedImage.getWidth() / 2, (double) -inputBufferedImage.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(inputBufferedImage,rotatedImage);

        return rotatedImage;
    }

    public static BufferedImage overlayImage(BufferedImage baseBufferedImage, BufferedImage oldBufferedImageOverlay){
        BufferedImage newImage = new BufferedImage(baseBufferedImage.getWidth(), baseBufferedImage.getHeight(), 6);

        int aB;// red component 0...255
        int rB;// = 255;// red component 0...255
        int gB;// = 0;// green component 0...255
        int bB;// = 0;// blue component 0...255

        int aO;// red component 0...255
        int rO;// = 255;// red component 0...255
        int gO;// = 0;// green component 0...255
        int bO;// = 0;// blue component 0...255

        int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
        for (int x = 0; x < baseBufferedImage.getWidth(); x++) {
            for (int y = 0; y < baseBufferedImage.getHeight(); y++) {
                Color myColour;
                myColour = new Color(oldBufferedImageOverlay.getRGB(x, y));
                aO = myColour.getAlpha();
                rO = myColour.getRed();
                gO = myColour.getGreen();
                bO = myColour.getBlue();
                if(rO == NULL_COLOR.getRed() && gO == NULL_COLOR.getGreen() && bO == NULL_COLOR.getBlue()){
                    myColour = new Color(baseBufferedImage.getRGB(x, y));
                    aB = myColour.getAlpha();
                    rB = myColour.getRed();
                    gB = myColour.getGreen();
                    bB = myColour.getBlue();
                    col = ((aB&0x0ff)<<24)|((rB&0x0ff)<<16)|((gB&0x0ff)<<8)|(bB&0x0ff);
                }else{
                    col = ((aO&0x0ff)<<24)|((rO&0x0ff)<<16)|((gO&0x0ff)<<8)|(bO&0x0ff);
                }
                newImage.setRGB(x, y, col);
            }
        }
        return newImage;
    }

    public static BufferedImage changeBrightness(BufferedImage inputBufferedImage, double modifier){
        BufferedImage newImage = new BufferedImage(inputBufferedImage.getWidth(), inputBufferedImage.getHeight(), 6);
        int a;// red component 0...255
        int r;// = 255;// red component 0...255
        int g;// = 0;// green component 0...255
        int b;// = 0;// blue component 0...255
        int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
        for (int x = 0; x < inputBufferedImage.getWidth(); x++) {
            for (int y = 0; y < inputBufferedImage.getHeight(); y++) {
                Color myColour = new Color(inputBufferedImage.getRGB(x, y));
                a = myColour.getAlpha();
                r = (int) (myColour.getRed() * modifier);
                g = (int) (myColour.getGreen() * modifier);
                b = (int) (myColour.getBlue() * modifier);
                col = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                newImage.setRGB(x, y, col);
            }
        }
        return newImage;
    }

    public static BufferedImage resizeImage(BufferedImage inputBufferedImage, int scaleFactor){
        return ImageProcessing.imageToBufferedImage(inputBufferedImage.getScaledInstance(inputBufferedImage.getWidth() * scaleFactor, inputBufferedImage.getHeight() * scaleFactor, Image.SCALE_REPLICATE));
    }
}
