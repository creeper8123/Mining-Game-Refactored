package AllClasses;

import UniqueIDs.ItemID;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes how objects move in the world, and how they interact with their environment.
 */
public abstract class MovingObject {

    /**
     * Defines the shape, size, location, and looks of a new object, then adds it to the game&#46; Its texture is pulled from resources folder.
     * @param x The x position the object will spawn at (from the upper right corner).
     * @param y The y position the object will spawn at (from the upper right corner).
     * @param z The level the object will spawn in.
     * @param width The width of the object (from the upper right corner).
     * @param height The height of the object (from the upper right corner).
     * @param textureLocation The String filepath of the texture to load.
     */
    public MovingObject(double x, double y, int z, int width, int height, String textureLocation){
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.hitbox = new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);

        this.textureLabel = new JLabel();
        this.textureLabel.setBounds(hitbox);
        this.texture = ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(ImageProcessing.getImageFromResources(textureLocation)), 4, 4);
        this.textureLabel.setIcon(new ImageIcon(this.texture));

        postInitialization();

        Game.layeredPane.add(textureLabel);
        Game.layeredPane.setLayer(textureLabel, textureLayer);
    }

    /**
     * Defines the shape, size, location, and looks of a new object, then adds it to the game&#46; Its texture is predefined.
     * @param x The x position the object will spawn at (from the upper right corner).
     * @param y The y position the object will spawn at (from the upper right corner).
     * @param z The level the object will spawn in.
     * @param width The width of the object (from the upper right corner).
     * @param height The height of the object (from the upper right corner).
     * @param newTexture The Image that the object will be set to with no further processing.
     */
    public MovingObject(double x, double y, int z, int width, int height, Image newTexture){
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.hitbox = new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);
        this.texture = ImageProcessing.imageToBufferedImage(newTexture);

        this.textureLabel = new JLabel();
        this.textureLabel.setBounds(hitbox.x + textureXOffset, hitbox.y + textureYOffset, hitbox.width, hitbox.height);
        this.textureLabel.setIcon(new ImageIcon(this.texture));

        postInitialization();

        Game.layeredPane.add(textureLabel);
        Game.layeredPane.setLayer(textureLabel, textureLayer);
    }

    public MovingObject(double x, double y, int z, int width, int height, BufferedImage newTexture){
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.hitbox = new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);

        this.textureLabel = new JLabel();
        this.textureLabel.setBounds(hitbox.x + textureXOffset, hitbox.y + textureYOffset, hitbox.width, hitbox.height);
        this.texture = newTexture;
        this.textureLabel.setIcon(new ImageIcon(this.texture));

        postInitialization();

        Game.layeredPane.add(textureLabel);
        Game.layeredPane.setLayer(textureLabel, textureLayer);
    }

    //Default values, can be changed in objects extends AllClasses.MovingObject
    /**The ArrayList of ItemIDs that the moving object will not check for collisions on.*/ArrayList<ItemID> nonSolidTiles = new ArrayList<>(List.of(ItemID.TILE_AIR));

    /**The width of the object (from the upper left corner).*/int width;
    /**The height of the object (from the upper left corner).*/int height;

    /**The x-position of the object (from the upper left corner).*/double x;
    /**The y-position of the object (from the upper left corner).*/double y;
    /**The current level of the object (from the upper left corner).*/int z;

    /**The Rectangle object used for collision detection.*/Rectangle hitbox;
    /**The JLabel object that is used for rendering.*/JLabel textureLabel;
    /**The number of pixels to shift the texture right (can be negative to shift left).*/int textureXOffset;
    /**The number of pixels to shift the texture down (can be negative to shift up).*/int textureYOffset;
    BufferedImage texture;
    int textureLayer = ImageProcessing.MOVING_OBJECT_LAYER;

    /**The flag for if the object is trying to move left.*/boolean left;
    /**The flag for if the object is trying to move right.*/boolean right;
    /**The flag for if the object is trying to move up.*/boolean up;
    /**The flag for if the object is trying to move down.*/boolean down;
    /**The flag for if the object is trying to sprint.*/boolean sprinting;

    /**The current horizontal speed of the object.*/double xSpeed = 0.0;
    /**The current vertical speed of the object.*/double ySpeed = 0.0;
    /**The current horizontal speed the object is trying to reach.*/double xSpeedTarget = 0.0;
    /**The current vertical speed the object is trying to reach.*/double ySpeedTarget = 0.0;
    /**The amount that the xSpeed will change per update when moving to a different speed.*/double xSpeedAcceleration = 0.20;
    /**The maximum xSpeed of the object under normal circumstances.*/double xSpeedLimit = 5;
    /**The multiplier of xSpeedLimit when the object is trying to sprint.*/double sprintSpeedMultiplier = 1.33;
    /**The value that the ySpeed will be set to if the object is touching the ground and the up flag is set.*/double jumpForce = 10;
    /**The highest value that ySpeed can be under normal circumstances.*/double terminalVelocity = 25;
    /**The multiplier of xSpeedAcceleration when the object is in the air.*/double airborneManeuverabilityMultiplier = 0.25;
    /**The value that, if the difference between the current speed and the target speed is less than, the current speed will be set to the target speed.*/double speedDeadZone = 0.1;
    /**The value that's added to the ySpeed every update if the object is not on the ground.*/double gravity = 0.4;

    /**The flag for if the object has the ability to sprint.*/boolean canSprint = true;
    /**The flag for if the object is affected by gravity*/boolean hasGravity = true; //TODO: make objects with no gravity float towards ySpeed 0, using the gravity value as a dampener rate.
    /**The flag for if the object has a tile 1 pixel beneath it, and it's ySpeed is 0*/boolean onGround;

    /**The flag for if the object collided with the side of a tile (xSpeed must have not been 0).*/boolean horizontalTileCollisionDetected;
    /**The flag for if the object collided with the top or the bottom of a tile (ySpeed must have not been 0).*/boolean verticalTileCollisionDetected;
    /***/InventoryManager inventory;

    /**
     * Commits any changes that need to be made to the individual object before it's displayed on the screen.
     */
    public abstract void postInitialization();

    /**
     * Sets the target speeds of the object depending on what direction it's currently trying to move in.
     */
    void setTargetSpeed(){
        if(left ^ right){
            if(left){
                if(sprinting && canSprint){
                    xSpeedTarget = -xSpeedLimit * sprintSpeedMultiplier;
                }else{
                    xSpeedTarget = -xSpeedLimit;
                }
            }else{
                if(sprinting && canSprint){
                    xSpeedTarget = xSpeedLimit * sprintSpeedMultiplier;
                }else{
                    xSpeedTarget = xSpeedLimit;
                }
            }
        }else{
            if(onGround){
                xSpeedTarget = 0;
            }else{
                xSpeedTarget = xSpeed;
            }
        }
    }

    /**
     * Sets the speed to its target value if the difference between the target speed and the actual speed is less than speedDeadZone.
     */
    void applySpeedDeadzone(){
         if((Math.abs(xSpeed - xSpeedTarget) < speedDeadZone)){
             xSpeed = xSpeedTarget;
         }else if(xSpeed > xSpeedTarget){
             if(onGround){
                 xSpeed -= xSpeedAcceleration;
             }else{
                 xSpeed -= (xSpeedAcceleration * airborneManeuverabilityMultiplier);
             }
         }else if(xSpeed < xSpeedTarget){
             if(onGround){
                 xSpeed += xSpeedAcceleration;
             }else{
                 xSpeed += (xSpeedAcceleration * airborneManeuverabilityMultiplier);
             }
         }
     }

    /**
     * Increases the downward speed if the object is not on the ground, and it's moving slower than its terminal velocity.
     */
    void applyGravity(){
        if(!onGround && hasGravity){
            if(ySpeed < terminalVelocity){
                ySpeed += gravity; //If not on ground and slower than terminal velocity, accelerate downwards
            }else if(ySpeed > terminalVelocity){
                ySpeed = terminalVelocity;//If not on ground and faster than terminal velocity, set downward speed to terminal velocity
            }//If not on ground and moving at terminal velocity, do nothing
        }
    }


    //TODO: Fix dead stop on landing bug
    //To recreate: jump twice without releasing jump button, will stop dead upon landing.
    //Related: Some jumps are higher than others. Easiest to replicate on all flat terrain (IE bottom of the world)
    //Dead stop only happens on the higher jumps
    //TODO: Fix twitching when pushing to the right bug.
    /**
     * Checks if the object is intersecting with any tiles not on the nonSolidTiles ArrayList&#46; If an intersection is detected, the object is pushed away from the tile and the appropriate flag is set for 1 tick.
     */
    void collisionDetection(){

        final int COLLISION_RANGE = 2;

        int minX = (int) Math.round(((double) hitbox.x / TileGraphics.TILE_WIDTH) - COLLISION_RANGE);
        int maxX = (int) Math.round(((double) hitbox.x / TileGraphics.TILE_WIDTH) + COLLISION_RANGE);
        int minY = (int) Math.round(((double) hitbox.y / TileGraphics.TILE_HEIGHT) - COLLISION_RANGE);
        int maxY = (int) Math.round(((double) hitbox.y / TileGraphics.TILE_HEIGHT) + COLLISION_RANGE);

        if(minX < COLLISION_RANGE){
            minX = 0;
            maxX = (2 * COLLISION_RANGE) + 1;
        }else if(maxX > Game.tiles.length - 1){
            maxX = Game.tiles.length - 1;
            minX = (Game.tiles.length - 1) - ((2 * COLLISION_RANGE) + 1);
        }

        if(minY < COLLISION_RANGE){
            minY = 0;
            maxY = (2 * COLLISION_RANGE) + 1;
        }else if(maxY > Game.tiles[0].length - 1){
            maxY = Game.tiles[0].length - 1;
            minY = (Game.tiles[0].length - 1) - ((2 * COLLISION_RANGE) + 1);
        }

        horizontalTileCollisionDetected = false;
        if(xSpeed != 0){
            hitbox.x += xSpeed;
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    if(hitbox.intersects(Game.tiles[x][y].hitbox) && !nonSolidTiles.contains(Game.tiles[x][y].itemID)){
                        hitbox.x -= xSpeed;
                        horizontalTileCollisionDetected = true;
                        while (!hitbox.intersects(Game.tiles[x][y].hitbox)) {
                            hitbox.x += Math.signum(xSpeed);
                        }
                        hitbox.x -= Math.signum(xSpeed);
                        xSpeed = 0;
                        this.x = hitbox.x;
                    }
                }
            }
        }

        verticalTileCollisionDetected = false;
        if(ySpeed != 0){
            hitbox.y += ySpeed;
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    if(hitbox.intersects(Game.tiles[x][y].hitbox) && !nonSolidTiles.contains(Game.tiles[x][y].itemID)){
                        hitbox.y -= ySpeed;
                        verticalTileCollisionDetected = true;
                        while (!hitbox.intersects(Game.tiles[x][y].hitbox)) {
                            hitbox.y += Math.signum(ySpeed);
                        }
                        hitbox.y -= Math.signum(ySpeed);
                        ySpeed = 0;
                        this.y = hitbox.y;
                    }
                }
            }
        }
    }

    /**
     * Checks if the object is touching the ground, and sets the onGround flag appropriately.
     */
    void checkOnGround(){
        final int COLLISION_RANGE = 1;

        int minX = (int) Math.round(((double) hitbox.x / TileGraphics.TILE_WIDTH) - COLLISION_RANGE);
        int maxX = (int) Math.round(((double) hitbox.x / TileGraphics.TILE_WIDTH) + COLLISION_RANGE);
        int minY = (int) Math.round(((double) hitbox.y / TileGraphics.TILE_HEIGHT) - COLLISION_RANGE);
        int maxY = (int) Math.round(((double) hitbox.y / TileGraphics.TILE_HEIGHT) + COLLISION_RANGE);

        if(minX < COLLISION_RANGE){
            minX = 0;
            maxX = (2 * COLLISION_RANGE) + 1;
        }else if(maxX > Game.tiles.length - 1){
            maxX = Game.tiles.length - 1;
            minX = (Game.tiles.length - 1) - ((2 * COLLISION_RANGE) + 1);
        }

        if(minY < COLLISION_RANGE){
            minY = 0;
            maxY = (2 * COLLISION_RANGE) + 1;
        }else if(maxY > Game.tiles[0].length - 1){
            maxY = Game.tiles[0].length - 1;
            minY = (Game.tiles[0].length - 1) - ((2 * COLLISION_RANGE) + 1);
        }

        hitbox.y++;
        for (int x = minX; x <= maxX; x++) {
            boolean broken = false;
            for (int y = minY; y <= maxY; y++) {
                if (hitbox.intersects(Game.tiles[x][y].hitbox) && !nonSolidTiles.contains(Game.tiles[x][y].itemID)) {
                    onGround = true;
                    broken = true;
                    break;
                } else {
                    onGround = false;
                }
            }
            if(broken){
                break;
            }
        }
        hitbox.y--;
    }

    /**
     * If the object is on the ground and is wanting to jump, then its vertical speed is set to -jumpForce.
     */
    void applyJumpForce(){
        if(up && onGround){
            ySpeed = -jumpForce; //Jump if the player is on the ground and the jump key is pressed.
        }
    }

    /**
     * Adds the current x and y speeds to the position, and sets both the hitbox and texture to the newly updated position.
     */
    void confirmPosition(){
        y += ySpeed;
        x += xSpeed;

        hitbox.setLocation((int) Math.round(x), (int) Math.round(y));
        textureLabel.setLocation(((int) Math.round(x))+textureXOffset, ((int) Math.round(y))+textureYOffset);
    }

    /**
     * Calls various methods to generate and confirm a new position for the object.
     */
    public void calculateNewPosition(){
        setTargetSpeed();
        applySpeedDeadzone();
        applyGravity();
        collisionDetection();
        checkOnGround();
        applyJumpForce();
        confirmPosition();
    }

    /**
     * This method is called every tick for the object.
     */
    void onUpdate(){
        calculateNewPosition();
    }

    /**
     * Removes the object from the screen and from the list of objects to update each tick.
     * @param confirm If true, the object will follow through with deconstruction&#46; If false, the deconstruction is aborted.
     */
    void deconstruct(boolean confirm){
        if(confirm){
            Game.layeredPane.remove(textureLabel);
            Game.livingEntities.remove(this);
        }
    }

    /**
     * Generates a large String of useful information about the object's current situation.
     * @return Returns a String containing its x and y positions, its x and y speeds, and its x and y target speeds.
     */
    @Override
    public String toString(){
        String entityData = "Entity Info: {";

        entityData += "Position: {";
        entityData += "X: [" + x;
        entityData += "], Y: [" + y;
        entityData += "]}";

        entityData += ", Speed: {";
        entityData += "X Speed: [" + xSpeed;
        entityData += "], Y Speed: [" + ySpeed;
        entityData += "]}";

        entityData += ", Speed Targets: {";
        entityData += "X Speed Target: [" + xSpeedTarget;
        entityData += "], Y Speed Target: [" + ySpeedTarget;
        entityData += "]}";

        entityData += "}";
        return entityData;
    }
}
