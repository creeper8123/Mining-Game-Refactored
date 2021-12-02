import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.*;

/**
 * Contains info about all vital game objects, all loaded tiles, and all loaded entities.
 */
public class Game{
    /**The number of milliseconds between ticks.*/public static final long MILLISECONDS_PER_UPDATE = 16;
    /**The seed of the world, and all world related generation.*/public static final long WORLD_RANDOM_SEED = new Random().nextLong();//1537182761089763510L;//
    /**The random number generator used for terrain and terrain texture generation.*/public static final Random WORLD_RANDOM = new Random(WORLD_RANDOM_SEED);
    public static final Random TICK_RANDOM = new Random();
    public static final Random MOVING_OBJECT_RANDOM = new Random();
    /**The frame that the game is in.*/public static JFrame frame = new JFrame();
    /**The layered pane that the textures are shown on, and that separates the layers into manageable sections.*/public static JLayeredPane layeredPane = new JLayeredPane();
    /**The timer that the game ticks are looped in.*/public static Timer mainGameLoop;
    /**The 2D-list of tiles that make up the world.*/public static Tiles.Tile[][] tiles;
    /**The list of chunks that store the textures and handle tile breaking.*/public static Tiles.TileGraphics[] chunks;
    /**A copy of tiles used to generate the background textures.*/public static Tiles.Tile[][] backgroundTiles;
    /**The size of the screen.*/protected static Dimension screenSize;
    /***/public static int screenX;
    /***/public static int screenY;
    /**The list of currently active entities to update each game tick. The Player will always be the 1st object in the ArrayList.*/public static ArrayList<MovingObject> livingEntities = new ArrayList<>();
    /**The list of currently active particles to update each game tick. The Player will always be the 1st object in the ArrayList.*/public static ArrayList<MovingObject> livingParticles = new ArrayList<>();
    /**The list of currently active projectiles to update each game tick. The Player will always be the 1st object in the ArrayList.*/public static ArrayList<MovingObject> livingProjectiles = new ArrayList<>();
    /***/public static Player player;
    /**The number of tiles to randomly select per tick. The same tile can be selected multiple times.*/private static final int RANDOM_SELECTIONS_PER_CHUNK_PER_UPDATE = 1;
    /**The current iteration that the game is on. Updated as the final step of each tick*/public static long updateNum = 0;
    /**The flag to save all the chunk textures to a .png file.*/public static final boolean SAVE_MAP_AS_PHOTO = false;
    public static long startTime = 0;
    /**The task that defines what happens in a game tick.*/public static TimerTask mainGameLoopTask = new TimerTask() {
        @Override
        public void run() {
            /*------------------------------------BEGIN MAIN LOOP------------------------------------*/
            final long OVERLOAD_LIMIT = MILLISECONDS_PER_UPDATE * 10;
            if(frame.hasFocus()){
                //If system is overloaded, kill all particles
                if(System.currentTimeMillis() - startTime > OVERLOAD_LIMIT && updateNum != 0){
                    System.err.println("Project critically overloaded! " + ((System.currentTimeMillis() - startTime) - OVERLOAD_LIMIT) + " milliseconds past limit! Current limit: " + OVERLOAD_LIMIT + " Current Target: " + MILLISECONDS_PER_UPDATE);
                    for (int i = livingParticles.size() - 1 ; i >= 0 ; i--) {
                        livingParticles.get(i).deconstruct(true);
                    }
                }
                startTime = System.currentTimeMillis();

                player.onUpdate();

                //Updates for living entities
                for (int i = 0 ; i < livingEntities.size(); i++) {
                    int oldSize = livingEntities.size();
                    livingEntities.get(i).onUpdate(); //Do not replace with enhanced loop, deconstruction will throw a ConcurrentModificationExceptions
                    if(livingEntities.size() == oldSize-1){
                        i--;
                    }
                }

                //Updates for living projectiles
                for (int i = 0 ; i < livingProjectiles.size(); i++) {
                    int oldSize = livingProjectiles.size();
                    livingProjectiles.get(i).onUpdate(); //Do not replace with enhanced loop, deconstruction will throw a ConcurrentModificationExceptions
                    if(livingProjectiles.size() == oldSize-1){
                        i--;
                    }
                }

                //Updates for living particles
                for (int i = 0 ; i < livingParticles.size(); i++) {
                    int oldSize = livingParticles.size();
                    livingParticles.get(i).onUpdate(); //Do not replace with enhanced loop, deconstruction will throw a ConcurrentModificationExceptions
                    if(livingParticles.size() == oldSize-1){
                        i--;
                    }
                }

                //TODO: Move texture updates to a separate thread. Note: This causes texture artifacts
                for (int x = 0; x < chunks.length; x++) {
                    if(chunks[x] != null && chunks[x].yValues.size()>0){
                        chunks[x].modifyChunk(x, chunks[x].yValues);
                        for (int i = 0; i < chunks[x].yValues.size(); i++) {
                            chunks[x].yValues.remove(i);
                        }
                    }
                }

                //TODO: Restore random update functionality after testing.
                for (int x = 0; x < tiles.length; x++) {
                    for (int i = 0; i < RANDOM_SELECTIONS_PER_CHUNK_PER_UPDATE; i++) {
                        int y = (int) Math.floor(TICK_RANDOM.nextDouble() * tiles[x].length);
                        tiles[x][y].onRandomUpdate(tiles, x, y, false);
                    }
                }
            }else{
                startTime = System.currentTimeMillis();
            }

            updateNum++;
            /*--------------------------------------END MAIN LOOP--------------------------------------*/
        }
    };

