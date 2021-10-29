import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class NonMovingObject {
    BufferedImage texture;
    ItemID itemID;


    public BufferedImage generateTexture(ItemID itemID){
        switch(itemID){
            case TILE_AIR -> {
                BufferedImage newImage = ImageProcessing.bufferedImageToImage(new BufferedImage(Tiles.TILE_BASE_WIDTH, Tiles.TILE_BASE_HEIGHT, 6));
                int a;// red component 0...255
                int r;// = 255;// red component 0...255
                int g;// = 0;// green component 0...255
                int b;// = 0;// blue component 0...255
                int col;// = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                for (int x = 0; x < Tiles.TILE_BASE_WIDTH; x++) {
                    for (int y = 0; y < Tiles.TILE_BASE_HEIGHT; y++) {
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

                BufferedImage baseTexture = ImageProcessing.getImageFromResources(textureRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * textureRandom.size())));
                return ImageProcessing.bufferedImageToImage(ImageProcessing.rotateImageRandomly(baseTexture, (int) (Game.WORLD_RANDOM.nextDouble() * 4)));
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

                BufferedImage baseDirtTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(dirtRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * dirtRandom.size()))) ,(int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseGrassTexture = ImageProcessing.getImageFromResources(grassRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * grassRandom.size())));
                return ImageProcessing.overlayImage(baseDirtTexture, baseGrassTexture);

            }
            case TILE_STONE -> {
                final ArrayList<String> textureRandom = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    textureRandom.add("textures/tiles/fullTiles/stone/stone" + i + ".png");
                }

                BufferedImage baseTexture = ImageProcessing.getImageFromResources(textureRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * textureRandom.size())));
                return ImageProcessing.rotateImageRandomly(baseTexture, (int) (Game.WORLD_RANDOM.nextDouble() * 4));
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

                BufferedImage baseStoneTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(stoneRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseCoalTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(coalRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * coalRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
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

                BufferedImage baseStoneTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(stoneRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseCoalTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(ironRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * ironRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
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

                BufferedImage baseStoneTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(stoneRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseCoalTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(goldRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * goldRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
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

                BufferedImage baseStoneTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(stoneRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseCoalTexture = ImageProcessing.rotateImageRandomly(ImageProcessing.getImageFromResources(diamondRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * diamondRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                return ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
            }
            case TILE_STALACTITE -> {
                final ArrayList<String> textureRandom = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    textureRandom.add("textures/tiles/fullTiles/stalactite/stalactite" + i + ".png");
                }
                return ImageProcessing.getImageFromResources(textureRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * textureRandom.size())));
            }
            case TILE_STALAGMITE -> {
                final ArrayList<String> textureRandom = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    textureRandom.add("textures/tiles/fullTiles/stalagmite/stalagmite" + i + ".png");
                }
                return ImageProcessing.getImageFromResources(textureRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * textureRandom.size())));
            }
            default -> {
                return ImageProcessing.getImageFromResources("textures/missingTexture.png");
            }
        }
    }
}
