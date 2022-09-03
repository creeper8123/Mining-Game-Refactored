package AllClasses;

import UniqueIDs.GameState;
import UniqueIDs.ItemID;

import java.awt.*;
import java.util.ArrayList;

class Tile extends HoldableObject {

    /***/
    public Rectangle hitbox;
    /***/
    public boolean canBeBroken;
    /***/
    public ArrayList<ItemStack> droppedItemStacks = new ArrayList<>();

    public Tile(int x, int y, int width, int height, ItemID itemID) {
        super(itemID);
        hitbox = new Rectangle(x, y, width, height);
        canBeBroken = itemID != ItemID.TILE_AIR;
        hasTransparency = itemID == ItemID.TILE_STALACTITE || itemID == ItemID.TILE_STALAGMITE;
    }

    public static boolean placeTile(int x, int y, ItemID itemID, MovingObject placedByEntity, boolean forcePlace) {
        if (forcePlace) {
            Game.tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, itemID);
            Game.tiles[x][y].whenPlaced(Game.tiles, x, y, placedByEntity);
            return true;
        }
        if (Game.tiles[x][y].itemID != ItemID.TILE_AIR) {
            return false;
        }
        boolean foundFoothold = false;
        if (x > 0) {
            if (Game.tiles[x - 1][y].itemID != ItemID.TILE_AIR && !foundFoothold) {
                foundFoothold = true;
            }
        }
        if (x < Game.tiles.length - 1 && !foundFoothold) {
            if (Game.tiles[x + 1][y].itemID != ItemID.TILE_AIR) {
                foundFoothold = true;
            }
        }
        if (y > 0 && !foundFoothold) {
            if (Game.tiles[x][y - 1].itemID != ItemID.TILE_AIR) {
                foundFoothold = true;
            }
        }
        if (y < Game.tiles[x].length - 1 && !foundFoothold) {
            if (Game.tiles[x][y + 1].itemID != ItemID.TILE_AIR) {
                foundFoothold = true;
            }
        }
        if (Game.backgroundTiles[x][y].itemID != ItemID.TILE_AIR && !foundFoothold) {
            foundFoothold = true;
        }
        if (foundFoothold) {
            Game.tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, itemID);
            Game.tiles[x][y].whenPlaced(Game.tiles, x, y, placedByEntity);
            return true;
        }
        return false;
    }


    public void generateTileTextures() {
        texture = generateTexture(itemID);
    }

    public boolean whenUsed(Tile[][] tiles, int x, int y) {
        System.out.println("WARNING! Nothing Specified for " + this.itemID + " whenUsed at x" + x + ", y" + y);
        return false;
    }


    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
        if (placedByEntity != null) {
            placedByEntity.inventory.removeFromInventory(tiles[x][y], 1);
        }
        triggerUpdate(tiles, x, y);
        tiles[x][y].generateTileTextures();
        if (Game.chunks != null && Game.chunks[x] != null) {
            Game.chunks[x].yValues.add(y);
        }
    }


    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
        if (tiles[x][y].itemID != ItemID.TILE_AIR) {
            Game.tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_AIR);
            triggerUpdate(tiles, x, y);
            if (Game.chunks != null && Game.chunks[x] != null) {
                Game.chunks[x].yValues.add(y);
            }
        }
        if(dropItems && (Game.currentGameState == GameState.CHUNK_GENERATION || Game.currentGameState == GameState.NORMAL_GAMEPLAY)){
            for (ItemStack droppedItemStack : droppedItemStacks) {
                Game.livingDroppedItems.add(new DroppedItem(((x * TileGraphics.TILE_WIDTH) + (double) (TileGraphics.TILE_WIDTH/2))-8, ((y * TileGraphics.TILE_HEIGHT) + (double) (TileGraphics.TILE_HEIGHT/2))-8, 0, 16, 16, this.generateTexture(droppedItemStack.holdableObject.itemID), droppedItemStack.holdableObject.itemID, droppedItemStack.quantity));
            }
        }
    }


    public void onTileUpdate(Tile[][] tiles, int x, int y) {
        System.out.println("WARNING! Nothing Specified for " + this.itemID + " onTileUpdate at x" + x + ", y" + y);
    }


    public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
        System.out.println("WARNING! Nothing Specified for " + this.itemID + " onRandomUpdate at x" + x + ", y" + y);
    }

    private void triggerUpdate(Tile[][] tiles, int x, int y) {
        if (x > 0) {
            tiles[x - 1][y].onTileUpdate(tiles, x - 1, y);
        }
        if (x < tiles.length - 1) {
            tiles[x + 1][y].onTileUpdate(tiles, x + 1, y);
        }
        if (y > 0) {
            tiles[x][y - 1].onTileUpdate(tiles, x, y - 1);
        }
        if (y < tiles[0].length - 1) {
            tiles[x][y + 1].onTileUpdate(tiles, x, y + 1);
        }
    }

}
