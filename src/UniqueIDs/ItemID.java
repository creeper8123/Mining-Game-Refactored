package UniqueIDs;

/**
 * Item IDs for unique items.
 */

/*
When adding a new UniqueIDs.ItemID, needs to update:
AllClasses.HoldableObject.generateTexture(itemID);                     //Only if object can be placed
AllClasses.HoldableObject.generateDisplayName(itemID);                 //Only if item is intended to be held.
AllClasses.HoldableObject.getCraftingRecipe(itemID);                   //Only if item can be crafted.
AllClasses.HoldableObject.whenUsed(int x, int y, AllClasses.MovingObject usedBy); //Only if the item has a use or can be placed (IE is not an intermediate crafting item)
AllClasses.InventoryManager.getStackSize(itemID);                      //Only if item is intended to be held.
AllClasses.TilePresets.getTilePreset();                     //For tiles only
AllClasses.TileGeneration.xyzGeneration();                             //For tiles only, and only if naturally occurring.
*/
public enum ItemID {
    /**The ID for an Air tile.*/TILE_AIR,
    /**The ID for a Dirt tile.*/TILE_DIRT,
    /**The ID for a Dirt tile with grass.*/TILE_DIRT_GRASS,
    /**The ID for a Stone tile.*/TILE_STONE,
    /**The ID for a Stone tile with embedded coal ore.*/TILE_COAL_ORE,
    /**The ID for a Stone tile with embedded coal ore.*/TILE_COPPER_ORE,
    /**The ID for a Stone tile with embedded coal ore.*/TILE_TIN_ORE,
    /**The ID for a Stone tile with embedded iron ore.*/TILE_IRON_ORE,
    /**The ID for a Stone tile with embedded gold ore.*/TILE_GOLD_ORE,
    /**The ID for a Stone tile with embedded diamond ore.*/TILE_DIAMOND_ORE,
    /**The ID for a Stalagmite tile.*/TILE_STALAGMITE, //The floor one
    /**The ID for a Stalactite tile.*/TILE_STALACTITE,  //The ceiling one, I hate that I know this by memory now.
    /***/ TILE_LOG,
    /***/ TILE_LEAVES,
    /***/ TILE_LEAFY_LOG,
    TILE_WOOD,
    ITEM_PINE_CONE,
    TILE_TREE_STARTER,
    ITEM_STICK,
    TILE_WORKBENCH_L1,
    TILE_FURNACE_L1,
    TILE_CHEST_L1
}