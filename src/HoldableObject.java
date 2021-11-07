import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A HoldableObject is an object that does not move, but can be stored in an inventory, used in crafting, or be interacted with in some other way.
 */
public class HoldableObject {
    /***/String displayName;
    /**The unscaled texture of the object.*/BufferedImage texture;
    /**The ItemID that identifies the object.*/ItemID itemID;
    /**A flag to render the texture while replacing the ImageProcessing.NULL_COLOR colours of the textures with full transparency. Note: The removal must be the final step, several Image Processing functions will reset the alpha value.*/boolean hasTransparency = false;


    public HoldableObject(ItemID itemID, String displayName){
        this.itemID = itemID;
        this.displayName = displayName;
    }

    public HoldableObject(ItemID itemID){
        this.itemID = itemID;
        this.displayName = generateDisplayName(itemID);
    }

    /**
     * Generates a texture for each unique ItemID, defaults to a null texture if the texture file or the ItemID cannot be found&#46; Stores the texture in the objects 'texture' variable.
     */
    public void generateTexture(){
        texture = generateTexture(this.itemID);
    }

    /**
     * Generates a texture for each unique ItemID, defaults to a null texture if the texture file or the ItemID cannot be found.
     * @param itemID The ItemID to generate a texture for.
     * @return Returns a 16x16 BufferedImage of the texture.
     */
    public BufferedImage generateTexture(ItemID itemID){
        BufferedImage finalImage;
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
                        a = ImageProcessing.SKY_COLOR.getAlpha();
                        r = ImageProcessing.SKY_COLOR.getRed();
                        g = ImageProcessing.SKY_COLOR.getGreen();
                        b = ImageProcessing.SKY_COLOR.getBlue();
                        col = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                        newImage.setRGB(x, y, col);
                    }
                }
                finalImage = newImage;
            }
            case TILE_DIRT -> {
                final ArrayList<String> textureRandom = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    textureRandom.add("textures/tiles/fullTiles/dirt/dirt" + i + ".png");
                }

                BufferedImage baseTexture = ImageProcessing.getImageFromResources(textureRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * textureRandom.size())));
                finalImage = ImageProcessing.bufferedImageToImage(ImageProcessing.rotateImage(baseTexture, (int) (Game.WORLD_RANDOM.nextDouble() * 4)));
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

                BufferedImage baseDirtTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(dirtRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * dirtRandom.size()))) ,(int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseGrassTexture = ImageProcessing.getImageFromResources(grassRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * grassRandom.size())));
                finalImage = ImageProcessing.overlayImage(baseDirtTexture, baseGrassTexture);

            }
            //TODO: Replace stone textures with a less noisy, smoother variant
            case TILE_STONE -> {
                final ArrayList<String> textureRandom = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    textureRandom.add("textures/tiles/fullTiles/stone/stone" + i + ".png");
                }

                BufferedImage baseTexture = ImageProcessing.getImageFromResources(textureRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * textureRandom.size())));
                finalImage = ImageProcessing.rotateImage(baseTexture, (int) (Game.WORLD_RANDOM.nextDouble() * 4));
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

                BufferedImage baseStoneTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(stoneRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseCoalTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(coalRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * coalRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                finalImage = ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
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

                BufferedImage baseStoneTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(stoneRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseCoalTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(ironRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * ironRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                finalImage = ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
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

                BufferedImage baseStoneTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(stoneRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseCoalTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(goldRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * goldRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                finalImage = ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
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

                BufferedImage baseStoneTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(stoneRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * stoneRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                BufferedImage baseCoalTexture = ImageProcessing.rotateImage(ImageProcessing.getImageFromResources(diamondRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * diamondRandom.size()))), (int) (Game.WORLD_RANDOM.nextDouble() * 4));
                finalImage = ImageProcessing.overlayImage(baseStoneTexture, baseCoalTexture);
            }
            case TILE_STALACTITE -> {
                final ArrayList<String> textureRandom = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    textureRandom.add("textures/tiles/fullTiles/stalactite/stalactite" + i + ".png");
                }
                finalImage = ImageProcessing.getImageFromResources(textureRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * textureRandom.size())));
            }
            case TILE_STALAGMITE -> {
                final ArrayList<String> textureRandom = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    textureRandom.add("textures/tiles/fullTiles/stalagmite/stalagmite" + i + ".png");
                }
                finalImage = ImageProcessing.getImageFromResources(textureRandom.get((int) (Game.WORLD_RANDOM.nextDouble() * textureRandom.size())));
            }
            default -> finalImage = ImageProcessing.getImageFromResources("textures/missingTexture.png");
        }
        return finalImage;
    }

    public String generateDisplayName(ItemID itemID){
        if(itemID == null){
            return "Empty";
        }
        switch(itemID){
            case TILE_AIR -> {
                return "Air";
            }
            case TILE_DIRT -> {
                return "Dirt";
            }
            case TILE_DIRT_GRASS -> {
                return "Grassy Dirt";
            }
            case TILE_STONE -> {
                return "Stone";
            }
            case TILE_COAL_ORE -> {
                return "Coal Ore";
            }
            case TILE_IRON_ORE -> {
                return "Iron Ore";
            }
            case TILE_GOLD_ORE -> {
                return "Gold Ore";
            }
            case TILE_DIAMOND_ORE -> {
                return "Diamond Ore";
            }
            case TILE_STALAGMITE -> {
                return "Stalagmite";
            }
            case TILE_STALACTITE -> {
                return "Stalactite";
            }
            default -> {
                return itemID.toString();
            }
        }
    }
}
