import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tiles {
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;
    public static final int TILE_BASE_WIDTH = 16;
    public static final int TILE_BASE_HEIGHT = 16;

    static class TileGraphics implements MouseListener {
        public static final int TILE_LAYER = 2;

        public JLabel textureLabel;
        public BufferedImage texture;
        public BufferedImage backgroundTexture = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT * Game.tiles[0].length, 6);

        public ArrayList<Integer> yValues = new ArrayList<>();

        public TileGraphics(){
            textureLabel = new JLabel();
            textureLabel.setSize(TILE_WIDTH, Game.tiles[0].length * TILE_HEIGHT);
            Game.layeredPane.add(textureLabel);
            Game.layeredPane.setLayer(textureLabel, TILE_LAYER);
            textureLabel.addMouseListener(this);
        }

        public void redrawChunk(Image newTexture){
            textureLabel.setIcon(new ImageIcon(newTexture));
        }

        public void stitchTexture(Tiles.Tile[] tiles){
            BufferedImage chunkTexture = new BufferedImage(TILE_BASE_WIDTH, Game.tiles[0].length * TILE_BASE_HEIGHT, 6);
            for (int i = 0; i < tiles.length; i++) {
                BufferedImage workingImage;
                int a;
                int r;
                int g;
                int b;
                if(tiles[i].itemID == ItemID.TILE_AIR){
                    workingImage = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6);
                    for (int x = 0; x < TILE_BASE_WIDTH; x++) {
                        for (int y = 0; y < TILE_BASE_HEIGHT; y++) {
                            Color pixelColour = new Color(backgroundTexture.getRGB(x, y+(i * TILE_BASE_HEIGHT)));
                            a = pixelColour.getAlpha();
                            r = pixelColour.getRed();
                            g = pixelColour.getGreen();
                            b = pixelColour.getBlue();
                            if(a != ImageProcessing.NULL_COLOR_REPLACEMENT.getAlpha() || r != ImageProcessing.NULL_COLOR_REPLACEMENT.getRed() || g != ImageProcessing.NULL_COLOR_REPLACEMENT.getGreen() || b != ImageProcessing.NULL_COLOR_REPLACEMENT.getBlue()){
                                r /=2;
                                g /=2;
                                b /=2;
                            }
                            workingImage.setRGB(x, y, ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff));
                        }
                    }
                }else{
                    if(tiles[i].hasTransparency){
                        workingImage = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6);
                        for (int x = 0; x < TILE_BASE_WIDTH; x++) {
                            for (int y = 0; y < TILE_BASE_HEIGHT; y++) {
                                Color pixelColour = new Color(backgroundTexture.getRGB(x, y+(i * TILE_BASE_HEIGHT)));
                                a = pixelColour.getAlpha();
                                r = pixelColour.getRed()/2;
                                g = pixelColour.getGreen()/2;
                                b = pixelColour.getBlue()/2;
                                workingImage.setRGB(x, y, ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff));
                            }
                        }
                        workingImage = ImageProcessing.overlayImage(workingImage, tiles[i].texture);
                    }else{
                        workingImage = tiles[i].texture;
                    }
                }
                for (int x = 0; x < TILE_BASE_WIDTH; x++) {
                    for (int y = 0; y < TILE_BASE_HEIGHT; y++) {
                        Color pixelColour = new Color(workingImage.getRGB(x, y));
                        a = pixelColour.getAlpha();
                        r = pixelColour.getRed();
                        g = pixelColour.getGreen();
                        b = pixelColour.getBlue();
                        chunkTexture.setRGB(x, y+(i * TILE_BASE_HEIGHT), ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff));
                    }
                }
            }
            this.texture = chunkTexture;
        }

        public void stitchBackgroundTexture(Tiles.Tile[] tiles){
            BufferedImage chunkTexture = new BufferedImage(TILE_BASE_WIDTH, Game.tiles[0].length * TILE_BASE_HEIGHT, 6);
            for (int i = 0; i < tiles.length; i++) {
                BufferedImage workingImage;
                int a;
                int r;
                int g;
                int b;
                if(tiles[i].itemID == ItemID.TILE_AIR){
                    workingImage = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6);
                    for (int x = 0; x < TILE_BASE_WIDTH; x++) {
                        for (int y = 0; y < TILE_BASE_HEIGHT; y++) {
                            Color pixelColour = ImageProcessing.NULL_COLOR_REPLACEMENT;
                            a = pixelColour.getAlpha();
                            r = pixelColour.getRed();
                            g = pixelColour.getGreen();
                            b = pixelColour.getBlue();
                            workingImage.setRGB(x, y, ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff));
                        }
                    }
                }else{
                    workingImage = tiles[i].texture;
                    for (int x = 0; x < TILE_BASE_WIDTH; x++) {
                        for (int y = 0; y < TILE_BASE_HEIGHT; y++) {
                            Color pixelColour = new Color(workingImage.getRGB(x, y));
                            a = pixelColour.getAlpha();
                            r = pixelColour.getRed();
                            g = pixelColour.getGreen();
                            b = pixelColour.getBlue();
                            workingImage.setRGB(x, y, ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff));
                        }
                    }
                }
                for (int x = 0; x < TILE_BASE_WIDTH; x++) {
                    for (int y = 0; y < TILE_BASE_HEIGHT; y++) {
                        Color pixelColour = new Color(workingImage.getRGB(x, y));
                        a = pixelColour.getAlpha();
                        r = pixelColour.getRed();
                        g = pixelColour.getGreen();
                        b = pixelColour.getBlue();
                        chunkTexture.setRGB(x, y+(i * TILE_BASE_HEIGHT), ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff));
                    }
                }
            }
            this.backgroundTexture = chunkTexture;
        }

        //Re-draw one tile in the chunk without re-drawing the entire chunk
        //TODO: Redo to work with air tiles replacing old tiles
        public void modifyChunk(int x, ArrayList<Integer> yValues){
            BufferedImage chunkTexture = this.texture;

            int a;// red component 0...255
            int r;// = 255;// red component 0...255
            int g;// = 0;// green component 0...255
            int b;// = 0;// blue component 0...255
            int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
            for (int i = 0; i < yValues.size(); i++) {
                Integer y = yValues.get(i);
                BufferedImage workingImage;
                if (Game.tiles[x][y].itemID == ItemID.TILE_AIR) {
                    workingImage = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT, 6);
                    for (int xB = 0; xB < TILE_BASE_WIDTH; xB++) {
                        for (int yB = 0; yB < TILE_BASE_HEIGHT; yB++) {
                            Color pixelColour = new Color(backgroundTexture.getRGB(xB, yB + (y * TILE_BASE_HEIGHT)));
                            a = pixelColour.getAlpha();
                            r = pixelColour.getRed() / 2;
                            g = pixelColour.getGreen() / 2;
                            b = pixelColour.getBlue() / 2;
                            workingImage.setRGB(xB, yB, ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
                        }
                    }
                } else {
                    workingImage = Game.tiles[x][y].texture;
                }

                for (int loopX = 0; loopX < workingImage.getWidth(); loopX++) {
                    for (int loopY = y * TILE_BASE_HEIGHT; loopY < workingImage.getHeight() + (y * TILE_BASE_HEIGHT); loopY++) {
                        Color myColour = new Color(workingImage.getRGB(loopX, loopY - (y * TILE_BASE_HEIGHT)));
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
            redrawChunk(ImageProcessing.resizeImage(texture, 4));
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
                if(Game.tiles[x][y].canBeBroken){
                    Game.tiles[x][y].whenBroken(Game.tiles, x, y);
                }
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

    static class Tile extends TileObject{

        public Rectangle hitbox;
        protected boolean hasTransparency = false;
        public boolean canBeBroken;

        public Tile(int x, int y, int width, int height, ItemID tileID){
            this.itemID = tileID;
            hitbox = new Rectangle(x, y, width, height);
            canBeBroken = tileID != ItemID.TILE_AIR;
            hasTransparency = tileID == ItemID.TILE_STALACTITE || tileID == ItemID.TILE_STALAGMITE;
        }

        //TODO: Integrate this into method in NonMovingObjects
        public void generateTileTextures(){
            texture = generateTexture(itemID);
        }

        @Override
        public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
            System.out.println("Nothing specified for \"whenClicked\" for tile " + tiles[x][y].itemID + " at position [" + x + "][" + y +"]");
        }

        @Override
        public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
            System.out.println("Nothing specified for \"onTileUpdate\" for tile " + tiles[x][y].itemID + " at position [" + x + "][" + y +"]");
        }

        @Override
        public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
            System.out.println("Nothing specified for \"onRandomUpdate\" for tile " + tiles[x][y].itemID + " at position [" + x + "][" + y +"]");
        }
    }

    static class WorldGeneration {
        static class OverworldGeneration implements TileGeneration {
            private static final int SEA_LEVEL = Game.tiles[0].length/16;

            @Override
            public Tile[][] generateBase(Tile[][] tiles) {
                System.out.println();
                System.out.println("Beginning Overworld base generation...");
                long startTime = System.nanoTime();
                PerlinNoise.Perlin1D perlin1D = new PerlinNoise.Perlin1D(Game.WORLD_RANDOM.nextLong());
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = 0; y < tiles[x].length; y++) {
                        int perlinY = (int) Math.round(perlin1D.cosInterpolation((double) x/10)*8)-4;
                        if(y < (SEA_LEVEL + perlinY)){
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_AIR){
                                @Override
                                public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenPlaced(tiles, x, y);
                                }

                                @Override
                                public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenBroken(tiles, x, y);
                                }

                                @Override
                                public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }
                            };
                        }else{
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_STONE){
                                @Override
                                public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenPlaced(tiles, x, y);
                                }

                                @Override
                                public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenBroken(tiles, x, y);
                                }

                                @Override
                                public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }
                            };
                        }
                    }
                }

                //Add grass and dirt to surface
                perlin1D = new PerlinNoise.Perlin1D(Game.WORLD_RANDOM.nextLong());
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = 0; y < tiles[x].length; y++) {
                        if(tiles[x][y].itemID == ItemID.TILE_STONE){
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_DIRT_GRASS){
                                @Override
                                public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenPlaced(tiles, x, y);
                                }

                                @Override
                                public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenBroken(tiles, x, y);
                                }

                                @Override
                                public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }
                            };
                            for (int z = 1; z < ((perlin1D.cosInterpolation(((double) x/6)) * 4)+1); z++) {
                                tiles[x][y + z] = new Tile(x * TILE_WIDTH, (y + z) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_DIRT){
                                    @Override
                                    public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                                        //Do nothing
                                    }

                                    @Override
                                    public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                                        super.whenPlaced(tiles, x, y);
                                    }

                                    @Override
                                    public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                                        super.whenBroken(tiles, x, y);
                                    }

                                    @Override
                                    public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                        //Do nothing
                                    }

                                    @Override
                                    public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                        //Do nothing
                                    }
                                };
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
                perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D, ItemID.TILE_STONE, ItemID.TILE_COAL_ORE, 0.85, 0.8, 3, 7, SEA_LEVEL, tiles[0].length);

                //Iron Ore
                perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  ItemID.TILE_STONE, ItemID.TILE_IRON_ORE, 0.925, 0.875, 6, 2, SEA_LEVEL + 8, tiles[0].length - 8);

                //Gold Ore
                perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  ItemID.TILE_STONE, ItemID.TILE_GOLD_ORE, 0.93, 0.91, 4, 4, SEA_LEVEL + 20, tiles[0].length);

                //Diamond Ore
                perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  ItemID.TILE_STONE, ItemID.TILE_DIAMOND_ORE, 0.975, 0.95, 3, 3, SEA_LEVEL + 32, tiles[0].length);

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
                PerlinNoise.Perlin2D perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                int caveVerticalStretch = 15;
                int caveHorizontalStretch = 5;
                generateCavitySection(tiles, perlin2D, 1, 0.5, caveVerticalStretch, caveHorizontalStretch, SEA_LEVEL, tiles[0].length-45);
                generateCavitySection(tiles, perlin2D, 0.5, 0.75, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-45, tiles[0].length-20);
                generateCavitySection(tiles, perlin2D, 0.75, 0, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-20, tiles[0].length-3);
                generateCavitySection(tiles, perlin2D, 0, 0, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-3, tiles[0].length-1);

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

                //TODO: Make stalagmite/stalactites have a 10% chance after testing.
                final double STALACTITE_CHANCE = 1;
                final double STALAGMITE_CHANCE = 1;

                final ArrayList<ItemID> stalactiteRocks = new ArrayList<>();
                stalactiteRocks.add(ItemID.TILE_STONE);
                stalactiteRocks.add(ItemID.TILE_COAL_ORE);
                stalactiteRocks.add(ItemID.TILE_IRON_ORE);
                stalactiteRocks.add(ItemID.TILE_GOLD_ORE);
                stalactiteRocks.add(ItemID.TILE_DIAMOND_ORE);

                for (int x = 0; x < tiles.length; x++) {
                    boolean hadStalactite = false;
                    for (int y = 0; y < tiles[x].length - 1; y++) {
                        if (stalactiteRocks.contains(tiles[x][y].itemID) && tiles[x][y + 1].itemID == ItemID.TILE_AIR) {
                            if (STALACTITE_CHANCE > Game.WORLD_RANDOM.nextDouble()) {
                                tiles[x][y + 1] = new Tile(x * TILE_WIDTH, (y + 1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_STALACTITE){
                                    @Override
                                    public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                                        //Do nothing
                                    }

                                    @Override
                                    public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                                        super.whenPlaced(tiles, x, y);
                                    }

                                    @Override
                                    public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                                        super.whenBroken(tiles, x, y);
                                    }

                                    @Override
                                    public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                        if(y>0){
                                            if(tiles[x][y-1].itemID == ItemID.TILE_AIR && this.itemID != ItemID.TILE_AIR){
                                                this.whenBroken(tiles, x, y);
                                                Game.movingObjects.add(new FallingStalactite(x * TILE_WIDTH, y * TILE_HEIGHT, this.hitbox.width, this.hitbox.height, ImageProcessing.resizeImage(ImageProcessing.removeNullPixels(this.texture), 4)));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                        //Do nothing
                                    }
                                };
                                tiles[x][y + 1].hasTransparency = true;
                                hadStalactite = true;
                            }
                        }
                        if (stalactiteRocks.contains(tiles[x][y].itemID) && tiles[x][y].itemID != ItemID.TILE_AIR && tiles[x][y - 1].itemID == ItemID.TILE_AIR && hadStalactite) {
                            if (STALAGMITE_CHANCE > Game.WORLD_RANDOM.nextDouble()) {
                                tiles[x][y - 1] = new Tile(x * TILE_WIDTH, (y - 1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_STALAGMITE){
                                    @Override
                                    public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                                        //Do nothing
                                    }

                                    @Override
                                    public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                                        super.whenPlaced(tiles, x, y);
                                    }

                                    @Override
                                    public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                                        super.whenBroken(tiles, x, y);
                                    }

                                    @Override
                                    public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                        if(y<tiles[0].length-1){
                                            if(tiles[x][y+1].itemID == ItemID.TILE_AIR){
                                                this.whenBroken(tiles, x, y);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                        //Do nothing
                                    }
                                };
                                tiles[x][y - 1].hasTransparency = true;
                                hadStalactite = false;
                            }
                        }
                    }
                }
                System.out.println("Overworld detail generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }

            private void generateOreVeins(Tile[][] tiles, PerlinNoise.Perlin2D perlin2D, ItemID targetTileID, ItemID oreTileID, double startingThreshold, double endingThreshold, int verticalStretch, int horizontalStretch, int startingLevel, int endingLevel){
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = startingLevel; y < endingLevel; y++) {

                        double currentThreshold = startingThreshold * (1 - ((double) (y-startingLevel)/((endingLevel-startingLevel)-1))) + endingThreshold * ((double) (y-startingLevel)/((endingLevel-startingLevel)-1));

                        if(perlin2D.cosInterpolation((double) x/horizontalStretch, (double) y/verticalStretch) >= currentThreshold && (tiles[x][y].itemID == targetTileID || targetTileID == null)){
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, oreTileID){
                                @Override
                                public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenPlaced(tiles, x, y);
                                }

                                @Override
                                public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenBroken(tiles, x, y);
                                }

                                @Override
                                public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }
                            };
                        }
                    }
                }
            }

            private void generateCavitySection(Tile[][] tiles, PerlinNoise.Perlin2D perlin2D, double startingThreshold, double endingThreshold, int verticalStretch, int horizontalStretch, int startingLevel, int endingLevel){
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = startingLevel; y < endingLevel; y++) {

                        double currentThreshold = startingThreshold * (1 - ((double) (y-startingLevel)/((endingLevel-startingLevel)-1))) + endingThreshold * ((double) (y-startingLevel)/((endingLevel-startingLevel)-1));

                        if(perlin2D.cosInterpolation((double) x/horizontalStretch, (double) y/verticalStretch) >= currentThreshold){
                            tiles[x][y] = new Tile(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_AIR){
                                @Override
                                public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenPlaced(tiles, x, y);
                                }

                                @Override
                                public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                                    super.whenBroken(tiles, x, y);
                                }

                                @Override
                                public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }

                                @Override
                                public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                                    //Do nothing
                                }
                            };
                        }
                    }
                }
            }
        }
    }
}
