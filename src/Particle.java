import javax.swing.*;
import java.awt.*;

public class Particle extends MovingObject {
    double opacity = 255;
    double fadePerUpdate;
    boolean hasCollision;

    public Particle(double x, double y, int z, int width, int height, String textureLocation, double initialXSpeed, double initialYSpeed, boolean hasGravity, double fadePerUpdate, boolean hasCollision, double gravity) {
        super(x, y, z, width, height, textureLocation);
        this.xSpeed = initialXSpeed;
        this.ySpeed = initialYSpeed;
        this.hasGravity = hasGravity;
        this.fadePerUpdate = fadePerUpdate;
        this.hasCollision = hasCollision;
        this.gravity = gravity;
    }

    public Particle(double x, double y, int z, int width, int height, Image newTexture, double initialXSpeed, double initialYSpeed, boolean hasGravity, double fadePerUpdate, boolean hasCollision, double gravity) {
        super(x, y, z, width, height, newTexture);
        this.xSpeed = initialXSpeed;
        this.ySpeed = initialYSpeed;
        this.hasGravity = hasGravity;
        this.fadePerUpdate = fadePerUpdate;
        this.hasCollision = hasCollision;
        this.gravity = gravity;
    }


    @Override
    public void postInitialization() {
        textureLayer = ImageProcessing.PARTICLE_LAYER;
    }

    @Override
    public void onUpdate(){
        calculateNewPosition();
        this.textureLabel.setIcon(new ImageIcon(this.texture));
        opacity -= fadePerUpdate;
        ImageProcessing.changeImageAlpha(this.texture, (int) opacity, true);
        deconstruct(opacity <= 0);
    }

    @Override
    public void calculateNewPosition(){
        if(this.hasGravity){
            applyGravity();
        }
        if(this.hasCollision){
            collisionDetection();
            checkOnGround();
        }
        confirmPosition();
    }

    @Override
    void deconstruct(boolean confirm){
        if(confirm){
            Game.layeredPane.remove(textureLabel);
            this.hitbox = null;
            Game.livingParticles.remove(this);
        }
    }
}
