import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Tiles {
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;
    public static final int TILE_BASE_WIDTH = 16;
    public static final int TILE_BASE_HEIGHT = 16;

    public static final Random WORLD_RANDOM = new Random();
    static class TileGraphics implements MouseListener {
        public static final int TILE_LAYER = 1;

        public JLabel textureLabel;
        public BufferedImage texture;

        public TileGraphics(){
            textureLabel = new JLabel();
            textureLabel.setSize(TILE_WIDTH, Game.tiles[0].length * TILE_HEIGHT);
            Game.layeredPane.add(textureLabel);
            Game.layeredPane.setLayer(textureLabel, TILE_LAYER);
            textureLabel.addMouseListener(this);
        }

        public void redrawChunk(Image newTexture){
            textureLabel.setIcon(new ImageIcon(newTexture));
            //textureLabel.paint(textureLabel.getGraphics());
            //textureLabel.revalidate();
            //textureLabel.repaint();

            /*
            try{
                ImageIO.write((RenderedImage) newTexture, "png", new File("/C:\\Users\\HAL 3000\\Desktop\\ChunkTests\\test.png"));
            }catch(IOException e){
                e.printStackTrace();
            )
             */
        }

        public Image stitchChunk(int chunkX){

            BufferedImage chunkTexture = new BufferedImage(TILE_BASE_WIDTH, Game.tiles[0].length * TILE_BASE_HEIGHT, 6);

                int a;// red component 0...255
                int r;// = 255;// red component 0...255
                int g;// = 0;// green component 0...255
                int b;// = 0;// blue component 0...255
                int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                for (int x = 0; x < chunkTexture.getWidth(); x++) {
                    for (int y = 0; y < chunkTexture.getHeight(); y++) {
                        Color myColour;
                        if(Game.tiles[chunkX][y/TILE_BASE_HEIGHT].texture == null){
                            myColour = ImageProcessing.NULL_COLOR_REPLACEMENT;
                        }else{
                            BufferedImage workingImage;
                            if(Game.tiles[chunkX][y/TILE_BASE_HEIGHT].isBroken){
                                workingImage = Game.tiles[chunkX][y/TILE_BASE_HEIGHT].textureBackground;
                            }else{
                                workingImage = Game.tiles[chunkX][y/TILE_BASE_HEIGHT].texture;
                            }
                            myColour = new Color(workingImage.getRGB(x, y % 16));
                        }
                        a = myColour.getAlpha();
                        r = myColour.getRed();
                        g = myColour.getGreen();
                        b = myColour.getBlue();
                        col = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                        chunkTexture.setRGB(x, y, col);
                    }
                }
            this.texture = chunkTexture;
            return texture;
        }

        //Re-draw one tile in the chunk without re-drawing the entire chunk
        public void modifyChunk(int x, ArrayList<Integer> yValues){


            BufferedImage chunkTexture = Game.chunkTextures[x];

            int a;// red component 0...255
            int r;// = 255;// red component 0...255
            int g;// = 0;// green component 0...255
            int b;// = 0;// blue component 0...255
            int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
            for (Integer y : yValues) {
                BufferedImage workingTexture;
                if(Game.tiles[x][y].isBroken){
                    workingTexture = ImageProcessing.resizeImage(Game.tiles[x][y].textureBackground, 4);
                }else{
                    workingTexture = ImageProcessing.resizeImage(Game.tiles[x][y].texture, 4);
                }
                for (int loopX = 0; loopX < workingTexture.getWidth(); loopX++) {
                    for (int loopY = y * TILE_HEIGHT; loopY < workingTexture.getHeight() + (y * TILE_HEIGHT); loopY++) {
                        Color myColour = new Color(workingTexture.getRGB(loopX, loopY - (y * TILE_HEIGHT)));
                        a = myColour.getAlpha();
                        r = myColour.getRed();
                        g = myColour.getGreen();
                        b = myColour.getBlue();
                        col = ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
                        chunkTexture.setRGB(loopX, loopY, col);
                    }
                }
            }

            texture = chunkTexture;
            redrawChunk(chunkTexture);
        }


        @Override
        public void mouseClicked(MouseEvent e) {
            int x = textureLabel.getX()/TILE_WIDTH;
            int y = e.getY()/TILE_HEIGHT;

            int tileMiddleX = (x * TILE_WIDTH) + (TILE_WIDTH/2);
            int tileMiddleY = (y * TILE_HEIGHT) + (TILE_HEIGHT/2);
            int playerMiddleX = (int) Game.movingObjects.get(0).x + (Player.PLAYER_WIDTH/2);
            int playerMiddleY = (int) Game.movingObjects.get(0).y + (Player.PLAYER_HEIGHT/2);
            if(Math.sqrt(Math.pow(tileMiddleX-playerMiddleX, 2)+Math.pow(tileMiddleY-playerMiddleY, 2)) < Player.PLAYER_REACH){
                ArrayList<Integer> yValues = new ArrayList<>();
                if(Game.tiles[x][y].canBeBroken){
                    Game.tiles[x][y].isBroken = true;
                    yValues.add(y);
                    if(y < Game.tiles[x].length){
                        if(Game.tiles[x][y+1].tileID == Tile.TILE_STALACTITE){
                            Game.tiles[x][y+1].isBroken = true;
                            Game.movingObjects.add(new FallingStalactite(x * TILE_WIDTH, (y+1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT));
                            yValues.add(y+1);
                        }
                    }
                    if(y > 0){
                        if(Game.tiles[x][y-1].tileID == Tile.TILE_STALAGMITE){
                            Game.tiles[x][y-1].isBroken = true;
                            yValues.add(y-1);
                        }
                    }
                }
                modifyChunk(x, yValues);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //Do nothing;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //Do nothing;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //Do nothing;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //Do nothing;
        }
    }

    static class Tile {
        public static final int TILE_AIR = 0;
        public static final int TILE_DIRT = 1;
        public static final int TILE_DIRT_GRASS = 2;
        public static final int TILE_STONE = 3;
        public static final int TILE_COAL_ORE = 4;
        public static final int TILE_IRON_ORE = 5;
        public static final int TILE_GOLD_ORE = 6;
        public static final int TILE_DIAMOND_ORE = 7;
        public static final int TILE_STALAGMITE = 8; //The floor one
        public static final int TILE_STALACTITE = 9; //The ceiling one, I hate that I know this by memory now.

        public Rectangle hitbox;
        public BufferedImage texture;
        public BufferedImage textureBackground;
        public int tileID;
        public boolean isBroken;
        public boolean canBeBroken;
        private int backgroundTileID;

        public Tile(int x, int y, int width, int height, int tileID){
            this.tileID = tileID;
            backgroundTileID = tileID;
            hitbox = new Rectangle(x, y, width, height);
            canBeBroken = tileID != TILE_AIR;
        }

        public Tile(int x, int y, int width, int height, int tileID, int backgroundTileID){
            this.tileID = tileID;
            this.backgroundTileID = backgroundTileID;
            hitbox = new Rectangle(x, y, width, height);
            canBeBroken = tileID != TILE_AIR;
        }

        public Tile(int x, int y, int width, int height, int tileID, BufferedImage texture, BufferedImage textureBackground){
            this.tileID = tileID;
            hitbox = new Rectangle(x, y, width, height);
            canBeBroken = tileID != TILE_AIR;
            this.texture = texture;
            this.textureBackground = textureBackground;
        }

        public void generateTileTextures(){
            texture = generateTexture(tileID);
            if(tileID == backgroundTileID){
                textureBackground = ImageProcessing.changeBrightness(texture, 0.5);
            }else{
                textureBackground = ImageProcessing.changeBrightness(generateTexture(backgroundTileID), 0.5);
            }
        }

        private BufferedImage generateTexture(int tileID){
            switch(tileID){
                case TILE_AIR -> {
                    BufferedImage newImage = ImageProcessing.bufferedImageToImage(new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6));
                    int a;// red component 0...255
                    int r;// = 255;// red component 0...255
                    int g;// = 0;// green component 0...255
                    int b;// = 0;// blue component 0...255
                    int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                    for (int x = 0; x < TILE_BASE_WIDTH; x++) {
                        for (int y = 0; y < TILE_BASE_HEIGHT; y++) {
                            a = ImageProcessing.NULL_COLOR_REPLACEMENT.getAlpha();
                            r = ImageProcessing.NULL_COLOR_REPLACEMENT.getRed();
                            g = ImageProcessing.NULL_COLOR_REPLACEMENT.getGreen();
                            b = ImageProcessing.NULL_COLOR_REPLACEMENT.getBlue();
                            col = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                            newImage.setRGB(x, y, col);
                        }
                    }
                    return newImage;
                }
                case TILE_DIRT -> {
                    final ArrayList<String> textureRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        textureRandom.add("textures/tiles/fullTiles/dirt/dirt" + i + ".png");
                    }

                    BufferedImage baseTexture = ImageProcessing.getImageFromResources(textureRandom.get((int) (WORLD_RANDOM.nextDouble() * textureRandom.size())));
                    return ImageProcessing.bufferedImageToImage(ImageProcessing.rotateImageRandomly(baseTexture, (int) (WORLD_RANDOM.nextDouble() * 4)));
                }
                case TILE_DIRT_GRASS -> {
                    final ArrayList<String> dirtRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        dirtRandom.add("textures/tiles/fullTiles/dirt/dirt" + i + ".png");
                    }

                    final ArrayList<String> grassRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        grassRandom.add("textures/tiles/tileOverlays/grass/grass" + i + ".png");
                    }

                    BufferedImage baseDirtTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(dirtRandom.get((int) (WORLD_RANDOM.nextDouble() * dirtRandom.size()))) ,(int) (WORLD_RANDOM.nextDouble() * 4));
                    BufferedImage baseGrassTexture = ImageProcessing.getImageFromResources(grassRandom.get((int) (WORLD_RANDOM.nextDouble() * grassRandom.size())));
                    return ImageProcessing.overlayImage(baseDirtTexture, baseGrassTexture);

                }
                case TILE_STONE -> {
                    final ArrayList<String> textureRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        textureRandom.add("textures/tiles/fullTiles/stone/stone" + i + ".png");
                    }

                    BufferedImage baseTexture = ImageProcessing.getImageFromResources(textureRandom.get((int) (WORLD_RANDOM.nextDouble() * textureRandom.size())));
                    return ImageProcessing.rotateImageRandomly(baseTexture, (int) (WORLD_RANDOM.nextDouble() * 4));
                }
                case TILE_COAL_ORE -> {
                    final ArrayList<String> stoneRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        stoneRandom.add("textures/tiles/fullTiles/stone/stone" + i + ".png");
                    }

                    final ArrayList<String> coalRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        coalRandom.add("textures/tiles/tileOverlays/coalOre/coalOre" + i + ".png");
                    }

                    BufferedImage baseStoneTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(stoneRandom.get((int) (WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (WORLD_RANDOM.nextDouble() * 4));
                    BufferedImage baseCoalTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(coalRandom.get((int) (WORLD_RANDOM.nextDouble() * coalRandom.size()))), (int) (WORLD_RANDOM.nextDouble() * 4));
                    return ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
                }
                case TILE_IRON_ORE -> {
                    final ArrayList<String> stoneRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        stoneRandom.add("textures/tiles/fullTiles/stone/stone" + i + ".png");
                    }

                    final ArrayList<String> ironRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        ironRandom.add("textures/tiles/tileOverlays/ironOre/ironOre" + i + ".png");
                    }

                    BufferedImage baseStoneTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(stoneRandom.get((int) (WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (WORLD_RANDOM.nextDouble() * 4));
                    BufferedImage baseCoalTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(ironRandom.get((int) (WORLD_RANDOM.nextDouble() * ironRandom.size()))), (int) (WORLD_RANDOM.nextDouble() * 4));
                    return ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
                }
                case TILE_GOLD_ORE -> {
                    final ArrayList<String> stoneRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        stoneRandom.add("textures/tiles/fullTiles/stone/stone" + i + ".png");
                    }

                    final ArrayList<String> goldRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        goldRandom.add("textures/tiles/tileOverlays/goldOre/goldOre" + i + ".png");
                    }

                    BufferedImage baseStoneTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(stoneRandom.get((int) (WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (WORLD_RANDOM.nextDouble() * 4));
                    BufferedImage baseCoalTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(goldRandom.get((int) (WORLD_RANDOM.nextDouble() * goldRandom.size()))), (int) (WORLD_RANDOM.nextDouble() * 4));
                    return ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
                }
                case TILE_DIAMOND_ORE -> {
                    final ArrayList<String> stoneRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        stoneRandom.add("textures/tiles/fullTiles/stone/stone" + i + ".png");
                    }

                    final ArrayList<String> diamondRandom = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        diamondRandom.add("textures/tiles/tileOverlays/diamondOre/diamondOre" + i + ".png");
                    }

                    BufferedImage baseStoneTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(stoneRandom.get((int) (WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (WORLD_RANDOM.nextDouble() * 4));
                    BufferedImage baseCoalTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(diamondRandom.get((int) (WORLD_RANDOM.nextDouble() * diamondRandom.size()))), (int) (WORLD_RANDOM.nextDouble() * 4));
                    return ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
                }
                default -> {
                    return ImageProcessing.getImageFromResources("textures/missingTexture.png");
                }
            }
        }
    }

    static class WorldGeneration {
        static class OverworldGeneration implements TileGeneration {
            private static final int SEA_LEVEL = Game.tiles[0].length/8;

            @Override
            public Tile[][] generateBase(Tile[][] tiles) {
                System.out.println();
                System.out.println("Beginning Overworld base generation...");
                long startTime = System.nanoTime();
                PerlinNoise.Perlin1D perlin1D = new PerlinNoise.Perlin1D(WORLD_RANDOM.nextLong());
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = 0; y < tiles[x].length; y++) {
                        int perlinY = (int) Math.round(perlin1D.cosInterpolation((double) x/10)*8)-4;
                        if(y < (SEA_LEVEL + perlinY)){
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Tile.TILE_AIR);
                        }else{
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Tile.TILE_STONE);
                        }
                    }
                }

                //Add grass and dirt to surface
                perlin1D = new PerlinNoise.Perlin1D(WORLD_RANDOM.nextLong());
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = 0; y < tiles[x].length; y++) {
                        if(tiles[x][y].tileID == Tile.TILE_STONE){
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Tile.TILE_DIRT_GRASS);
                            for (int z = 1; z < ((perlin1D.cosInterpolation(((double) x/6)) * 4)+1); z++) {
                                tiles[x][y + z] = new Tile(x * TILE_WIDTH, (y + z) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Tile.TILE_DIRT);
                            }
                            break;
                        }
                    }
                }
                System.out.println("Overworld base generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }

            @Override
            public Tile[][] generateOres(Tile[][] tiles) {
                System.out.println();
                System.out.println("Beginning Overworld ore generation...");
                long startTime = System.nanoTime();

                //TODO: fix ore dispersion at different levels, make only certain ores available at certain z levels

                //To increase # of ore pockets, lower threshold and stretch values
                PerlinNoise.Perlin2D perlin2D;

                //Coal Ore
                perlin2D = new PerlinNoise.Perlin2D(WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D, Tile.TILE_STONE, Tile.TILE_COAL_ORE, 0.85, 0.8, 3, 7, SEA_LEVEL, tiles[0].length);

                //Iron Ore
                perlin2D = new PerlinNoise.Perlin2D(WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  Tile.TILE_STONE, Tile.TILE_IRON_ORE, 0.925, 0.875, 6, 2, SEA_LEVEL + 8, tiles[0].length - 8);

                //Gold Ore
                perlin2D = new PerlinNoise.Perlin2D(WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  Tile.TILE_STONE, Tile.TILE_GOLD_ORE, 0.93, 0.91, 4, 4, SEA_LEVEL + 20, tiles[0].length);

                //Diamond Ore
                perlin2D = new PerlinNoise.Perlin2D(WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  Tile.TILE_STONE, Tile.TILE_DIAMOND_ORE, 0.975, 0.95, 3, 3, SEA_LEVEL + 32, tiles[0].length);

                System.out.println("Overworld ore generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }

            @Override
            public Tile[][] generateCavities(Tile[][] tiles) {
                System.out.println();
                System.out.println("Beginning Overworld cavity generation...");
                long startTime = System.nanoTime();

                //TODO: Make cavities more common/larger higher in the level and less common/smaller lower in the level

                //Air Cavities
                PerlinNoise.Perlin2D perlin2D = new PerlinNoise.Perlin2D(WORLD_RANDOM.nextLong());
                int caveVerticalStretch = 15;
                int caveHorizontalStretch = 5;
                breakTiles(tiles, perlin2D, 1, 0.5, caveVerticalStretch, caveHorizontalStretch, SEA_LEVEL, tiles[0].length-45);
                breakTiles(tiles, perlin2D, 0.5, 0.75, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-45, tiles[0].length-20);
                breakTiles(tiles, perlin2D, 0.75, 0, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-20, tiles[0].length-3);
                breakTiles(tiles, perlin2D, 0, 0, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-3, tiles[0].length);

                System.out.println("Overworld cavity generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }

            @Override
            public Tile[][] generateWorldFloor(Tile[][] tiles) {
                System.out.println();
                System.out.println("Beginning Overworld floor generation...");
                long startTime = System.nanoTime();

                for (int x = 0; x < Game.tiles.length; x++) {
                    tiles[x][Game.tiles[x].length-1].isBroken = false;
                    tiles[x][Game.tiles[x].length-1].canBeBroken = false;
                }

                System.out.println("Overworld floor generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }

            @Override
            public Tile[][] generatePortals(Tile[][] tiles) {
                System.out.println();
                System.out.println("Beginning Overworld portal generation...");
                long startTime = System.nanoTime();



                System.out.println("Overworld portal generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }

            @Override
            public Tile[][] generateDetails(Tile[][] tiles) {
                System.out.println();
                System.out.println("Beginning Overworld detail generation...");
                long startTime = System.nanoTime();

                final double STALACTITE_CHANCE = 0.1;
                final double STALAGMITE_CHANCE = 0.1;
                for (int x = 0; x < tiles.length; x++) {
                    boolean hadStalactite = false;
                    for (int y = 0; y < tiles[x].length - 1; y++) {
                        if (tiles[x][y].tileID >= 3 && tiles[x][y].tileID <= 7 && !tiles[x][y].isBroken && tiles[x][y + 1].isBroken) {
                            if (STALACTITE_CHANCE > WORLD_RANDOM.nextDouble()) {
                                tiles[x][y + 1] = new Tile(x * TILE_WIDTH, (y + 1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Tile.TILE_STALACTITE, tiles[x][y + 1].tileID);
                                hadStalactite = true;
                            }
                        }
                        if (tiles[x][y].tileID >= 3 && tiles[x][y].tileID <= 7 && !tiles[x][y].isBroken && tiles[x][y - 1].isBroken && hadStalactite) {
                            if (STALAGMITE_CHANCE > WORLD_RANDOM.nextDouble()) {
                                tiles[x][y - 1] = new Tile(x * TILE_WIDTH, (y - 1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Tile.TILE_STALAGMITE, tiles[x][y - 1].tileID);
                                hadStalactite = false;
                            }
                        }
                    }
                }
                System.out.println("Overworld detail generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }

            private void generateOreVeins(Tile[][] tiles, PerlinNoise.Perlin2D perlin2D, int targetTileID, int oreTileID, double startingThreshold, double endingThreshold, int verticalStretch, int horizontalStretch, int startingLevel, int endingLevel){
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = startingLevel; y < endingLevel; y++) {

                        double currentThreshold = startingThreshold * (1 - ((double) (y-startingLevel)/((endingLevel-startingLevel)-1))) + endingThreshold * ((double) (y-startingLevel)/((endingLevel-startingLevel)-1));

                        if(perlin2D.cosInterpolation((double) x/horizontalStretch, (double) y/verticalStretch) >= currentThreshold && (tiles[x][y].tileID == targetTileID || targetTileID == -1)){
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, oreTileID);
                        }
                    }
                }
            }

            private void breakTiles(Tile[][] tiles, PerlinNoise.Perlin2D perlin2D, double startingThreshold, double endingThreshold, int verticalStretch, int horizontalStretch, int startingLevel, int endingLevel){
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = startingLevel; y < endingLevel; y++) {

                        double currentThreshold = startingThreshold * (1 - ((double) (y-startingLevel)/((endingLevel-startingLevel)-1))) + endingThreshold * ((double) (y-startingLevel)/((endingLevel-startingLevel)-1));

                        if(perlin2D.cosInterpolation((double) x/horizontalStretch, (double) y/verticalStretch) >= currentThreshold){
                            tiles[x][y].isBroken = true;
                        }
                    }
                }
            }
        }
    }
}
