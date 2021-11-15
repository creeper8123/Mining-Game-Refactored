import java.awt.*;

/**
 * A FallingStalactite is a moving objects that falls when spawned, and is deconstructed when it hits another solid tile.
 */
public class FallingStalactite extends MovingObject{

    /**
     * Calls the MovingObject constructor with the following parameters
     * @param x The x position the FallingStalactite will spawn at (from the upper right corner).
     * @param y The y position the FallingStalactite will spawn at (from the upper right corner).
     * @param width The width of the Falling Stalactite's hitbox.
     * @param height The height of the Falling Stalactite's hitbox.
     * @param textureLocation The file path of the texture of the Falling Stalactite.
     */
    public FallingStalactite(double x, double y, int width, int height, String textureLocation) {
        super(x, y, 0, width, height, textureLocation);
    }

    /**
     * Calls the MovingObject constructor with the following parameters
     * @param x The x position the FallingStalactite will spawn at (from the upper right corner).
     * @param y The y position the FallingStalactite will spawn at (from the upper right corner).
     * @param width The width of the Falling Stalactite's hitbox.
     * @param height The height of the Falling Stalactite's hitbox.
     * @param newTexture The texture to be shown on the object.
     */
    public FallingStalactite(double x, double y, int width, int height, Image newTexture) {
        super(x, y, 0, width, height, newTexture);
    }


    @Override
    public void postInitialization() {
        this.gravity *= 1.25;
        this.terminalVelocity *= 1.25;
        this.textureXOffset = -20;
        this.hitbox = new Rectangle(hitbox.x - textureXOffset, hitbox.y - textureYOffset, hitbox.width, hitbox.height);
        this.x -= textureXOffset;
        this.textureLabel.setBounds(hitbox.x + textureXOffset, hitbox.y + textureYOffset, Tiles.TILE_WIDTH, Tiles.TILE_HEIGHT);
    }


    @Override
    void onUpdate() {
        calculateNewPosition();
        deconstruct(verticalTileCollisionDetected);
    }

    @Override
    public void calculateNewPosition(){
        applySpeedDeadzone();
        applyGravity();
        collisionDetection();
        confirmPosition();
    }
}