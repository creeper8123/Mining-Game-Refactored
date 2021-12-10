import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
            case TILE_DIRT -> {
                return 100;
            }
            case TILE_STONE -> {
                return 100;
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
            case ITEM_PINE_CONE -> {
                return 100;
            }
            case ITEM_STICK -> {
                return 125;
            }
            case TILE_WOOD -> {
                return 75;
            }
            case TILE_WORKBENCH -> {
                return 10;
            }
        }
        return Integer.MAX_VALUE;
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

    public static void createCraftingMenuInAltMenu(String title, ItemStack[] items){
        AtomicInteger craftingQuantity = new AtomicInteger(1);
        Game.player.altMenu.deconstruct(true);
        Game.player.altMenu = new MenuDisplay<>(350, 50){
            @Override
            public void configureMenuContents(){
                this.menuDisplayContents = new String[menuContents.length];
                for (int i = 0; i < this.menuDisplayContents.length; i++) {
                    if(i == selectedItem){
                        if(this.menuContents[i] == null || this.menuContents[i].holdableObject == null || this.menuContents[i].holdableObject.itemID == null){
                            this.menuDisplayContents[i] = "&gt[Empty]&lt";
                        }else{
                            this.menuDisplayContents[i] = "&gt[" + this.menuContents[i].holdableObject.displayName + " *" + this.menuContents[i].quantity + "]&lt";
                        }
                    }else{
                        if(this.menuContents[i] == null || this.menuContents[i].holdableObject == null || this.menuContents[i].holdableObject.itemID == null){
                            this.menuDisplayContents[i] = "&#8194[Empty]&#8194";
                        }else{
                            this.menuDisplayContents[i] = "&#8194[" + this.menuContents[i].holdableObject.displayName + " *" + this.menuContents[i].quantity + "]&#8194";
                        }
                    }
                }
                this.fullMenuDisplayContents = "<html><body><u>" + menuTitle + "</u>";
                for (String menuDisplayContent : this.menuDisplayContents)
                    this.fullMenuDisplayContents += "<br>" + menuDisplayContent;
                this.fullMenuDisplayContents += "<br/><br/>"; //Add a <br/> for every line break
                this.fullMenuDisplayContents += "<u>---Crafting Ingredients---</u>";
                if(selectedItem > -1){
                    ItemStack[] ingredients = items[selectedItem].holdableObject.getCraftingRecipe();
                    if(ingredients != null){
                        for (int i = 0 ; i < ingredients.length ; i++) {
                            if(ingredients[i] == null){
                                this.fullMenuDisplayContents += "<br>&#8194-----&#8194";
                            }else{
                                this.fullMenuDisplayContents += "<br>&#8194[" + ingredients[i].holdableObject.displayName + " *" + ingredients[i].quantity + "]&#8194";
                            }
                        }
                    }else{
                        for (int i = 0 ; i < 5 ; i++) {
                            this.fullMenuDisplayContents += "<br>&#8194-----&#8194";
                        }
                    }
                }else{
                    for (int i = 0 ; i < 5 ; i++) {
                        this.fullMenuDisplayContents += "<br>&#8194-----&#8194";
                    }
                }
                this.fullMenuDisplayContents += "<br/><br/>Quantity: " + craftingQuantity;
                this.fullMenuDisplayContents +="<br/><br/><br/><br/></body></html>";
                this.menu.setText(fullMenuDisplayContents);
            }
        };
        Game.player.altMenu.setMenuTitle(title);
        Game.player.altMenu.setMenuContents(items);
        Game.player.altMenu.setMenuSize(212, (Game.player.altMenu.menuContents.length + 13) * MenuDisplay.STANDARD_TEXT_HEIGHT_PIXELS);
        Game.player.altMenu.configureMenuContents();
        Game.player.altMenu.addButton(0, ((Game.player.altMenu.menuContents.length + 10) * MenuDisplay.STANDARD_TEXT_HEIGHT_PIXELS)+5, 20, 46, "--", () -> {
            craftingQuantity.addAndGet(-10);
            if(craftingQuantity.get() < 1){
                craftingQuantity.set(1);
            }
            Game.player.altMenu.configureMenuContents();
        });
        Game.player.altMenu.addButton(46, ((Game.player.altMenu.menuContents.length + 10) * MenuDisplay.STANDARD_TEXT_HEIGHT_PIXELS)+5, 20, 40, "-", () -> {
            craftingQuantity.addAndGet(-1);
            if(craftingQuantity.get() < 1){
                craftingQuantity.set(1);
            }
            Game.player.altMenu.configureMenuContents();
        });
        Game.player.altMenu.addButton(86, ((Game.player.altMenu.menuContents.length + 10) * MenuDisplay.STANDARD_TEXT_HEIGHT_PIXELS)+5, 20, 40, "1", () -> {
            craftingQuantity.set(1);
            Game.player.altMenu.configureMenuContents();
        });
        Game.player.altMenu.addButton(126, ((Game.player.altMenu.menuContents.length + 10) * MenuDisplay.STANDARD_TEXT_HEIGHT_PIXELS)+5, 20, 40, "+", () -> {
            craftingQuantity.addAndGet(1);
            if(craftingQuantity.get() < 1){
                craftingQuantity.set(1);
            }
            Game.player.altMenu.configureMenuContents();
        });
        Game.player.altMenu.addButton(166, ((Game.player.altMenu.menuContents.length + 10) * MenuDisplay.STANDARD_TEXT_HEIGHT_PIXELS)+5, 20, 46, "++", () -> {
            craftingQuantity.addAndGet(10);
            if(craftingQuantity.get() < 1){
                craftingQuantity.set(1);
            }
            Game.player.altMenu.configureMenuContents();
        });
        Game.player.altMenu.addButton(0, ((Game.player.altMenu.menuContents.length + 11) * MenuDisplay.STANDARD_TEXT_HEIGHT_PIXELS)+10, 20, 212, "Craft Selected Item", () -> {
            if(Game.player.altMenu.selectedItem > -1){
                boolean cancelCraft = false;
                ItemStack itemToCraft = Game.player.altMenu.menuContents[Game.player.altMenu.selectedItem];
                ItemStack[] craftingRecipe = itemToCraft.holdableObject.getCraftingRecipe();
                for (int i = 0 ; i < craftingQuantity.get() ; i++) {
                    for (int j = 0 ; j < craftingRecipe.length ; j++) {
                        if(craftingRecipe[j] != null){
                            if(Game.player.inventory.getQuantityInInventory(craftingRecipe[j].holdableObject) < craftingRecipe[j].quantity){
                                cancelCraft = true;
                                break;
                            }
                        }
                    }
                    if(!cancelCraft){
                        int remainder = Game.player.inventory.addToInventory(itemToCraft.holdableObject, itemToCraft.quantity);
                        if(remainder == 0){
                            for (int j = 0 ; j < craftingRecipe.length ; j++) {
                                if(craftingRecipe[j] != null){
                                    Game.player.inventory.removeFromInventory(craftingRecipe[j].holdableObject, craftingRecipe[j].quantity);
                                }
                            }
                        }else{
                            Game.player.inventory.removeFromInventory(itemToCraft.holdableObject, itemToCraft.quantity - remainder);
                        }
                    }
                }

            }
        });
        Game.player.altMenu.setVisibility(true);
        Game.player.inventoryMenu.setVisibility(true);
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
        //TODO: Implement Sorting algorithm for inventory
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
            if(inventory[i] == null || inventory[i].quantity <= 0){
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