    /**
     * The method to start the game&#46; First the map is generated, then individual tile textures are generated, then the game starts&#46; After the game starts, chunks are created, their textures are stitched, and they are inserted into the world.
     */
    public Game(){
        //TODO: Have multiple different levels, and re-call the texture generation methods on the fly for each separate level. A loading screen will be necessary.
        tiles = new Tiles.Tile[256][256]; //[# of chunks][height of each chunk], limit is 100,000 tiles per level.
        backgroundTiles = new Tiles.Tile[tiles.length][tiles[0].length];

        frame.setTitle("the game is loading lol"); //Best loading message.
        frame.setIconImage(ImageProcessing.getImageFromResources("textures/missingTexture.png")); //TODO: Create and implement a proper icon.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        frame.setLayout(null);
        frame.getContentPane().setBackground(ImageProcessing.SKY_COLOR);
        screenSize = frame.getSize();
        frame.getContentPane().add(layeredPane);

        layeredPane.setLocation(0, 0);
        layeredPane.setSize(Tiles.TILE_WIDTH * tiles.length, tiles[0].length * Tiles.TILE_HEIGHT);
        layeredPane.setLayout(null);
        layeredPane.setVisible(true);

        frame.setVisible(true);

        System.out.println();
        System.out.println("World Seed: " + WORLD_RANDOM_SEED + "L");

        Tiles.WorldGeneration.OverworldGeneration owg = new Tiles.WorldGeneration.OverworldGeneration(){
            @Override
            public Tiles.Tile[][] generate(Tiles.Tile[][] tiles, String levelName) {
                System.out.println();
                System.out.println("Beginning " + levelName + " map generation...");
                long startTime = System.nanoTime();
                tiles = generateBase(tiles);
                for (int i = 0; i < tiles.length; i++) {
                    backgroundTiles[i] = Arrays.copyOf(tiles[i], tiles[i].length);
                }
                for (int x = 0; x < backgroundTiles.length; x++) {
                    for (int y = 0; y < backgroundTiles[x].length; y++) {
                        if(backgroundTiles[x][y].itemID != ItemID.TILE_STONE){
                            backgroundTiles[x][y] = Tiles.Tile.TilePresets.getTilePreset(x * Tiles.TILE_WIDTH, y * Tiles.TILE_HEIGHT, Tiles.TILE_WIDTH, Tiles.TILE_HEIGHT, ItemID.TILE_AIR);
                        }
                    }
                }
                tiles = generateDetails(generatePortals(generateWorldFloor(generateCavities(generateOres(tiles)))));
                System.out.println();
                System.out.println();
                System.out.println("Map generation complete");
                System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
                return tiles;
            }
        };
        tiles = owg.generate(tiles, "Overworld");

        System.out.println();
        System.out.println("Beginning Tile texture generation...");
        long startTime = System.nanoTime();
        for (int x = 0; x < tiles.length; x++) {
            System.out.printf("%07.3f%% (%0" + String.valueOf(tiles.length * tiles[0].length).length() + "d/%0" + String.valueOf(tiles.length * tiles[0].length).length() + "d)\n", ((double) (x*100)/ tiles.length), x * tiles[0].length, tiles.length * tiles[0].length); //Don't move to y loop, will double time to generate textures due to printing
            for (int y = 0; y < tiles[x].length; y++) {
                tiles[x][y].generateTileTextures();
                backgroundTiles[x][y].generateTileTextures();
            }
        }
        System.out.println("100.000% (" + tiles.length * tiles[0].length + "/" + tiles.length * tiles[0].length + ")");
        System.out.println("Tile texture generation complete");
        System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
        chunks = new Tiles.TileGraphics[tiles.length];

        int playerSpawnX = 0;
        for (int y = 0; y < tiles[playerSpawnX].length; y++) {
            if(tiles[playerSpawnX][y].itemID != ItemID.TILE_AIR && tiles[playerSpawnX][y].itemID != ItemID.TILE_AIR){
                player = new Player(playerSpawnX * Tiles.TILE_WIDTH, (y * Tiles.TILE_HEIGHT)-Player.PLAYER_HEIGHT);
                break;
            }
        }

        frame.setTitle("Underminer"); //TODO: Come up with, and add, a proper title.

        startUpdating();
    }

