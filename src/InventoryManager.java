import java.util.ArrayList;
import java.util.Arrays;

/**
 * InventoryManager creates an inventory consisting of slots, themselves consisting of an ItemID as an identifier, and a quantity. It also has several methods for interacting with the inventory.
 */
public class InventoryManager {

    /**
     * Defines and returns the stack size of each item in ItemID.java, returning -1 if the item is not in the list.
     * @param itemID The ItemID to get the stack size for.
     * @return The stack size of the input ItemID.
     */
    protected static int getStackSize(ItemID itemID){
        switch(itemID){
            case TILE_AIR -> {
                return 5;
            }
            case TILE_DIRT -> {
                return 100;
            }
            case TILE_DIRT_GRASS -> {
                return 100;
            }
            case TILE_STONE -> {
                return 250;
            }
            case TILE_COAL_ORE -> {
                return 50;
            }
            case TILE_IRON_ORE -> {
                return 50;
            }
            case TILE_GOLD_ORE -> {
                return 25;
            }
            case TILE_DIAMOND_ORE -> {
                return 25;
            }
            case TILE_STALAGMITE -> {
                return 50;
            }
            case TILE_STALACTITE -> {
                return 50;
            }
        }
        return -1;
    }

    /**
     * A stack of a single type of ItemID, containing an ItemID and its current quantity.
     */
    public static class ItemStack {
        /**The ItemID currently in the slot.*/ public HoldableObject holdableObject;
        /**The number of items currently in the slot.*/public int quantity;

        /**
         * Initializes the ItemID with null, and the Quantity with null.
         */
        public ItemStack(){
            holdableObject = null;
            quantity = 0;
        }

        @Override
        public String toString(){
            String itemID;
            if(holdableObject == null || holdableObject.itemID == null){
                itemID = "null";
            }else{
                itemID = holdableObject.itemID.toString();
            }

            return "[" + itemID + " *" + quantity + "]";
        }
    }

    /**A list of ItemStack objects.*/public ItemStack[] inventory;

