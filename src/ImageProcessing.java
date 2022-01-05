import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

//TODO: Make method that flips image on an axis.
/**
 * ImageProcessing is a utility class that handles most of the work involving BufferedImages.
 */
public class ImageProcessing{
    /**Pixels of this colour have the option to be rendered with full transparency.*/public static final Color NULL_COLOR = new Color(255, 82, 255, 255); //Use for green 32 in GIMP, for some reason it comes out as 82. Continue to use 32 in GIMP and 82 here.
    /**The colour to render the sky as.*/public static final Color SKY_COLOR = new Color(0, 191, 255, 255);

    /***/public static final int TILE_LAYER = 2;
    /**The layer of the JLayeredPane that moving objects will be assigned to.*/static final int MOVING_OBJECT_LAYER = 3;
    /***/public static final int HUD_LAYER = 5;
    public static final int PARTICLE_LAYER = 4;

    /**
     * Retrieves a texture from the resources folder. Note that "resources/" is automatically added to the front of the filepath.
     * @param filepath The location in the resources folder of the desired texture.
     * @return Returns a BufferedImage of the desired texture.
     */
    public static BufferedImage getImageFromResources(String filepath) {
        try{
            return imageToBufferedImage(new ImageIcon("resources/" + filepath).getImage());
        }catch(Exception exception){
            System.err.println("Warning! Texture " + "[resources/" + filepath + "] not found! Defaulting to [resources/textures/missingTexture.png].");
            return imageToBufferedImage(new ImageIcon("resources/textures/missingTexture.png").getImage());
        }
    }


    //TODO: Find a more efficient imageToBufferedImage method
    //USE SPARINGLY, excessive use leads to long loading times.
    /**
     * Converts Image objects into BufferedImage objects. Note that this does not copy over the Alpha channel.
     * @param inputImage The Image object to be converted.
     * @return Returns the BufferedImage version of the input image.
     */
    public static BufferedImage imageToBufferedImage(Image inputImage){
        //TODO: Get the NULL_COLOR pixels from the original image to reset the alpha pixels, as one of these methods breaks the alpha value.
        BufferedImage newBufferedImage = new BufferedImage(inputImage.getWidth(null), inputImage.getHeight(null), 6);
        Graphics2D g2d = newBufferedImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, null);
        g2d.dispose();
        return newBufferedImage;
    }

    /**
     * Converts BufferedImage objects Image objects.
     * @param inputBufferedImage The BufferedImage object to be converted.
     * @return Returns the Image version of the input buffered image.
     */
    public static BufferedImage bufferedImageToImage(BufferedImage inputBufferedImage){
        return inputBufferedImage;
    }

    /**
     * Rotates the inputBufferedImage in intervals of 90 degrees.
     * @param inputBufferedImage The BufferedImage to be rotated.
     * @param rotation The number of 90 degree clockwise rotations to do.
     * @return Returns the inputBufferedImage rotated by some multiple of 90 degrees.
     */
    public static BufferedImage rotateImage(BufferedImage inputBufferedImage, int rotation){
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

    /**
     * Overlays two BufferedImages, the top image takes priority unless the pixel is equal to NULL_COLOR (excluding the alpha value), in which case the pixel from the bottom image is used.
     * @param baseBufferedImage The BufferedImage to show behind the top image.
     * @param topBufferedImage The BufferedImage to be primarily shown, pixels equaling NULL_COLOR will be replaced.
     * @return Returns a BufferedImage of the top BufferedImage above the base BufferedImage.
     */
    public static BufferedImage overlayImage(BufferedImage baseBufferedImage, BufferedImage topBufferedImage){
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
                myColour = new Color(topBufferedImage.getRGB(x, y));
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

    /**
     * Changes the brightness of the image by multiplying the RGB values by the modifier. Alpha values remain untouched.
     * @param inputBufferedImage The BufferedImage to have its brightness changed.
     * @param modifier The multiplier to change each pixel by.
     * @return Returns the inputBufferedImage with the pixel brightness modified.
     */
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

    /**
     * Sets the Alpha value to 0 for any pixel whose color equals NULL_COLOR.
     * @param inputBufferedImage The BufferedImage to have NULL_COLOR pixels changed.
     * @return Returns the input BufferedImage with any pixel equal to NULL_COLOR have 0 Alpha, but the same RGB.
     */
    public static BufferedImage removeNullPixels(BufferedImage inputBufferedImage){
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
                r = myColour.getRed();
                g = myColour.getGreen();
                b = myColour.getBlue();
                if(r == NULL_COLOR.getRed() && g == NULL_COLOR.getGreen() && b == NULL_COLOR.getBlue()){
                    a = 0;
                }
                col = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                newImage.setRGB(x, y, col);
            }
        }
        return newImage;
    }

    /**
     * Scaled a BufferedImage up based on the scaleFactor.
     * @param inputBufferedImage The BufferedImage to be scaled.
     * @param widthScaleFactor The factor of the original size to be the new horizontal scale.
     * @param heightScaleFactor The factor of the original size to be the new vertical scale.
     * @return Returns a scaled up version of the input BufferedImage.
     */
    public static BufferedImage resizeImage(BufferedImage inputBufferedImage, int widthScaleFactor, int heightScaleFactor){
        return ImageProcessing.imageToBufferedImage(inputBufferedImage.getScaledInstance(inputBufferedImage.getWidth() * widthScaleFactor, inputBufferedImage.getHeight() * heightScaleFactor, Image.SCALE_REPLICATE));
    }

    public static BufferedImage changeImageAlpha(BufferedImage inputBufferedImage, int newAlpha, boolean keepFullTransparencies){
        assert newAlpha >= 0;
        assert newAlpha < 256;
        int a;// red component 0...255
        int r;// = 255;// red component 0...255
        int g;// = 0;// green component 0...255
        int b;// = 0;// blue component 0...255
        int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
        for (int x = 0; x < inputBufferedImage.getWidth(); x++) {
            for (int y = 0; y < inputBufferedImage.getHeight(); y++) {
                Color myColour = new Color(inputBufferedImage.getRGB(x, y), true);
                a = newAlpha;
                r = myColour.getRed();
                g = myColour.getGreen();
                b = myColour.getBlue();
                if(keepFullTransparencies && myColour.getAlpha() <= 0){
                    a=0;
                }
                col = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                inputBufferedImage.setRGB(x, y, col);
            }
        }
        return inputBufferedImage;
    }
}