    /**
     * Moves the camera to a specific position, setting it in the middle of the given width and height, while keeping it within the bounds of the JLayeredPane screen.
     * @param x The x position of the object that the camera will follow.
     * @param y The y position of the object that the camera will follow.
     * @param width The width the object that the camera will follow.
     * @param height The width the object that the camera will follow.
     */
    public static void moveCamera(double x, double y, int width, int height){
        screenX = ((int) -Math.round(x)) + ((frame.getWidth() - width)/2);
        screenY = ((int) -Math.round(y)) + ((frame.getHeight() - height)/2);

        if(screenX > 0){
            screenX = 0;
        }else if(screenX < frame.getWidth() - layeredPane.getWidth() - (width/2)){
            screenX = frame.getWidth() - layeredPane.getWidth() - (width/2);
        }

        if(screenY > 0){
            screenY = 0;
        }else if(screenY < frame.getHeight() - layeredPane.getHeight() - (height/2)){
            screenY = frame.getHeight() - layeredPane.getHeight() - (height/2);
        }

        layeredPane.setLocation(screenX, screenY);
    }

    /**
     * Saves the textures of the chunks to a png at the specified filepath.
     * @param filepath The location of the file to save the finalized photo to. The ".png" is added at the end, regardless of if it is in the original filepath string
     * @param chunks The chunk objects that make up the world, although only the textures are used. These will have their "texture" variables used to create a single .png of all the textures.
     */
    private static void saveChunkTexturesAsImage(String filepath, Tiles.TileGraphics[] chunks){
        BufferedImage worldTexture = new BufferedImage(Tiles.TILE_BASE_WIDTH * tiles.length, Tiles.TILE_BASE_HEIGHT * tiles[0].length, 6);
        for (int i = 0; i < chunks.length; i++) {
            BufferedImage workingImage = chunks[i].texture;
            for (int x = 0; x < workingImage.getWidth(); x++) {
                for (int y = 0; y < workingImage.getHeight(); y++) {
                    worldTexture.setRGB(x + (i * workingImage.getWidth()), y, workingImage.getRGB(x, y));
                }
            }
        }
        try{
            ImageIO.write(worldTexture, "png", new File(filepath + ".png"));
            System.out.println("File saved!");
            System.out.println("File Saved to " + filepath + ".png");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void startUpdating(){
        Game.mainGameLoop = new Timer();
        Game.mainGameLoop.scheduleAtFixedRate(mainGameLoopTask, 0, MILLISECONDS_PER_UPDATE);


        Thread gameBackgroundGeneration = new Thread(() -> {
            System.out.println();
            System.out.println("Beginning chunk texture stitching...");
            long startTime1 = System.nanoTime();
            for (int i = 0; i < chunks.length; i++) {
                System.out.printf("%07.3f%% (%0" + String.valueOf(chunks.length).length() + "d/%0" + String.valueOf(chunks.length).length() + "d)\n", ((double) (i*100)/chunks.length), i, chunks.length);
                chunks[i] = new Tiles.TileGraphics();
                chunks[i].stitchBackgroundTexture(backgroundTiles[i]);
                chunks[i].stitchTexture(tiles[i]);
                chunks[i].textureLabel.setLocation(i* Tiles.TILE_WIDTH, 0);
                chunks[i].redrawChunk(ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(chunks[i].texture), 4, 4));
            }
            System.out.println("100.000% (" + chunks.length + "/" + chunks.length + ")");
            System.out.println("Chunk texture stitching complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime1)*0.000001) + " Milliseconds");
            if(Game.SAVE_MAP_AS_PHOTO){
                System.out.println();
                System.out.println("Saving textures as photo...");
                saveChunkTexturesAsImage("output\\World Maps\\" + WORLD_RANDOM_SEED, chunks);
            }
        });
        gameBackgroundGeneration.start();
    }
}
