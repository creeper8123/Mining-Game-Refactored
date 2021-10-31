import java.awt.*;

public class FallingStalactite extends MovingObject{


    public FallingStalactite(double x, double y, int width, int height, String textureLocation) {
        super(x, y, 0, width, height, textureLocation);
    }


    public FallingStalactite(double x, double y, int width, int height, Image newTexture) {
        super(x, y, 0, width, height, newTexture);
    }


    @Override
    public void postInitialization() {
        terminalVelocity = 35;
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