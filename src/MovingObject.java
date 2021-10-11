import javax.swing.*;
import java.awt.*;

public abstract class MovingObject {
    public MovingObject(double x, double y, int width, int height){
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.hitbox = new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);

        this.textureLabel = new JLabel();
        this.textureLabel.setBounds(hitbox);
        this.textureLabel.setIcon(new ImageIcon(ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(ImageProcessing.getImageFromResources("textures/missingTexture.png")), 4)));
        Game.layeredPane.add(textureLabel);
        Game.layeredPane.setLayer(textureLabel, MovingObject.MOVING_OBJECT_LAYER);
    }


    static final int MOVING_OBJECT_LAYER = 3;

    //Default values, can be changed in objects extends MovingObject

    int width;
    int height;

    double x;
    double y;

    Rectangle hitbox;
    JLabel textureLabel;
    int textureXOffset;
    int textureYOffset;

    boolean left;
    boolean right;
    boolean up;
    boolean down;
    boolean sprinting;

    double xSpeed = 0.0;
    double ySpeed = 0.0;
    double xSpeedTarget = 0.0;
    double ySpeedTarget = 0.0;
    double xSpeedAcceleration = 0.15;
    double xSpeedLimit = 5.0;
    double sprintSpeedMultiplier = 1.5;
    double jumpForce = 12.5;
    double terminalVelocity = 25.0;
    double airborneManeuverabilityMultiplier = 0.25;
    double speedDeadZone = 0.1;
    double gravity = 0.4;

    boolean canSprint = true;
    boolean hasGravity = true;
    boolean onGround;
    boolean horizontalTileCollisionDetected;
    boolean verticalTileCollisionDetected;

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

    void applyGravity(){
        if(!onGround && hasGravity){
            if(ySpeed < terminalVelocity){
                ySpeed += gravity; //If not on ground and slower than terminal velocity, accelerate downwards
            }else if(ySpeed > terminalVelocity){
                ySpeed = terminalVelocity;//If not on ground and faster than terminal velocity, set downward speed to terminal velocity
            }//If not on ground and moving at terminal velocity, do nothing
        }
    }

    void collisionDetection(){

        final int COLLISION_RANGE = 2;

        int minX = (int) Math.round(((double) hitbox.x / Tiles.TILE_WIDTH) - COLLISION_RANGE);
        int maxX = (int) Math.round(((double) hitbox.x / Tiles.TILE_WIDTH) + COLLISION_RANGE);
        int minY = (int) Math.round(((double) hitbox.y / Tiles.TILE_HEIGHT) - COLLISION_RANGE);
        int maxY = (int) Math.round(((double) hitbox.y / Tiles.TILE_HEIGHT) + COLLISION_RANGE);

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
            for (int i = minX; i <= maxX; i++) {
                for (int j = minY; j <= maxY; j++) {
                    if(hitbox.intersects(Game.tiles[i][j].hitbox) && Game.tiles[i][j].tileID != Tiles.Tile.TILE_AIR && !Game.tiles[i][j].isBroken){
                        hitbox.x -= xSpeed;
                        horizontalTileCollisionDetected = true;
                        while (!hitbox.intersects(Game.tiles[i][j].hitbox)) {
                            hitbox.x += Math.signum(xSpeed);
                        }
                        hitbox.x -= Math.signum(xSpeed);
                        xSpeed = 0;
                        x = hitbox.x;
                    }
                }
            }
        }

        verticalTileCollisionDetected = false;
        if(ySpeed != 0){
            hitbox.y += ySpeed;
            for (int i = minX; i <= maxX; i++) {
                for (int j = minY; j <= maxY; j++) {
                    if(hitbox.intersects(Game.tiles[i][j].hitbox) && Game.tiles[i][j].tileID != Tiles.Tile.TILE_AIR && !Game.tiles[i][j].isBroken){
                        hitbox.y -= ySpeed;
                        verticalTileCollisionDetected = true;
                        while (!hitbox.intersects(Game.tiles[i][j].hitbox)) {
                            hitbox.y += Math.signum(ySpeed);
                        }
                        hitbox.y -= Math.signum(ySpeed);
                        ySpeed = 0;
                        y = hitbox.y;
                    }
                }
            }
        }
    }

    void checkOnGround(){
        final int COLLISION_RANGE = 2;

        int minX = (int) Math.round(((double) hitbox.x / Tiles.TILE_WIDTH) - COLLISION_RANGE);
        int maxX = (int) Math.round(((double) hitbox.x / Tiles.TILE_WIDTH) + COLLISION_RANGE);
        int minY = (int) Math.round(((double) hitbox.y / Tiles.TILE_HEIGHT) - COLLISION_RANGE);
        int maxY = (int) Math.round(((double) hitbox.y / Tiles.TILE_HEIGHT) + COLLISION_RANGE);

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
        for (int i = minX; i <= maxX; i++) {
            boolean broken = false;
            for (int j = minY; j <= maxY; j++) {
                if (hitbox.intersects(Game.tiles[i][j].hitbox) && Game.tiles[i][j].tileID != Tiles.Tile.TILE_AIR && !Game.tiles[i][j].isBroken) {
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
    void applyJumpForce(){
        if(up && onGround){
            ySpeed = -jumpForce; //Jump if the player is on the ground and the jump key is pressed.
        }
    }

    void confirmPosition(){
        y += ySpeed;
        x += xSpeed;

        hitbox.setLocation((int) Math.round(x), (int) Math.round(y));
        textureLabel.setLocation(((int) Math.round(x))+textureXOffset, ((int) Math.round(y))+textureYOffset);
    }

    public void calculateNewPosition(){
        setTargetSpeed();
        applySpeedDeadzone();
        applyGravity();
        collisionDetection();
        checkOnGround();
        applyJumpForce();
        confirmPosition();
    }

    void onUpdate(){
        calculateNewPosition();
    }

    void deconstruct(boolean confirm){
        if(confirm){
            Game.layeredPane.remove(textureLabel);
            this.hitbox = null;
            Game.movingObjects.remove(this);
        }
    }

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
