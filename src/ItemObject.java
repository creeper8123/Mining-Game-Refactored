/**
 * Any object that can be held but not directly placed in the world. Has the option of doing various things when used.
 */
public abstract class ItemObject extends HoldableObject {
    public ItemObject(ItemID itemID) {
        super(itemID);
    }
}
