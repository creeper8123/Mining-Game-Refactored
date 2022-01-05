import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class TileGraphics {

    /***/public static final int TILE_WIDTH = 64;
    /***/public static final int TILE_HEIGHT = 64;
    /***/public static final int TILE_BASE_HEIGHT = 16;
    /***/public static final int TILE_BASE_WIDTH = 16;
    /***/
    public JLabel textureLabel;
    /***/
    public BufferedImage texture;

    /***/
    public BufferedImage backgroundTexture = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT * Game.tiles[0].length, 6);

    public ArrayList<Integer> yValues = new ArrayList<>();

    public TileGraphics() {
        textureLabel = new JLabel();
        textureLabel.setSize(TILE_WIDTH, Game.tiles[0].length * TILE_HEIGHT);
        Game.layeredPane.add(textureLabel);
        Game.layeredPane.setLayer(textureLabel, ImageProcessing.TILE_LAYER);
        //textureLabel.addMouseListener(this);
    }

    public void redrawChunk(Image newTexture) {
        textureLabel.setIcon(new ImageIcon(newTexture));
    }

    public void stitchTexture(Tile[] tiles) {
        BufferedImage chunkTexture = new BufferedImage(TILE_BASE_WIDTH, Game.tiles[0].length * TILE_BASE_HEIGHT, 6);
        for (int i = 0 ; i < tiles.length ; i++) {
            BufferedImage workingImage;
            int a;
            int r;
            int g;
            int b;
            if (tiles[i].itemID == ItemID.TILE_AIR) {
                workingImage = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6);
                for (int x = 0 ; x < TILE_BASE_WIDTH ; x++) {
                    for (int y = 0 ; y < TILE_BASE_HEIGHT ; y++) {
                        Color pixelColour = new Color(backgroundTexture.getRGB(x, y + (i * TILE_BASE_HEIGHT)), true);
                        a = pixelColour.getAlpha();
                        r = pixelColour.getRed();
                        g = pixelColour.getGreen();
                        b = pixelColour.getBlue();
                        if (a != ImageProcessing.SKY_COLOR.getAlpha() || r != ImageProcessing.SKY_COLOR.getRed() || g != ImageProcessing.SKY_COLOR.getGreen() || b != ImageProcessing.SKY_COLOR.getBlue()) {
                            r /= 2;
                            g /= 2;
                            b /= 2;
                        }
                        workingImage.setRGB(x, y, ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
                    }
                }
            } else {
                if (tiles[i].hasTransparency) {
                    workingImage = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6);
                    for (int x = 0 ; x < TILE_BASE_WIDTH ; x++) {
                        for (int y = 0 ; y < TILE_BASE_HEIGHT ; y++) {
                            Color pixelColour = new Color(backgroundTexture.getRGB(x, y + (i * TILE_BASE_HEIGHT)), true);
                            a = pixelColour.getAlpha();
                            r = pixelColour.getRed();
                            g = pixelColour.getGreen();
                            b = pixelColour.getBlue();
                            if (a != ImageProcessing.SKY_COLOR.getAlpha() || r != ImageProcessing.SKY_COLOR.getRed() || g != ImageProcessing.SKY_COLOR.getGreen() || b != ImageProcessing.SKY_COLOR.getBlue()) {
                                r /= 2;
                                g /= 2;
                                b /= 2;
                            }
                            workingImage.setRGB(x, y, ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
                        }
                    }
                    workingImage = ImageProcessing.overlayImage(workingImage, tiles[i].texture);
                } else {
                    workingImage = tiles[i].texture;
                }
            }
            for (int x = 0 ; x < TILE_BASE_WIDTH ; x++) {
                for (int y = 0 ; y < TILE_BASE_HEIGHT ; y++) {
                    Color pixelColour = new Color(workingImage.getRGB(x, y), true);
                    a = pixelColour.getAlpha();
                    r = pixelColour.getRed();
                    g = pixelColour.getGreen();
                    b = pixelColour.getBlue();
                    chunkTexture.setRGB(x, y + (i * TILE_BASE_HEIGHT), ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
                }
            }
        }
        this.texture = chunkTexture;
    }

    public void stitchBackgroundTexture(Tile[] tiles) {
        BufferedImage chunkTexture = new BufferedImage(TILE_BASE_WIDTH, Game.tiles[0].length * TILE_BASE_HEIGHT, 6);
        for (int i = 0 ; i < tiles.length ; i++) {
            BufferedImage workingImage;
            int a;
            int r;
            int g;
            int b;
            if (tiles[i].itemID == ItemID.TILE_AIR) {
                workingImage = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6);
                for (int x = 0 ; x < TILE_BASE_WIDTH ; x++) {
                    for (int y = 0 ; y < TILE_BASE_HEIGHT ; y++) {
                        Color pixelColour = ImageProcessing.SKY_COLOR;
                        a = pixelColour.getAlpha();
                        r = pixelColour.getRed();
                        g = pixelColour.getGreen();
                        b = pixelColour.getBlue();
                        workingImage.setRGB(x, y, ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
                    }
                }
            } else {
                workingImage = tiles[i].texture;
                for (int x = 0 ; x < TILE_BASE_WIDTH ; x++) {
                    for (int y = 0 ; y < TILE_BASE_HEIGHT ; y++) {
                        Color pixelColour = new Color(workingImage.getRGB(x, y), true);
                        a = pixelColour.getAlpha();
                        r = pixelColour.getRed();
                        g = pixelColour.getGreen();
                        b = pixelColour.getBlue();
                        workingImage.setRGB(x, y, ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
                    }
                }
            }
            for (int x = 0 ; x < TILE_BASE_WIDTH ; x++) {
                for (int y = 0 ; y < TILE_BASE_HEIGHT ; y++) {
                    Color pixelColour = new Color(workingImage.getRGB(x, y), true);
                    a = pixelColour.getAlpha();
                    r = pixelColour.getRed();
                    g = pixelColour.getGreen();
                    b = pixelColour.getBlue();
                    chunkTexture.setRGB(x, y + (i * TILE_BASE_HEIGHT), ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
                }
            }
        }
        this.backgroundTexture = chunkTexture;
    }


    public void modifyChunk(int x, ArrayList<Integer> yValues) {
        BufferedImage chunkTexture = this.texture;

        int a;// red component 0...255
        int r;// = 255;// red component 0...255
        int g;// = 0;// green component 0...255
        int b;// = 0;// blue component 0...255
        int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
        for (int i = 0 ; i < yValues.size() ; i++) {
            Integer y = yValues.get(i);
            BufferedImage workingImage;
            if (Game.tiles[x][y].itemID == ItemID.TILE_AIR) {
                workingImage = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6);
                for (int xB = 0 ; xB < TILE_BASE_WIDTH ; xB++) {
                    for (int yB = 0 ; yB < TILE_BASE_HEIGHT ; yB++) {
                        Color pixelColour = new Color(backgroundTexture.getRGB(xB, yB + (y * TILE_BASE_HEIGHT)), true);
                        a = pixelColour.getAlpha();
                        r = pixelColour.getRed();
                        g = pixelColour.getGreen();
                        b = pixelColour.getBlue();
                        if (r != ImageProcessing.SKY_COLOR.getRed() || g != ImageProcessing.SKY_COLOR.getGreen() || b != ImageProcessing.SKY_COLOR.getBlue()) {
                            r /= 2;
                            g /= 2;
                            b /= 2;
                        }
                        workingImage.setRGB(xB, yB, ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
                    }
                }
            } else {
                workingImage = Game.tiles[x][y].texture;
            }

            if (Game.tiles[x][y].hasTransparency) {
                BufferedImage backImage = Game.backgroundTiles[x][y].texture;
                if (Game.backgroundTiles[x][y].itemID != ItemID.TILE_AIR) {
                    backImage = ImageProcessing.changeBrightness(backImage, 0.5);
                }
                workingImage = ImageProcessing.overlayImage(backImage, workingImage);
            }

            //TODO: Fix this, Cannot invoke "java.awt.image.BufferedImage.getWidth()" because "workingImage" is null, because "chunkTexture" is null, java.lang.NullPointerException
            for (int loopX = 0 ; loopX < workingImage.getWidth() ; loopX++) {
                for (int loopY = y * TILE_BASE_HEIGHT ; loopY < workingImage.getHeight() + (y * TILE_BASE_HEIGHT) ; loopY++) {
                    Color myColour = new Color(workingImage.getRGB(loopX, loopY - (y * TILE_BASE_HEIGHT)), true);
                    a = myColour.getAlpha();
                    r = myColour.getRed();
                    g = myColour.getGreen();
                    b = myColour.getBlue();
                    col = ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
                    chunkTexture.setRGB(loopX, loopY, col);
                }
            }
        }

        this.texture = chunkTexture;
        redrawChunk(ImageProcessing.resizeImage(texture, 4, 4));
    }
}
