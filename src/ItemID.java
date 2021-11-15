/**
 * Item IDs for unique items.
 */

/*
When adding a new ItemID, needs to update:
HoldableObject.generateTexture();
HoldableObject.generateDisplayName();
InventoryManager.getStackSize();
Tiles.Tile.TilePresets.getTilePreset(); //For tiles only
Tiles.WorldGeneration(); //For tiles only, and only if naturally occurring.
 */
public enum ItemID {
    /**The ID for an Air tile.*/TILE_AIR,
    /**The ID for a Dirt tile.*/TILE_DIRT,
    /**The ID for a Dirt tile with grass.*/TILE_DIRT_GRASS,
    /**The ID for a Stone tile.*/TILE_STONE,
    /**The ID for a Stone tile with embedded coal ore.*/TILE_COAL_ORE,
    /**The ID for a Stone tile with embedded iron ore.*/TILE_IRON_ORE,
    /**The ID for a Stone tile with embedded gold ore.*/TILE_GOLD_ORE,
    /**The ID for a Stone tile with embedded diamond ore.*/TILE_DIAMOND_ORE,
    /**The ID for a Stalagmite tile.*/TILE_STALAGMITE, //The floor one
    /**The ID for a Stalactite tile.*/TILE_STALACTITE,  //The ceiling one, I hate that I know this by memory now.
    /***/ TILE_LOG,
    /***/ TILE_LEAVES,
    /***/ TILE_LEAFY_LOG,
    ITEM_PINE_CONE,
    TILE_TREE_STARTER
}