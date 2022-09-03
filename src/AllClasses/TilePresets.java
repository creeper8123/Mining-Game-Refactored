package AllClasses;

import UniqueIDs.ItemID;

import java.awt.*;
import java.util.stream.IntStream;

/**
 *
 */
public class TilePresets {
    private static final int TREE_MAX_HEIGHT = 12;
    private static final ItemStack[] WORKBENCH_LV1_CRAFTING_OPTIONS = new ItemStack[]{
            new ItemStack(ItemID.TILE_FURNACE_L1, 1),
            new ItemStack(ItemID.TILE_CHEST_L1, 1)};
    private static final ItemStack[] FURNACE_LV1_CRAFTING_OPTIONS = new ItemStack[]{new ItemStack(ItemID.ITEM_PINE_CONE, 1)};

    public static Tile getTilePreset(int x, int y, int width, int height, ItemID tileID) {
        Tile finalTile;
        final double TREE_GROWTH_THRESHOLD = 0.995;
        switch (tileID) {
            case TILE_AIR -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                //finalTile.droppedItems = new ArrayList<>();
            }
            case TILE_STONE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_STONE, 1));
            }
            case TILE_DIRT_GRASS -> {
                final double KILL_GRASS_THRESHOLD = 0.5;
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_DIRT, 1));
            }
            case TILE_DIRT -> {
                final double GROW_GRASS_THRESHOLD = 0.5;
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_DIRT, 1));
            }
            case TILE_COAL_ORE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_COAL_ORE, 1));
            }
            case TILE_COPPER_ORE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_COPPER_ORE, 1));
            }
            case TILE_TIN_ORE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_TIN_ORE, 1));
            }
            case TILE_IRON_ORE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_IRON_ORE, 1));
            }
            case TILE_GOLD_ORE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_GOLD_ORE, 1));
            }
            case TILE_DIAMOND_ORE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_DIAMOND_ORE, 1));
            }
            case TILE_STALACTITE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
                    }

                    @Override
                    public void onTileUpdate(Tile[][] tiles, int x, int y) {
                        if (y > 0) {
                            if (tiles[x][y - 1].itemID == ItemID.TILE_AIR && this.itemID != ItemID.TILE_AIR) {
                                this.whenBroken(tiles, x, y, true);
                                Game.livingEntities.add(new FallingStalactite(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, this.hitbox.width, this.hitbox.height, ImageProcessing.resizeImage(ImageProcessing.removeNullPixels(this.texture), 4, 4)));
                            }
                        }
                    }

                    @Override
                    public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                        //Do nothing
                    }
                };
                finalTile.hasTransparency = true;
                finalTile.hitbox = new Rectangle(x + 20, y, width - 40, height - 16);
            }
            case TILE_STALAGMITE -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
                    }

                    @Override
                    public void onTileUpdate(Tile[][] tiles, int x, int y) {
                        if (y < tiles[0].length - 1) {
                            if (tiles[x][y + 1].itemID == ItemID.TILE_AIR) {
                                this.whenBroken(tiles, x, y, true);
                            }
                        }
                    }

                    @Override
                    public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                        //Do nothing
                    }
                };
                finalTile.hasTransparency = true;
                finalTile.hitbox = new Rectangle(x + 20, y + 16, width - 40, height - 16);
            }
            case TILE_LOG -> {
                final double LEAFY_LOGS_PER_NORMAL_LOG = 3;
                final double NEW_LEAF_THRESHOLD = 0.5;
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
                    }

                    @Override
                    public void onTileUpdate(Tile[][] tiles, int x, int y) {
                        if (y < tiles.length) {
                            if (!(tiles[x][y + 1].itemID == ItemID.TILE_LOG || tiles[x][y + 1].itemID == ItemID.TILE_LEAFY_LOG || tiles[x][y + 1].itemID == ItemID.TILE_DIRT || tiles[x][y + 1].itemID == ItemID.TILE_DIRT_GRASS)) {
                                this.whenBroken(tiles, x, y, true);
                            }
                        }
                    }

                    @Override
                    public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                        //TODO: Fix bug where trees get decapitated if the leaves of 2 trees intersect.

                        //If the log is on the ground, and is within range, and the random number is higher than the threshold, grow the tree.
                        if ((Game.TICK_RANDOM.nextDouble() > TREE_GROWTH_THRESHOLD || overrideRandomChance) && y < tiles.length - 1 && (tiles[x][y + 1].itemID == ItemID.TILE_DIRT || tiles[x][y + 1].itemID == ItemID.TILE_DIRT_GRASS)) {

                            int currentTreeHeight = TREE_MAX_HEIGHT;
                            double normalLogLength = 1 - (LEAFY_LOGS_PER_NORMAL_LOG / (LEAFY_LOGS_PER_NORMAL_LOG + 1));
                            boolean growthBlocked = false;
                            int newNumOfLeafyLogs = 0;
                            int oldNumOfLeafyLogs = 0;

                            //Determine the current height of the tree, and if growth is blocked.
                            for (int i = y - 1 ; i >= 0 && y - i < TREE_MAX_HEIGHT ; i--) {
                                if (tiles[x][i].itemID != ItemID.TILE_LOG && tiles[x][i].itemID != ItemID.TILE_LEAFY_LOG) {
                                    currentTreeHeight = ((y - 1) - (i - 1));
                                    if (tiles[x][i].itemID != ItemID.TILE_LOG && tiles[x][i].itemID != ItemID.TILE_LEAFY_LOG && tiles[x][i].itemID != ItemID.TILE_LEAVES && tiles[x][i].itemID != ItemID.TILE_AIR) {
                                        growthBlocked = true;
                                    }
                                    break;
                                }
                            }

                            //If the tree can still grow, continue growing.
                            if (!growthBlocked && currentTreeHeight < TREE_MAX_HEIGHT) {

                                //Determine the number of leafy logs before trunk growth
                                for (int i = currentTreeHeight ; i >= 0 ; i--) {
                                    if (tiles[x][y - i].itemID == ItemID.TILE_LEAFY_LOG) {
                                        oldNumOfLeafyLogs++;
                                    }
                                }

                                //Grow the trunk, adding leafy logs where necessary.
                                int leftLeafLength = 0;
                                int rightLeafLength = 0;
                                for (int i = currentTreeHeight ; i >= 0 ; i--) {
                                    if ((double) i / (currentTreeHeight + 1) >= normalLogLength) {
                                        if (tiles[x][y - i].itemID != ItemID.TILE_LEAFY_LOG && tiles[x][y - i].itemID != ItemID.TILE_LOG) {
                                            Tile.placeTile(x, y - i, ItemID.TILE_LEAFY_LOG, null, true);
                                        }
                                    } else {
                                        if (tiles[x][y - i].itemID != ItemID.TILE_LOG) {
                                            for (int j = 1 ; j < oldNumOfLeafyLogs + 1 ; j++) {
                                                if (x - j >= 0 && x - j < tiles.length && y - i >= 0 && y - i < tiles[x - i].length) {
                                                    if (tiles[x - j][y - i].itemID == ItemID.TILE_LEAVES) {
                                                        leftLeafLength++;
                                                    }
                                                    if (tiles[x + j][y - i].itemID == ItemID.TILE_LEAVES) {
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
                                    if (tiles[x][y - i].itemID == ItemID.TILE_LEAFY_LOG) {
                                        newNumOfLeafyLogs++;
                                    }
                                }

                                //Place leaves on top of the tree if space permits
                                if (y - (currentTreeHeight + 1) >= 0 && tiles[x][y - (currentTreeHeight + 1)].itemID == ItemID.TILE_AIR) {
                                    Tile.placeTile(x, y - (currentTreeHeight + 1), ItemID.TILE_LEAVES, null, false);
                                }

                                //Shift the existing leaves up one tile
                                for (int i = 0 ; i < newNumOfLeafyLogs + 1 ; i++) {
                                    for (int j = 1 ; j < i + 1 ; j++) {
                                        if (x - j >= 0 && tiles[x - j][(y - currentTreeHeight) + i].itemID == ItemID.TILE_LEAVES && tiles[x - j][((y - currentTreeHeight) + i) - 1].itemID == ItemID.TILE_AIR) {
                                            placeTile(x - j, ((y - currentTreeHeight) + i) - 1, ItemID.TILE_LEAVES, null, false);
                                        }
                                        if (x + j < tiles.length && tiles[x + j][(y - currentTreeHeight) + i].itemID == ItemID.TILE_LEAVES && tiles[x + j][((y - currentTreeHeight) + i) - 1].itemID == ItemID.TILE_AIR) {
                                            placeTile(x + j, ((y - currentTreeHeight) + i) - 1, ItemID.TILE_LEAVES, null, false);
                                        }
                                    }
                                }
                                if (leftLeafLength != 0 || rightLeafLength != 0) {
                                    int newLeafY = y - (currentTreeHeight - newNumOfLeafyLogs) - 1;
                                    for (int i = 1 ; i <= leftLeafLength ; i++) {
                                        if (tiles[x - i][newLeafY].itemID == ItemID.TILE_AIR) {
                                            Tile.placeTile(x - i, newLeafY, ItemID.TILE_LEAVES, null, false);
                                        }
                                    }
                                    for (int i = 1 ; i <= rightLeafLength ; i++) {
                                        if (tiles[x + i][newLeafY].itemID == ItemID.TILE_AIR) {
                                            Tile.placeTile(x + i, newLeafY, ItemID.TILE_LEAVES, null, false);
                                        }
                                    }
                                }

                                //Add a new layer of leaves if there is a new leafy tile.
                                if (oldNumOfLeafyLogs != newNumOfLeafyLogs) {
                                    int newLeafY = (y - (currentTreeHeight - newNumOfLeafyLogs)) - 1;
                                    //left
                                    if (Game.TICK_RANDOM.nextDouble() > NEW_LEAF_THRESHOLD) {
                                        int newLeafX = x;
                                        for (int i = 1 ; i <= newNumOfLeafyLogs ; i++) {
                                            if (x - i >= 0 && x - i < tiles.length && tiles[x - i][newLeafY].itemID == ItemID.TILE_AIR) {
                                                newLeafX = x - i;
                                                break;
                                            }
                                        }
                                        placeTile(newLeafX, newLeafY, ItemID.TILE_LEAVES, null, false);
                                    }
                                    //right
                                    if (Game.TICK_RANDOM.nextDouble() > NEW_LEAF_THRESHOLD) {
                                        int newLeafX = x;
                                        for (int i = 1 ; i <= newNumOfLeafyLogs ; i++) {
                                            if (tiles[x + i][newLeafY].itemID == ItemID.TILE_AIR) {
                                                newLeafX = x + i;
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_WOOD, 1));
            }
            case TILE_LEAVES -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                        double randomDrop = Game.TICK_RANDOM.nextDouble();
                        ItemID itemToDrop = randomDrop >= 0.66666666 ? ItemID.ITEM_STICK : randomDrop < 0.1 ? ItemID.ITEM_PINE_CONE : null;
                        if(itemToDrop == ItemID.ITEM_PINE_CONE){
                            this.droppedItemStacks.add(new ItemStack(ItemID.ITEM_PINE_CONE, 1));
                        }else if(itemToDrop == ItemID.ITEM_STICK){
                            this.droppedItemStacks.add(new ItemStack(ItemID.ITEM_STICK, (int) (Game.TICK_RANDOM.nextDouble()*3)+1));
                        }
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
                    }

                    @Override
                    public void onTileUpdate(Tile[][] tiles, int x, int y) {
                        //left
                        boolean killLeaf = true;
                        int newX = x;
                        while (tiles[newX][y].itemID == ItemID.TILE_LEAVES && killLeaf) {
                            newX--;
                            if (newX < 0) {
                                break;
                            }
                            if (tiles[newX][y].itemID == ItemID.TILE_LEAFY_LOG) {
                                killLeaf = false;
                            }
                        }

                        //right
                        newX = x;
                        while (tiles[newX][y].itemID == ItemID.TILE_LEAVES && killLeaf) {
                            newX++;
                            if (newX > tiles.length - 1) {
                                break;
                            }
                            if (tiles[newX][y].itemID == ItemID.TILE_LEAFY_LOG) {
                                killLeaf = false;
                            }
                        }
                        if (y < tiles[x].length - 1) {
                            if (tiles[x][y + 1].itemID == ItemID.TILE_LEAFY_LOG) {
                                killLeaf = false;
                            }
                        }
                        if (killLeaf) {
                            this.whenBroken(tiles, x, y, true);
                        }
                    }

                    @Override
                    public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                        //Do nothing
                    }
                };
                finalTile.hasTransparency = true;
            }
            case TILE_LEAFY_LOG -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                        double randomDrop = Game.TICK_RANDOM.nextDouble();
                        ItemID itemToDrop = randomDrop >= 0.66666666 ? ItemID.ITEM_STICK : randomDrop < 0.1 ? ItemID.ITEM_PINE_CONE : null;
                        if(itemToDrop == ItemID.ITEM_PINE_CONE){
                            this.droppedItemStacks.add(new ItemStack(ItemID.ITEM_PINE_CONE, 1));
                        }else if(itemToDrop == ItemID.ITEM_STICK){
                            this.droppedItemStacks.add(new ItemStack(ItemID.ITEM_STICK, (int) (Game.TICK_RANDOM.nextDouble()*3)+1));
                        }
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
                    }

                    @Override
                    public void onTileUpdate(Tile[][] tiles, int x, int y) {
                        if (y < tiles.length) {
                            if (!(tiles[x][y + 1].itemID == ItemID.TILE_LOG || tiles[x][y + 1].itemID == ItemID.TILE_LEAFY_LOG || tiles[x][y + 1].itemID == ItemID.TILE_DIRT || tiles[x][y + 1].itemID == ItemID.TILE_DIRT_GRASS)) {
                                this.whenBroken(tiles, x, y, true);
                            }
                        }
                    }

                    @Override
                    public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                        if (y < tiles[x].length && y > 0 && (tiles[x][y + 1].itemID == ItemID.TILE_DIRT || tiles[x][y + 1].itemID == ItemID.TILE_DIRT_GRASS) && (tiles[x][y - 1].itemID == ItemID.TILE_AIR || tiles[x][y - 1].itemID == ItemID.TILE_LEAVES) && (Game.TICK_RANDOM.nextDouble() > TREE_GROWTH_THRESHOLD || overrideRandomChance)) {
                            Tile.placeTile(x, y, ItemID.TILE_LOG, null, true);
                            Tile.placeTile(x, y - 1, ItemID.TILE_LEAFY_LOG, null, true);
                            if (y > 1 && tiles[x][y - 2].itemID == ItemID.TILE_AIR) {
                                Tile.placeTile(x, y - 2, ItemID.TILE_LEAVES, null, true);
                            }
                            if (x - 1 >= 0 && tiles[x - 1][y - 1].itemID == ItemID.TILE_AIR) {
                                Tile.placeTile(x - 1, y - 1, ItemID.TILE_LEAVES, null, true);
                            }
                            if (x + 1 < tiles.length && tiles[x + 1][y - 1].itemID == ItemID.TILE_AIR) {
                                Tile.placeTile(x + 1, y - 1, ItemID.TILE_LEAVES, null, true);
                            }
                        }
                    }
                };
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_WOOD, 1));
            }
            case TILE_TREE_STARTER -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                        double randomDrop = Game.TICK_RANDOM.nextDouble();
                        ItemID itemToDrop = randomDrop >= 0.66666666 ? ItemID.ITEM_STICK : randomDrop < 0.1 ? ItemID.ITEM_PINE_CONE : null;
                        if(itemToDrop == ItemID.ITEM_PINE_CONE){
                            this.droppedItemStacks.add(new ItemStack(ItemID.ITEM_PINE_CONE, 1));
                        }else if(itemToDrop == ItemID.ITEM_STICK){
                            this.droppedItemStacks.add(new ItemStack(ItemID.ITEM_STICK, (int) (Game.TICK_RANDOM.nextDouble()*3)+1));
                        }
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
                    }

                    @Override
                    public void onTileUpdate(Tile[][] tiles, int x, int y) {
                        //Do nothing
                    }

                    @Override
                    public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                        if (Game.TICK_RANDOM.nextDouble() > TREE_GROWTH_THRESHOLD || overrideRandomChance) {
                            Tile.placeTile(x, y, ItemID.TILE_LEAFY_LOG, null, true);
                            if (y > 0 && tiles[x][y - 1].itemID == ItemID.TILE_AIR) {
                                Tile.placeTile(x, y - 1, ItemID.TILE_LEAVES, null, true);
                            }
                        }
                    }
                };
                //finalTile.droppedItems = new ArrayList<>();
            }
            case TILE_WOOD -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_WOOD, 1));
            }
            case TILE_WORKBENCH_L1 -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        InventoryManager.displayCraftingMenuInAltMenu("---Crafting (" + this.generateDisplayName(itemID) + ")---", WORKBENCH_LV1_CRAFTING_OPTIONS);
                        return true;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.hasTransparency = true;
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_WORKBENCH_L1, 1));
                finalTile.hitbox.y += TileGraphics.TILE_HEIGHT / 2;
                finalTile.hitbox.height /= 2;
            }
            case TILE_FURNACE_L1 -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        InventoryManager.displayCraftingMenuInAltMenu("---Crafting (" + this.generateDisplayName(itemID) + ")---", FURNACE_LV1_CRAFTING_OPTIONS);
                        if (y > 0 && tiles[x][y - 1].itemID == ItemID.TILE_AIR) {
                            for (int i = 0 ; i < 5 ; i++) {
                                Game.livingParticles.add(new Particle(((x + 1) * TileGraphics.TILE_WIDTH) - 16, (y * TileGraphics.TILE_HEIGHT) - 16, 0, 16, 16, ImageProcessing.removeNullPixels(ImageProcessing.resizeImage(ImageProcessing.rotateImage(ImageProcessing.getImageFromResources("textures/moving_objects/particles/smoke.png"), (int) (Game.MOVING_OBJECT_RANDOM.nextDouble() * 4)), 4, 4)), (Game.MOVING_OBJECT_RANDOM.nextDouble() * 2) - 1, -Game.MOVING_OBJECT_RANDOM.nextDouble(), true, (Game.MOVING_OBJECT_RANDOM.nextDouble() * 3) + 1, true, -0.025, true));
                            }
                        }
                        return true;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
                    }

                    @Override
                    public void onTileUpdate(Tile[][] tiles, int x, int y) {
                        //Do nothing
                    }

                    @Override
                    public void onRandomUpdate(Tile[][] tiles, int x, int y, boolean overrideRandomChance) {
                        if (y > 0 && tiles[x][y - 1].itemID == ItemID.TILE_AIR) {
                            Game.livingParticles.add(new Particle(((x + 1) * TileGraphics.TILE_WIDTH) - 16, (y * TileGraphics.TILE_HEIGHT) - 16, 0, 16, 16, ImageProcessing.removeNullPixels(ImageProcessing.resizeImage(ImageProcessing.rotateImage(ImageProcessing.getImageFromResources("textures/moving_objects/particles/smoke.png"), (int) (Game.MOVING_OBJECT_RANDOM.nextDouble() * 4)), 4, 4)), (Game.MOVING_OBJECT_RANDOM.nextDouble() * 2) - 1, -Game.MOVING_OBJECT_RANDOM.nextDouble(), true, (Game.MOVING_OBJECT_RANDOM.nextDouble() * 3) + 1, true, -0.025, true));
                        }
                    }
                };
                finalTile.hasTransparency = true;
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_FURNACE_L1, 1));
            }
            case TILE_CHEST_L1 -> {
                finalTile = new Tile(x, y, width, height, tileID) {
                    final InventoryManager chestInventory = new InventoryManager(5);

                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        InventoryManager.displayStorageInventoryInAltMenu("---Storage (" + this.generateDisplayName(itemID) + ")---", chestInventory);
                        return true;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        for (int i = 0 ; i < this.chestInventory.inventory.length; i++) {
                            if(chestInventory.getInventorySlot(i).holdableObject != null && chestInventory.getInventorySlot(i).quantity > 0){
                                this.droppedItemStacks.add(new ItemStack(chestInventory.getInventorySlot(i).holdableObject.itemID, chestInventory.getInventorySlot(i).quantity));
                            }
                        }
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(ItemID.TILE_CHEST_L1, 1));
            }
            default -> {
                System.err.println("tileID " + tileID + " has no defined tile preset, using default tile preset.");
                finalTile = new Tile(x, y, width, height, tileID) {
                    @Override
                    public boolean whenUsed(Tile[][] tiles, int x, int y) {
                        //Do nothing
                        return false;
                    }

                    @Override
                    public void whenPlaced(Tile[][] tiles, int x, int y, MovingObject placedByEntity) {
                        super.whenPlaced(tiles, x, y, placedByEntity);
                    }

                    @Override
                    public void whenBroken(Tile[][] tiles, int x, int y, boolean dropItems) {
                        super.whenBroken(tiles, x, y, dropItems);
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
                finalTile.droppedItemStacks.add(new ItemStack(finalTile.itemID, 1));
            }
        }
        return finalTile;
    }
}
