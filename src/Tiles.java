import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class Tiles {
    /***/public static final int TILE_WIDTH = 64;
    /***/public static final int TILE_HEIGHT = 64;
    /***/public static final int TILE_BASE_WIDTH = 16;
    /***/public static final int TILE_BASE_HEIGHT = 16;

    static class TileGraphics{

        /***/public JLabel textureLabel;
        /***/public BufferedImage texture;
        /***/public BufferedImage backgroundTexture = new BufferedImage(TILE_BASE_WIDTH, TILE_BASE_HEIGHT * Game.tiles[0].length, 6);

        public ArrayList<Integer> yValues = new ArrayList<>();

        public TileGraphics(){
            textureLabel = new JLabel();
            textureLabel.setSize(TILE_WIDTH, Game.tiles[0].length * TILE_HEIGHT);
            Game.layeredPane.add(textureLabel);
            Game.layeredPane.setLayer(textureLabel, ImageProcessing.TILE_LAYER);
            //textureLabel.addMouseListener(this);
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
                            if(a != ImageProcessing.SKY_COLOR.getAlpha() || r != ImageProcessing.SKY_COLOR.getRed() || g != ImageProcessing.SKY_COLOR.getGreen() || b != ImageProcessing.SKY_COLOR.getBlue()){
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
                            Color pixelColour = ImageProcessing.SKY_COLOR;
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
                            r = pixelColour.getRed();
                            g = pixelColour.getGreen();
                            b = pixelColour.getBlue();
                            if(r != ImageProcessing.SKY_COLOR.getRed() || g != ImageProcessing.SKY_COLOR.getGreen() || b != ImageProcessing.SKY_COLOR.getBlue()){
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
            redrawChunk(ImageProcessing.resizeImage(texture, 4, 4));
        }
    }

    static class Tile extends HoldableObject{

        /***/public Rectangle hitbox;
        /***/public boolean canBeBroken;
        /***/public ItemID dropItemID;

        public Tile(int x, int y, int width, int height, ItemID itemID){
            super(itemID);
            hitbox = new Rectangle(x, y, width, height);
            canBeBroken = itemID != ItemID.TILE_AIR;
            hasTransparency = itemID == ItemID.TILE_STALACTITE || itemID == ItemID.TILE_STALAGMITE;
        }

        public static boolean placeTile(int x, int y, ItemID itemID, MovingObject placedByEntity, boolean forcePlace){
            boolean foundFoothold = forcePlace;
            if(x > 0){
                if(Game.tiles[x-1][y].itemID != ItemID.TILE_AIR && !foundFoothold){
                    foundFoothold = true;
                }
            }
            if(x < Game.tiles.length-1 && !foundFoothold){
                if(Game.tiles[x+1][y].itemID != ItemID.TILE_AIR){
                    foundFoothold = true;
                }
            }
            if(y > 0 && !foundFoothold){
                if(Game.tiles[x][y-1].itemID != ItemID.TILE_AIR){
                    foundFoothold = true;
                }
            }
            if(y < Game.tiles[x].length-1 && !foundFoothold){
                if(Game.tiles[x][y+1].itemID != ItemID.TILE_AIR){
                    foundFoothold = true;
                }
            }
            if(Game.backgroundTiles[x][y].itemID != ItemID.TILE_AIR && !foundFoothold){
                foundFoothold = true;
            }
            if(foundFoothold){
                Game.tiles[x][y] = Tiles.Tile.TilePresets.getTilePreset(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, itemID);
                Game.tiles[x][y].whenPlaced(Game.tiles, x, y, placedByEntity);
                return true;
            }
            return false;
        }


        public void generateTileTextures(){
            texture = generateTexture(itemID);
        }

        public void whenUsed(Tiles.Tile[][] tiles, int x, int y){
            System.out.println("WARNING! Nothing Specified for " + this.itemID + " whenUsed at x" + x + ", y" + y);
        }


        public void whenPlaced(Tiles.Tile[][] tiles, int x, int y, MovingObject placedByEntity){
            if(placedByEntity != null){
                placedByEntity.inventory.removeFromInventory(tiles[x][y], 1);
            }
            triggerUpdate(tiles, x, y);
            tiles[x][y].generateTileTextures();
            if(Game.chunks != null && Game.chunks[x] != null){
                Game.chunks[x].yValues.add(y);
            }
        }


        public void whenBroken(Tiles.Tile[][] tiles, int x, int y, MovingObject brokenByEntity){
            if(tiles[x][y].itemID != ItemID.TILE_AIR){
                Game.tiles[x][y] = Tile.TilePresets.getTilePreset(x * Tiles.TILE_WIDTH, y * Tiles.TILE_HEIGHT, Tiles.TILE_WIDTH, Tiles.TILE_HEIGHT, ItemID.TILE_AIR);
                if(brokenByEntity != null){
                    brokenByEntity.inventory.addToInventory(new HoldableObject(this.dropItemID), 1);
                }
                triggerUpdate(tiles, x, y);
                if(Game.chunks != null && Game.chunks[x] != null){
                    Game.chunks[x].yValues.add(y);
                }
            }
        }


        public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y){
            System.out.println("WARNING! Nothing Specified for " + this.itemID + " onTileUpdate at x" + x + ", y" + y);
        }


        public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance){
            System.out.println("WARNING! Nothing Specified for " + this.itemID + " onRandomUpdate at x" + x + ", y" + y);
        }

        private void triggerUpdate(Tiles.Tile[][] tiles, int x, int y){
            if(x>0){
                tiles[x-1][y].onTileUpdate(tiles, x-1, y);
            }
            if(x<tiles.length-1){
                tiles[x+1][y].onTileUpdate(tiles, x+1, y);
            }
            if(y>0){
                tiles[x][y-1].onTileUpdate(tiles, x, y-1);
            }
            if(y<tiles[0].length-1){
                tiles[x][y+1].onTileUpdate(tiles, x, y+1);
            }
        }

        /**
         *
         */
        protected static class TilePresets{
            private final int MAX_TREE_HEIGHT = 10;

            public static Tile getTilePreset(int x, int y, int width, int height, ItemID tileID){
                Tile finalTile;
                final double TREE_GROWTH_THRESHOLD = 0.95;
                switch (tileID) {
                    case TILE_AIR -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_AIR) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.dropItemID = null;
                    }
                    case TILE_STONE -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_STONE) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_STONE;
                    }
                    case TILE_DIRT_GRASS -> {
                        final double KILL_GRASS_THRESHOLD = 0.5;
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_DIRT_GRASS) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                if (y > 0 && Game.TICK_RANDOM.nextDouble() > KILL_GRASS_THRESHOLD || overrideRandomChance) {
                                    boolean tileFound = IntStream.range(0, y).anyMatch(i -> (tiles[x][i].itemID != ItemID.TILE_AIR && !tiles[x][i].hasTransparency));
                                    if (tileFound) {
                                        Tile.placeTile(x, y, ItemID.TILE_DIRT, null, true);
                                    }
                                }
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_DIRT;
                    }
                    case TILE_DIRT -> {
                        final double GROW_GRASS_THRESHOLD = 0.5;
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_DIRT) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                if (y > 0 && Game.TICK_RANDOM.nextDouble() > GROW_GRASS_THRESHOLD || overrideRandomChance) {
                                    boolean tileFound = IntStream.range(0, y).anyMatch(i -> (tiles[x][i].itemID != ItemID.TILE_AIR && !tiles[x][i].hasTransparency));
                                    if (!tileFound) {
                                        Tile.placeTile(x, y, ItemID.TILE_DIRT_GRASS, null, true);
                                    }
                                }
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_DIRT;
                    }
                    case TILE_COAL_ORE -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_COAL_ORE) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_COAL_ORE;
                    }
                    case TILE_IRON_ORE -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_IRON_ORE) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_IRON_ORE;
                    }
                    case TILE_GOLD_ORE -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_GOLD_ORE) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_GOLD_ORE;
                    }
                    case TILE_DIAMOND_ORE -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_DIAMOND_ORE) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_DIAMOND_ORE;
                    }
                    case TILE_STALACTITE -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_STALACTITE) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                if (y > 0) {
                                    if (tiles[x][y - 1].itemID == ItemID.TILE_AIR && this.itemID != ItemID.TILE_AIR) {
                                        this.whenBroken(tiles, x, y, null);
                                        Game.movingObjects.add(new FallingStalactite(x * TILE_WIDTH, y * TILE_HEIGHT, this.hitbox.width, this.hitbox.height, ImageProcessing.resizeImage(ImageProcessing.removeNullPixels(this.texture), 4, 4)));
                                    }
                                }
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.hasTransparency = true;
                        finalTile.dropItemID = null;
                        finalTile.hitbox = new Rectangle(x + 20, y, width - 40, height - 16);
                    }
                    case TILE_STALAGMITE -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_STALAGMITE) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                if (y < tiles[0].length - 1) {
                                    if (tiles[x][y + 1].itemID == ItemID.TILE_AIR) {
                                        this.whenBroken(tiles, x, y, null);
                                    }
                                }
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.hasTransparency = true;
                        finalTile.dropItemID = null;
                        finalTile.hitbox = new Rectangle(x + 20, y + 16, width - 40, height - 16);
                    }
                    case TILE_LOG -> {
                        final double LEAFY_LOGS_PER_NORMAL_LOG = 3;
                        final int TREE_MAX_HEIGHT = 12;
                        final double NEW_LEAF_THRESHOLD = 0.5;
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_LOG) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                if (y < tiles.length) {
                                    if (!(tiles[x][y + 1].itemID == ItemID.TILE_LOG || tiles[x][y + 1].itemID == ItemID.TILE_LEAFY_LOG || tiles[x][y + 1].itemID == ItemID.TILE_DIRT || tiles[x][y + 1].itemID == ItemID.TILE_DIRT_GRASS)) {
                                        this.whenBroken(tiles, x, y, null);
                                    }
                                }
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //TODO: Fix bug where trees get decapitated if the leaves of 2 trees intersect.

                                //If the log is on the ground, and is within range, and the random number is higher than the threshold, grow the tree.
                                if((Game.TICK_RANDOM.nextDouble() > TREE_GROWTH_THRESHOLD || overrideRandomChance) && y < tiles.length-1 && (tiles[x][y+1].itemID == ItemID.TILE_DIRT || tiles[x][y+1].itemID == ItemID.TILE_DIRT_GRASS)){

                                    int currentTreeHeight = TREE_MAX_HEIGHT;
                                    double normalLogLength = 1 - (LEAFY_LOGS_PER_NORMAL_LOG / (LEAFY_LOGS_PER_NORMAL_LOG + 1));
                                    boolean growthBlocked = false;
                                    int newNumOfLeafyLogs = 0;
                                    int oldNumOfLeafyLogs = 0;

                                    //Determine the current height of the tree, and if growth is blocked.
                                    for (int i = y - 1 ; i >= 0 && y-i < TREE_MAX_HEIGHT ; i--) {
                                        if (tiles[x][i].itemID != ItemID.TILE_LOG && tiles[x][i].itemID != ItemID.TILE_LEAFY_LOG){
                                            currentTreeHeight = ((y - 1)-(i-1));
                                            if(tiles[x][i].itemID != ItemID.TILE_LOG && tiles[x][i].itemID != ItemID.TILE_LEAFY_LOG && tiles[x][i].itemID != ItemID.TILE_LEAVES && tiles[x][i].itemID != ItemID.TILE_AIR){
                                                growthBlocked = true;
                                            }
                                            break;
                                        }
                                    }

                                    //If the tree can still grow, continue growing.
                                    if(!growthBlocked && currentTreeHeight < TREE_MAX_HEIGHT){

                                        //Determine the number of leafy logs before trunk growth
                                        for (int i = currentTreeHeight ; i >= 0 ; i--) {
                                            if(tiles[x][y-i].itemID == ItemID.TILE_LEAFY_LOG){
                                                oldNumOfLeafyLogs++;
                                            }
                                        }

                                        //Grow the trunk, adding leafy logs where necessary.
                                        int leftLeafLength = 0;
                                        int rightLeafLength = 0;
                                        for (int i = currentTreeHeight ; i >= 0 ; i--) {
                                            if((double) i/(currentTreeHeight + 1) >= normalLogLength){
                                                if(tiles[x][y-i].itemID != ItemID.TILE_LEAFY_LOG && tiles[x][y-i].itemID != ItemID.TILE_LOG){
                                                    Tile.placeTile(x, y - i, ItemID.TILE_LEAFY_LOG, null, true);
                                                }
                                            }else{
                                                if(tiles[x][y-i].itemID != ItemID.TILE_LOG){
                                                    for (int j = 1 ; j < oldNumOfLeafyLogs + 1 ; j++) {
                                                        if(x-j >= 0 && x-j < tiles.length && y-i >= 0 && y-i < tiles[x-i].length){
                                                            if(tiles[x-j][y-i].itemID == ItemID.TILE_LEAVES){
                                                                leftLeafLength++;
                                                            }
                                                            if(tiles[x+j][y-i].itemID == ItemID.TILE_LEAVES){
                                                                rightLeafLength++;
                                                            }
                                                        }
                                                    }
                                                    Tile.placeTile(x, y - i, ItemID.TILE_LOG, null, true);
                                                }
                                            }
                                        }

                                        //Determine the number of leafy logs after trunk growth
                                        for (int i = currentTreeHeight ; i >= 0 ; i--) {
                                            if(tiles[x][y-i].itemID == ItemID.TILE_LEAFY_LOG){
                                                newNumOfLeafyLogs++;
                                            }
                                        }

                                        //Place leaves on top of the tree if space permits
                                        if(y - (currentTreeHeight + 1) >= 0 && tiles[x][y - (currentTreeHeight + 1)].itemID == ItemID.TILE_AIR){
                                            Tile.placeTile(x, y - (currentTreeHeight + 1), ItemID.TILE_LEAVES, null, false);
                                        }

                                        //Shift the existing leaves up one tile
                                        for (int i = 0 ; i < newNumOfLeafyLogs+1 ; i++) {
                                            for (int j = 1 ; j < i + 1; j++) {
                                                if(x-j >= 0 && tiles[x-j][(y - currentTreeHeight) + i].itemID == ItemID.TILE_LEAVES && tiles[x-j][((y - currentTreeHeight) + i) - 1].itemID == ItemID.TILE_AIR){
                                                    placeTile(x-j, ((y - currentTreeHeight) + i) - 1, ItemID.TILE_LEAVES, null, false);
                                                }
                                                if(x+j < tiles.length &&tiles[x+j][(y - currentTreeHeight) + i].itemID == ItemID.TILE_LEAVES && tiles[x+j][((y - currentTreeHeight) + i) - 1].itemID == ItemID.TILE_AIR){
                                                    placeTile(x+j, ((y - currentTreeHeight) + i) - 1, ItemID.TILE_LEAVES, null, false);
                                                }
                                            }
                                        }
                                        if(leftLeafLength != 0 || rightLeafLength != 0){
                                            int newLeafY = y-(currentTreeHeight-newNumOfLeafyLogs)-1;
                                            for (int i = 1 ; i <= leftLeafLength; i++) {
                                                if(tiles[x-i][newLeafY].itemID == ItemID.TILE_AIR){
                                                    Tile.placeTile(x-i, newLeafY, ItemID.TILE_LEAVES, null ,false);
                                                }
                                            }
                                            for (int i = 1 ; i <= rightLeafLength ; i++) {
                                                if(tiles[x+i][newLeafY].itemID == ItemID.TILE_AIR){
                                                    Tile.placeTile(x+i, newLeafY, ItemID.TILE_LEAVES, null ,false);
                                                }
                                            }
                                        }

                                        //Add a new layer of leaves if there is a new leafy tile.
                                        if(oldNumOfLeafyLogs != newNumOfLeafyLogs){
                                            int newLeafY = (y-(currentTreeHeight-newNumOfLeafyLogs))-1;
                                            //left
                                            if(Game.TICK_RANDOM.nextDouble() > NEW_LEAF_THRESHOLD){
                                                int newLeafX = x;
                                                for (int i = 1 ; i <= newNumOfLeafyLogs ; i++) {
                                                    if(tiles[x-i][newLeafY].itemID == ItemID.TILE_AIR){
                                                        newLeafX = x-i;
                                                        break;
                                                    }
                                                }
                                                placeTile(newLeafX, newLeafY, ItemID.TILE_LEAVES, null, false);
                                            }
                                            //right
                                            if(Game.TICK_RANDOM.nextDouble() > NEW_LEAF_THRESHOLD){
                                                int newLeafX = x;
                                                for (int i = 1 ; i <= newNumOfLeafyLogs ; i++) {
                                                    if(tiles[x+i][newLeafY].itemID == ItemID.TILE_AIR){
                                                        newLeafX = x+i;
                                                        break;
                                                    }
                                                }
                                                placeTile(newLeafX, newLeafY, ItemID.TILE_LEAVES, null, false);
                                            }
                                        }
                                    }
                                }
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_LOG;
                    }
                    case TILE_LEAVES -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_LEAVES) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //left
                                boolean killLeaf = true;
                                int newX = x;
                                while(tiles[newX][y].itemID == ItemID.TILE_LEAVES && killLeaf){
                                    newX--;
                                    if(newX < 0){
                                        break;
                                    }
                                    if(tiles[newX][y].itemID == ItemID.TILE_LEAFY_LOG){
                                        killLeaf = false;
                                    }
                                }
                                //right
                                newX = x;
                                while(tiles[newX][y].itemID == ItemID.TILE_LEAVES && killLeaf){
                                    newX++;
                                    if(newX > tiles.length-1){
                                        break;
                                    }
                                    if(tiles[newX][y].itemID == ItemID.TILE_LEAFY_LOG){
                                        killLeaf = false;
                                    }
                                }
                                if(y < tiles[x].length-1){
                                    if(tiles[x][y+1].itemID == ItemID.TILE_LEAFY_LOG){
                                        killLeaf = false;
                                    }
                                }
                                if(killLeaf){
                                    this.whenBroken(tiles, x, y, null);
                                }
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.hasTransparency = true;
                        finalTile.dropItemID = null;
                    }
                    case TILE_LEAFY_LOG -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_LEAFY_LOG) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                if (y < tiles.length) {
                                    if (!(tiles[x][y + 1].itemID == ItemID.TILE_LOG || tiles[x][y + 1].itemID == ItemID.TILE_LEAFY_LOG || tiles[x][y + 1].itemID == ItemID.TILE_DIRT || tiles[x][y + 1].itemID == ItemID.TILE_DIRT_GRASS)) {
                                        this.whenBroken(tiles, x, y, null);
                                    }
                                }
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                if(y < tiles[x].length && y > 0 && (tiles[x][y+1].itemID == ItemID.TILE_DIRT || tiles[x][y+1].itemID == ItemID.TILE_DIRT_GRASS) && (tiles[x][y-1].itemID == ItemID.TILE_AIR || tiles[x][y-1].itemID == ItemID.TILE_LEAVES) && (Game.TICK_RANDOM.nextDouble() > TREE_GROWTH_THRESHOLD || overrideRandomChance)){
                                    Tile.placeTile(x, y, ItemID.TILE_LOG, null, true);
                                    Tile.placeTile(x, y-1, ItemID.TILE_LEAFY_LOG, null, true);
                                    if(y>1 && tiles[x][y-2].itemID == ItemID.TILE_AIR){
                                        Tile.placeTile(x, y-2, ItemID.TILE_LEAVES, null, true);
                                    }
                                    if(x - 1 >= 0 && tiles[x-1][y-1].itemID == ItemID.TILE_AIR){
                                        Tile.placeTile(x-1, y-1, ItemID.TILE_LEAVES, null, true);
                                    }
                                    if(x + 1 < tiles.length && tiles[x+1][y-1].itemID == ItemID.TILE_AIR){
                                        Tile.placeTile(x+1, y-1, ItemID.TILE_LEAVES, null, true);
                                    }
                                }
                            }
                        };
                        //finalTile.hasTransparency = true;
                        finalTile.dropItemID = ItemID.TILE_LEAFY_LOG;
                    }
                    case ITEM_PINE_CONE -> {
                        finalTile = new Tile(x, y, width, height, ItemID.ITEM_PINE_CONE) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                    }
                    case TILE_TREE_STARTER -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_TREE_STARTER) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                if(Game.TICK_RANDOM.nextDouble() > TREE_GROWTH_THRESHOLD || overrideRandomChance){
                                    Tile.placeTile(x, y, ItemID.TILE_LEAFY_LOG, null, false);
                                    if(y > 0 && tiles[x][y-1].itemID == ItemID.TILE_AIR){
                                        Tile.placeTile(x, y-1, ItemID.TILE_LEAVES, null, false);
                                    }
                                }
                            }
                        };
                    }
                    default -> {
                        finalTile = new Tile(x, y, width, height, ItemID.TILE_AIR) {
                            @Override
                            public void whenUsed(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                                super.whenPlaced(tiles, x, y, placedByEntity);
                            }

                            @Override
                            public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                                super.whenBroken(tiles, x, y, brokenByEntity);
                            }

                            @Override
                            public void onTileUpdate(Tile[][] tiles, int x, int y) {
                                //Do nothing
                            }

                            @Override
                            public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                                //Do nothing
                            }
                        };
                        finalTile.dropItemID = ItemID.TILE_AIR;
                    }
                }

                /*
                else{
                    finalTile = new Tile(x, y, width, height, ItemID.TILE_AIR) {
                        @Override
                        public void whenUsed(Tile[][] tiles, int x, int y) {
                            //Do nothing
                        }

                        @Override
                       public void whenPlaced(Tiles.Tile[][] tiles, int x, int y, MovingObject placedByEntity){
                            super.whenPlaced(tiles, x, y, placedByEntity);
                        }

                        @Override
                        public void whenBroken(Tile[][] tiles, int x, int y, MovingObject brokenByEntity) {
                            super.whenBroken(tiles, x, y, brokenByEntity);
                        }

                        @Override
                        public void onTileUpdate(Tile[][] tiles, int x, int y) {
                            //Do nothing
                        }

                        @Override
                        public void onRandomUpdate(Tile[][] tiles, int x, int y) {
                            //Do nothing
                        }
                    };
                    finalTile.dropItemID = ItemID.TILE_AIR;
                }
                 */
                return finalTile;
            }
        }
    }

    static class WorldGeneration {
        static class OverworldGeneration implements TileGeneration {
            /***/private static final int SEA_LEVEL = Game.tiles[0].length/16;

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
                            tiles[x][y] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_AIR);
                        }else{
                            tiles[x][y] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_STONE);
                        }
                    }
                }

                //Add grass and dirt to surface
                perlin1D = new PerlinNoise.Perlin1D(Game.WORLD_RANDOM.nextLong());
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = 0; y < tiles[x].length; y++) {
                        if(tiles[x][y].itemID == ItemID.TILE_STONE){
                            tiles[x][y] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_DIRT_GRASS);
                            for (int z = 1; z < ((perlin1D.cosInterpolation(((double) x/6)) * 4)+1); z++) {
                                tiles[x][y + z] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, (y + z) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_DIRT);
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
                //Generates primarily in mid-area of the overworld
                int coalVerticalStretch = 3;
                int coalHorizontalStretch = 7;
                perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D, ItemID.TILE_STONE, ItemID.TILE_COAL_ORE, 1, 0.8, coalVerticalStretch, coalHorizontalStretch, SEA_LEVEL, tiles[0].length/3);
                generateOreVeins(tiles, perlin2D, ItemID.TILE_STONE, ItemID.TILE_COAL_ORE, 0.85, 0.85, coalVerticalStretch, coalHorizontalStretch, tiles[0].length/3, (2*tiles[0].length)/3);
                generateOreVeins(tiles, perlin2D, ItemID.TILE_STONE, ItemID.TILE_COAL_ORE, 0.85, 0.95, coalVerticalStretch, coalHorizontalStretch, (2*tiles[0].length)/3, tiles[0].length);

                //Iron Ore
                //Generates primarily in low area of the overworld. Doesn't generate at all in upper levels
                int ironVerticalStretch = 6;
                int ironHorizontalStretch = 2;
                perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  ItemID.TILE_STONE, ItemID.TILE_IRON_ORE, 0.925, 0.875, ironVerticalStretch, ironHorizontalStretch, tiles[0].length/2, tiles[0].length);

                /* Set these to generate in lower levels
                //Gold Ore
                perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  ItemID.TILE_STONE, ItemID.TILE_GOLD_ORE, 0.93, 0.91, 4, 4, SEA_LEVEL + 20, tiles[0].length);

                //Diamond Ore
                perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
                generateOreVeins(tiles, perlin2D,  ItemID.TILE_STONE, ItemID.TILE_DIAMOND_ORE, 0.975, 0.95, 3, 3, SEA_LEVEL + 32, tiles[0].length);
                 */
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

                final double STALACTITE_CHANCE = 0.1;
                final double STALAGMITE_CHANCE = 0.5;

                final double TREE_CHANCE = 0.1;

                final ArrayList<ItemID> stalactiteRocks = new ArrayList<>();
                stalactiteRocks.add(ItemID.TILE_STONE);
                stalactiteRocks.add(ItemID.TILE_COAL_ORE);
                stalactiteRocks.add(ItemID.TILE_IRON_ORE);
                stalactiteRocks.add(ItemID.TILE_GOLD_ORE);
                stalactiteRocks.add(ItemID.TILE_DIAMOND_ORE);

                for (int x = 0; x < tiles.length; x++) {
                    boolean hasStalactite = false;
                    for (int y = 0; y < tiles[x].length - 1; y++) {
                        if (stalactiteRocks.contains(tiles[x][y].itemID) && tiles[x][y + 1].itemID == ItemID.TILE_AIR) {
                            if (STALACTITE_CHANCE > Game.WORLD_RANDOM.nextDouble()) {
                                tiles[x][y + 1] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, (y + 1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_STALACTITE);
                                hasStalactite = true;
                            }
                        }
                        if (stalactiteRocks.contains(tiles[x][y].itemID) && tiles[x][y].itemID != ItemID.TILE_AIR && tiles[x][y - 1].itemID == ItemID.TILE_AIR && hasStalactite) {
                            if (STALAGMITE_CHANCE > Game.WORLD_RANDOM.nextDouble()) {
                                tiles[x][y - 1] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, (y - 1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_STALAGMITE);
                            }
                            hasStalactite = false;
                        }
                    }
                }

                for (int x = 5 ; x < tiles.length-5 ; x++) {
                    for (int y = 0 ; y < tiles[x].length ; y++) {
                        if(tiles[x][y].itemID == ItemID.TILE_DIRT_GRASS && y > 0 && tiles[x][y-1].itemID == ItemID.TILE_AIR){
                            tiles[x][y - 1] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, (y - 1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_TREE_STARTER);
                            for (int i = 0 ; i < (Game.WORLD_RANDOM.nextDouble() * 4) + 4 ; i++) {
                                tiles[x][y-1].onRandomUpdate(tiles, x, y-1, true);
                            }
                            x += 10;
                            break;
                        }
                    }
                }
                System.out.println("Overworld detail generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }

            /**
             *
             * @param tiles
             * @param perlin2D
             * @param targetTileID
             * @param oreTileID
             * @param startingThreshold
             * @param endingThreshold
             * @param verticalStretch
             * @param horizontalStretch
             * @param startingLevel
             * @param endingLevel
             */
            private void generateOreVeins(Tile[][] tiles, PerlinNoise.Perlin2D perlin2D, ItemID targetTileID, ItemID oreTileID, double startingThreshold, double endingThreshold, int verticalStretch, int horizontalStretch, int startingLevel, int endingLevel){
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = startingLevel; y < endingLevel; y++) {
                        double currentThreshold = startingThreshold * (1 - ((double) (y-startingLevel)/((endingLevel-startingLevel)-1))) + endingThreshold * ((double) (y-startingLevel)/((endingLevel-startingLevel)-1));
                        if(perlin2D.cosInterpolation((double) x/horizontalStretch, (double) y/verticalStretch) >= currentThreshold && (tiles[x][y].itemID == targetTileID || targetTileID == null)){
                            tiles[x][y] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, oreTileID);
                        }
                    }
                }
            }

            /**
             *
             * @param tiles
             * @param perlin2D
             * @param startingThreshold
             * @param endingThreshold
             * @param verticalStretch
             * @param horizontalStretch
             * @param startingLevel
             * @param endingLevel
             */
            private void generateCavitySection(Tile[][] tiles, PerlinNoise.Perlin2D perlin2D, double startingThreshold, double endingThreshold, int verticalStretch, int horizontalStretch, int startingLevel, int endingLevel){
                for (int x = 0; x < tiles.length; x++) {
                    for (int y = startingLevel; y < endingLevel; y++) {

                        double currentThreshold = startingThreshold * (1 - ((double) (y-startingLevel)/((endingLevel-startingLevel)-1))) + endingThreshold * ((double) (y-startingLevel)/((endingLevel-startingLevel)-1));

                        if(perlin2D.cosInterpolation((double) x/horizontalStretch, (double) y/verticalStretch) >= currentThreshold){
                            tiles[x][y] = Tile.TilePresets.getTilePreset(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, ItemID.TILE_AIR);
                        }
                    }
                }
            }
        }
    }
}
