public class FallingStalactite extends MovingObject{

    public FallingStalactite(double x, double y, int width, int height, String textureLocation) {
        super(x, y, 0, width, height, textureLocation);
        terminalVelocity = 35;
        gravity += gravity/4;
    }

    @Override
    void onUpdate() {
        calculateNewPosition();
        deconstruct(verticalTileCollisionDetected);
    }
}