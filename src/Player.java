import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player extends MovingObject implements KeyListener {
    public static final int PLAYER_WIDTH = 32;
    public static final int PLAYER_HEIGHT = 48;
    public static final double PLAYER_REACH = 256;

    public Player(double initialX, double initialY){
        super(initialX, initialY, PLAYER_WIDTH, PLAYER_HEIGHT);
        textureLabel.setIcon(new ImageIcon(ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(ImageProcessing.getImageFromResources("textures/missingTexture.png")), 4)));
        Game.frame.addKeyListener(this);
    }

    boolean upPressed;
    boolean downPressed;
    boolean leftPressed;
    boolean rightPressed;
    boolean sprintPressed;

    @Override
    public void onUpdate() {
        this.calculateNewPosition();
        Game.moveCamera(x, y, width, height);
        //Game.scrollLevel = (int) x/64;
        this.textureXOffset = -Game.scrollLevel * Tiles.TILE_WIDTH;
    }

    //TODO: Fix dead stop on landing bug.
    //To recreate: jump twice without releasing jump button, will stop dead upon landing.
    //Related: Some jumps are higher than others. Easiest to replicate on all flat terrain (IE bottom of the world)
        //Dead stop only happens on the higher jumps
    @Override
    void setTargetSpeed() {
        if(leftPressed ^ rightPressed){
            if(leftPressed){
                if(sprintPressed && canSprint){
                    xSpeedTarget = -xSpeedLimit * sprintSpeedMultiplier;
                }else{
                    xSpeedTarget = -xSpeedLimit;
                }
            }else{
                if(sprintPressed && canSprint){
                    xSpeedTarget = xSpeedLimit * sprintSpeedMultiplier;
                }else{
                    xSpeedTarget = xSpeedLimit;
                }
            }
        }else{
            if(this.onGround){
                this.xSpeedTarget = 0;
            }else{
                this.xSpeedTarget = this.xSpeed;
            }
        }
    }

    @Override
    void applyJumpForce(){
        if(upPressed && onGround){
            ySpeed = -jumpForce; //Jump if the player is on the ground and the jump key is pressed.
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case 16, 17 -> this.sprintPressed = true;
            case 37, 65 -> this.leftPressed = true;
            case 38, 87, 32 -> this.upPressed = true;
            case 39, 68 -> this.rightPressed = true;
            case 40, 83 -> this.downPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case 16, 17 -> this.sprintPressed = false;
            case 37, 65 -> this.leftPressed = false;
            case 38, 87, 32 -> this.upPressed = false;
            case 39, 68 -> this.rightPressed = false;
            case 40, 83 -> this.downPressed = false;
        }
    }

    @Override
    public String toString(){
        String playerData = "Player Info: {";
        playerData += "Key Inputs: {";
        playerData += "Left: [" + leftPressed;
        playerData += "], Right: [" + rightPressed;
        playerData += "], Up: [" + upPressed;
        playerData += "], Down: [" + downPressed;
        playerData += "], Sprint: [" + sprintPressed;
        playerData += "]}";

        playerData += ", Position: {";
        playerData += "X: [" + x;
        playerData += "], Y: [" + y;
        playerData += "]}";

        playerData += ", Speed: {";
        playerData += "X Speed: [" + xSpeed;
        playerData += "], Y Speed: [" + ySpeed;
        playerData += "]}";

        playerData += ", Speed Targets: {";
        playerData += "X Speed Target: [" + xSpeedTarget;
        playerData += "], Y Speed Target: [" + ySpeedTarget;
        playerData += "]}";

        playerData += ", On Ground: [";
        playerData += onGround;
        playerData += "]";

        playerData += "}";
        return playerData;
    }
}
