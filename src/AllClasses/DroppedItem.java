package AllClasses;

import UniqueIDs.ItemID;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DroppedItem extends MovingObject{
    ItemID itemID;
    int quantity;

    public DroppedItem(double x, double y, int z, int width, int height, Image newTexture, ItemID itemID, int quantity) {
        super(x, y, z, width, height, newTexture);
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public DroppedItem(double x, double y, int z, int width, int height, BufferedImage newTexture, ItemID itemID, int quantity) {
        super(x, y, z, width, height, newTexture);
        this.itemID = itemID;
        this.quantity = quantity;
    }

    @Override
    public void postInitialization() {

    }

    @Override
    void onUpdate(){
        calculateNewPosition();
        if(Game.player != null && this.hitbox.intersects(Game.player.hitbox)){
            this.quantity = Game.player.inventory.addToInventory(new HoldableObject(itemID), quantity);
        }
        for (int i = 0 ; i < Game.livingEntities.size(); i++) {
            int oldSize = Game.livingEntities.size();
            if(Game.livingEntities.get(i) != null && this.hitbox.intersects(Game.livingEntities.get(i).hitbox)){
                this.quantity = Game.livingEntities.get(i).inventory.addToInventory(new HoldableObject(itemID), quantity);
            }
            if(Game.livingEntities.size() == oldSize-1){
                i--;
            }
        }
        deconstruct(this.quantity <= 0);
    }

    @Override
    public void calculateNewPosition(){
        applyGravity();
        collisionDetection();
        checkOnGround();
        confirmPosition();
    }

    @Override
    void deconstruct(boolean confirm){
        if(confirm){
            Game.layeredPane.remove(textureLabel);
            Game.livingDroppedItems.remove(this);
        }
    }
}