    /**
     * Creates a list of ItemStack to a set length. If the length is less than 0, it is reset to 0.
     * @param length The amount of ItemStack objects in the inventory.
     */
    public InventoryManager(int length){
        length = Math.max(length, 0);
        inventory = new ItemStack[length];
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new ItemStack();
        }
    }

    /**
     * Attempts to add an item to the inventory, first adding it to existing items of the same ItemID, then to empty slots if there are still items remaining.
     * @param holdableObject The HoldableObject to be added to the inventory.
     * @param quantity The amount of the ItemID to be added.
     * @return Returns the number of items that cannot be allocated due to the inventory being full.
     */
    public int addToInventory(HoldableObject holdableObject, int quantity){
        assert quantity > 0;
        if(holdableObject == null || holdableObject.itemID == null){
            return quantity;
        }
        ArrayList<Integer> itemFoundAt = new ArrayList<>();
        ArrayList<Integer> emptySlot = new ArrayList<>();
        for (int i = 0; i < inventory.length; i++) {
            if(inventory[i].holdableObject == null){
                emptySlot.add(i);
            }else if(inventory[i].holdableObject.itemID == holdableObject.itemID){
                itemFoundAt.add(i);
            }
        }
        final int STACK_SIZE = getStackSize(holdableObject.itemID);
        for (int i = 0; i < itemFoundAt.size(); i++) {
            int quantityAtStack = inventory[itemFoundAt.get(i)].quantity;
            if(quantityAtStack < STACK_SIZE){
                if(quantityAtStack + quantity > STACK_SIZE){
                    quantity = quantity - (STACK_SIZE - quantityAtStack);
                    inventory[itemFoundAt.get(i)].quantity = STACK_SIZE;
                }else if(quantityAtStack + quantity <= STACK_SIZE){
                    inventory[itemFoundAt.get(i)].quantity += quantity;
                    quantity = 0;
                    resetEmptySlots();
                    return quantity;
                }
            }
        }
        for (int i = 0; i < emptySlot.size(); i++) {
            if(quantity > STACK_SIZE){
                inventory[emptySlot.get(i)].holdableObject = holdableObject;
                inventory[emptySlot.get(i)].quantity = STACK_SIZE;
                quantity -= STACK_SIZE;
            }else{
                inventory[emptySlot.get(i)].holdableObject = holdableObject;
                inventory[emptySlot.get(i)].quantity = quantity;
                resetEmptySlots();
                return 0;
            }
        }
        resetEmptySlots();
        return quantity;
    }

    /**
     * Attempts to add items to a specific index of the inventory.
     * @param holdableObject The HoldableObject of the items to be added.
     * @param quantity The amount of Items to add to the slot.
     * @param i The index of the desired slot.
     * @return Returns the number of items that cannot be allocated due to the inventory slot being full.
     */
    public int addToInventory(HoldableObject holdableObject, int quantity, int i){
        assert quantity > 0;
        assert i >= 0 && i < inventory.length;
        if(holdableObject == null || holdableObject.itemID == null){
            return quantity;
        }
        final int STACK_SIZE = getStackSize(holdableObject.itemID);
        if (inventory[i].holdableObject == null){
            inventory[i].holdableObject = holdableObject;
            inventory[i].quantity = 0;
        }else{
            if(inventory[i].holdableObject.itemID != holdableObject.itemID){
                resetEmptySlots();
                return quantity;
            }
        }
        if(inventory[i].quantity == STACK_SIZE){
            resetEmptySlots();
            return quantity;
        }
        if(inventory[i].quantity + quantity > STACK_SIZE){
            quantity = quantity - (STACK_SIZE - inventory[i].quantity);
            inventory[i].quantity = STACK_SIZE;
        }else if(inventory[i].quantity + quantity <= STACK_SIZE){
            inventory[i].quantity += quantity;
            quantity = 0;
        }
        inventory[i].holdableObject = holdableObject;
        resetEmptySlots();
        return quantity;
    }

    /**
     * Attempts to remove items from the inventory, starting from the end of the inventory working backwards.
     * @param holdableObject The HoldableObject to attempt to remove from the inventory.
     * @param quantity The number of items to attempt to remove from the inventory.
     * @return Returns the number of unallocated items that could not be removed because none are in the inventory.
     */
    public int removeFromInventory(HoldableObject holdableObject, int quantity){
        assert quantity > 0;
        if(holdableObject == null || holdableObject.itemID == null){
            return quantity;
        }
        ArrayList<Integer> itemFoundAt = new ArrayList<>();
        for (int x = inventory.length - 1; x >= 0; x--) {
            if(inventory[x].holdableObject != null){
                if(inventory[x].holdableObject.itemID == holdableObject.itemID){
                    itemFoundAt.add(x);
                }
            }
        }
        for (int i = 0; i < itemFoundAt.size(); i++) {
            if(quantity > 0){
                if(quantity - inventory[itemFoundAt.get(i)].quantity > 0){
                    quantity -= inventory[itemFoundAt.get(i)].quantity;
                    inventory[itemFoundAt.get(i)].quantity = 0;
                }else{
                    inventory[itemFoundAt.get(i)].quantity -= quantity;
                    quantity = 0;
                    resetEmptySlots();
                    return quantity;
                }
            }
        }
        resetEmptySlots();
        return quantity;
    }

    /**
     * Attempts to remove items from the inventory at a specified index.
     * @param holdableObject The HoldableObject to remove from the slot (Used for conformation).
     * @param quantity The amount of items to remove from the slot.
     * @param i The index to remove items from.
     * @return Returns the number of unallocated items that could not be removed because none are in the designated inventory slot.
     */
    public int removeFromInventory(HoldableObject holdableObject, int quantity, int i){
        assert quantity > 0;
        assert i >= 0 && i < inventory.length;
        if(holdableObject == null || holdableObject.itemID == null){
            return quantity;
        }
        if (inventory[i].holdableObject == null){
            resetEmptySlots();
            return quantity;
        }
        if(inventory[i].holdableObject.itemID != holdableObject.itemID){
            resetEmptySlots();
            return quantity;
        }
        if(inventory[i].quantity == 0){
            resetEmptySlots();
            return quantity;
        }
        if(quantity > inventory[i].quantity){
            quantity -= inventory[i].quantity;
            inventory[i].quantity = 0;
        }else{
            inventory[i].quantity -= quantity;
            quantity = 0;
        }
        resetEmptySlots();
        return quantity;
    }

    /**
     * Gets the total quantity of a ItemID currently in the inventory.
     * @param holdableObject The HoldableObject to search for in the inventory.
     * @return Returns the number of items of a certain ItemID currently in the inventory.
     */
    public int getQuantityInInventory(HoldableObject holdableObject){
        if(holdableObject == null || holdableObject.itemID == null){
            return -1;
        }
        int quantity = 0;
        for (int i = 0; i < inventory.length; i++) {
            if(inventory[i].holdableObject != null && inventory[i].holdableObject.itemID == holdableObject.itemID){
                quantity += inventory[i].quantity;
            }
        }
        return quantity;
    }

    /**
     * Checks the inventory to see if a certain ItemID is present.
     * @param holdableObject The HoldableObject to search for in the inventory.
     * @return Returns true if the ItemID is found, returns false otherwise.
     */
    public boolean hasItemType(HoldableObject holdableObject){
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].holdableObject != null && inventory[i].holdableObject.itemID == holdableObject.itemID){
                return true;
            }
        }
        return false;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    /**
     * Gets the ItemStack at a certain index.
     * @param i The index to retrieve from.
     * @return The ItemStack at the index.
     */
    public ItemStack getInventorySlot(int i){
        return inventory[i];
    }

    /**
     * Overrides the ItemStack at the specified index.
     * @param i The index to override.
     * @param itemStack The ItemStack to set the index to.
     */
    public void setInventorySlot(int i, ItemStack itemStack){
        inventory[i] = itemStack;
        resetEmptySlots();
    }

    /**
     * Sorts the items in the inventory based on their position in ItemID.java, where items higher up are listed higher in the inventory.
     */
    public void sortInventory(){
        //Use inventory[i].holdableObject.itemID.ordinal(); to get a numeric ID for each item.
        //Use quicksort or radix sort (ideally quicksort, easier to implement(?))
        //Switch to Insertion sort when sub-arrays are 7 elements or fewer.
        resetEmptySlots();
    }

    /**
     * Resets an item in the inventory to a new ItemStack if it's quantity is 0.
     */
    private void resetEmptySlots(){
        for (int i = 0; i < inventory.length; i++) {
            if(inventory[i].quantity <= 0){
                inventory[i] = new ItemStack();
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < inventory.length; i++) {
            result.append(i).append(":").append(inventory[i].toString());
            if(i < inventory.length-1){
                result.append(", ");
            }
        }
        return result + "]";
    }
}
