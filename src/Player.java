import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Player extends MovingObject implements KeyListener, MouseListener {
    /***/public static final int PLAYER_WIDTH = 32;
    /***/public static final int PLAYER_HEIGHT = 48;
    /***/public static final double PLAYER_REACH = 256;


    public Player(double initialX, double initialY){
        super(initialX, initialY, 0, PLAYER_WIDTH, PLAYER_HEIGHT, "textures/missingTexture.png");
    }


    @Override
    public void postInitialization(){
        Game.frame.addKeyListener(this);
        Game.layeredPane.addMouseListener(this);
        this.nonSolidTiles = new ArrayList<>(List.of(ItemID.TILE_AIR, ItemID.TILE_LOG, ItemID.TILE_LEAVES, ItemID.TILE_LEAFY_LOG, ItemID.TILE_TREE_STARTER));
        this.inventory = new InventoryManager(15) {
            @Override
            public int addToInventory(HoldableObject holdableObject, int quantity) {
                int result = super.addToInventory(holdableObject, quantity);
                inventoryMenu.configureMenuContents();
                return result;
            }

            @Override
            public int addToInventory(HoldableObject holdableObject, int quantity, int i) {
                int result = super.addToInventory(holdableObject, quantity, i);
                inventoryMenu.configureMenuContents();
                return result;
            }

            @Override
            public int removeFromInventory(HoldableObject holdableObject, int quantity) {
                int result = super.removeFromInventory(holdableObject, quantity);
                inventoryMenu.configureMenuContents();
                return result;
            }

            @Override
            public int removeFromInventory(HoldableObject holdableObject, int quantity, int i){
                int result = super.removeFromInventory(holdableObject, quantity, i);
                inventoryMenu.configureMenuContents();
                return result;
            }

            @Override
            public void sortInventory(){
                super.sortInventory();
                inventoryMenu.configureMenuContents();
            }
        };
        this.inventoryMenu = new MenuDisplay<>(){
            @Override
            public void configureMenuContents(){
                this.menuDisplayContents = new String[menuContents.length];
                for (int i = 0; i < this.menuDisplayContents.length; i++) {
                    if(i == selectedItem){
                        if(this.menuContents[i] == null || this.menuContents[i].holdableObject == null || this.menuContents[i].holdableObject.itemID == null){
                            this.menuDisplayContents[i] = "&gt[Empty]&lt";
                        }else{
                            this.menuDisplayContents[i] = "&gt[" + this.menuContents[i].holdableObject.displayName + " *" + this.menuContents[i].quantity + "]&lt";
                        }
                    }else{
                        if(this.menuContents[i] == null || this.menuContents[i].holdableObject == null || this.menuContents[i].holdableObject.itemID == null){
                            this.menuDisplayContents[i] = "&#8194[Empty]&#8194";
                        }else{
                            this.menuDisplayContents[i] = "&#8194[" + this.menuContents[i].holdableObject.displayName + " *" + this.menuContents[i].quantity + "]&#8194";
                        }
                    }
                }
                this.fullMenuDisplayContents = "<html><body><u>" + menuTitle + "</u>";
                for (String menuDisplayContent : this.menuDisplayContents)
                    this.fullMenuDisplayContents += "<br>" + menuDisplayContent;
                this.fullMenuDisplayContents += "<br/><br/><br/>";
                this.fullMenuDisplayContents += "</body></html>";
                this.menu.setText(fullMenuDisplayContents);
            }
        };
        this.inventoryMenu.setMenuTitle("---Player Inventory---");
        inventoryMenu.setMenuContents(inventory.getInventory(), 210);
        inventoryMenu.setMenuSize(inventoryMenu.menuWidth, inventoryMenu.menuHeight + 30);
        inventoryMenu.addButton(0, inventoryMenu.menuHeight + 10, 20, inventoryMenu.menuWidth, "Delete Selected Item", () -> {
            if(inventoryMenu.selectedItem >= 0){
                if(inventory.getInventory() != null){
                    inventory.setInventorySlot(inventoryMenu.selectedItem, null);
                    inventoryMenu.configureMenuContents();
                }
            }
        });
        inventoryMenu.configureMenuContents();
        inventoryMenu.setVisibility(true);
    }

    /***/boolean upPressed;
    /***/boolean downPressed;
    /***/boolean leftPressed;
    /***/boolean rightPressed;
    /***/boolean sprintPressed;

    /***/MenuDisplay<ItemStack> inventoryMenu;
    /***/MenuDisplay<ItemStack> altMenu;

    @Override
    public void onUpdate() {
        this.calculateNewPosition();
        Game.moveCamera(x, y, width, height);
        inventoryMenu.setMenuPosition(-Game.screenX, -Game.screenY);
        //this.textureLabel.setIcon(new ImageIcon(ImageProcessing.resizeImage(ImageProcessing.rotateImage(ImageProcessing.imageToBufferedImage(ImageProcessing.getImageFromResources("textures/missingTexture.png")), (int) (Game.MOVING_OBJECT_RANDOM.nextDouble() * 4)), 4, 4)));
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


    void boundsCheck() {
        if (x < 0) {
            x = 0;
            hitbox.x = 0;
            xSpeed = 0;
        } else if (x > Game.layeredPane.getWidth() - width) {
            int frameWidth = Game.layeredPane.getWidth() - width;
            x = frameWidth;
            hitbox.x = frameWidth;
            xSpeed = 0;
        }

        if (y < 0) {
            y = 0;
            hitbox.y = 0;
            ySpeed = 0;
        } else if (y > Game.layeredPane.getHeight() - height) {
            int frameHeight = Game.layeredPane.getHeight() - height;
            y = frameHeight;
            hitbox.y = frameHeight;
            ySpeed = 0;
        }
    }


    @Override
    public void calculateNewPosition(){
        setTargetSpeed();
        applySpeedDeadzone();
        applyGravity();
        collisionDetection();
        checkOnGround();
        applyJumpForce();
        boundsCheck();
        confirmPosition();
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
            case 84 -> Game.livingParticles.add(new Particle(x, y, 0, 16, 16, "textures/missingTexture.png", (Game.MOVING_OBJECT_RANDOM.nextDouble()*10)-5, (Game.MOVING_OBJECT_RANDOM.nextDouble()*10)-5, true, 2, true, 0.1));
        }
        //System.out.println(e.getKeyChar() + " (" + e.getKeyCode() + ")" + " pressed");
    }


    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case 16, 17 -> this.sprintPressed = false;
            case 37, 65 -> this.leftPressed = false;
            case 38, 87, 32 -> this.upPressed = false;
            case 39, 68 -> this.rightPressed = false;
            case 40, 83 -> this.downPressed = false;
            case 69 -> this.inventoryMenu.toggleVisibility();
        }
        //System.out.println(e.getKeyChar() + " (" + e.getKeyCode() + ")" + " released");
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int tileX = x/Tiles.TILE_WIDTH;
        int tileY = y/Tiles.TILE_HEIGHT;

        int tileMiddleX = (tileX * Tiles.TILE_WIDTH) + (Tiles.TILE_WIDTH/2);
        int tileMiddleY = (tileY * Tiles.TILE_HEIGHT) + (Tiles.TILE_HEIGHT/2);
        int playerMiddleX = (int) this.x + (PLAYER_WIDTH/2);
        int playerMiddleY = (int) this.y + (PLAYER_HEIGHT/2);
        if(Math.sqrt(Math.pow(tileMiddleX-playerMiddleX, 2)+Math.pow(tileMiddleY-playerMiddleY, 2)) < PLAYER_REACH){
            if(e.getButton() == 1){
                if(Game.tiles[tileX][tileY].canBeBroken){
                    Game.tiles[tileX][tileY].whenBroken(Game.tiles, tileX, tileY, this);
                }
            }else if(e.getButton() == 3){
                if(this.inventoryMenu.selectedItem != -1 && !Game.tiles[tileX][tileY].whenUsed(Game.tiles, tileX, tileY)){
                    if(this.inventory.getInventorySlot(this.inventoryMenu.selectedItem) != null && this.inventory.getInventorySlot(this.inventoryMenu.selectedItem).holdableObject != null && this.inventory.getInventorySlot(this.inventoryMenu.selectedItem).holdableObject.itemID != null && this.inventory.getInventorySlot(this.inventoryMenu.selectedItem).quantity > 0){
                        this.inventory.getInventorySlot(this.inventoryMenu.selectedItem).holdableObject.whenUsed(x, y, this);
                        //Tiles.Tile.placeTile(tileX, tileY, this.inventory.getInventorySlot(this.inventoryMenu.selectedItem).holdableObject.itemID, Game.player);
                    }
                }else{
                    System.out.println("Used tile at x" + tileX + " y" + tileY);
                }
                //TODO: Disable manual random updates after testing.
            }else if(e.getButton() == 2){
                Game.tiles[tileX][tileY].onRandomUpdate(Game.tiles, tileX, tileY, true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
