package AllClasses;

import UniqueIDs.ItemID;

/**
 * A stack of a single type of UniqueIDs.ItemID, containing an UniqueIDs.ItemID and its current quantity.
 */
//TODO: Move this into it's own class outside of AllClasses.InventoryManager.
public class ItemStack {
    /**
     * The UniqueIDs.ItemID currently in the slot.
     */
    public HoldableObject holdableObject;
    /**
     * The number of items currently in the slot.
     */
    public int quantity;

    /**
     * Initializes the UniqueIDs.ItemID with null, and the Quantity with null.
     */
    public ItemStack() {
        holdableObject = null;
        quantity = 0;
    }

    public ItemStack(ItemID itemID, int quantity) {
        holdableObject = new HoldableObject(itemID);
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        String itemID;
        if (holdableObject == null || holdableObject.itemID == null) {
            itemID = "null";
        } else {
            itemID = holdableObject.itemID.toString();
        }

        return "[" + itemID + " *" + quantity + "]";
    }
}
