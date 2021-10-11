public class FallingStalactite extends MovingObject{

    public FallingStalactite(double x, double y, int width, int height) {
        super(x, y, width, height);
        terminalVelocity = 35;
        gravity += gravity/4;
    }

    @Override
    void onUpdate() {
        calculateNewPosition();
        deconstruct(verticalTileCollisionDetected);
    }
}