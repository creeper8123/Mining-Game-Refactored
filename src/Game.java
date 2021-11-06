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
    /**The seed of the world, and all world related generation.*/public static final long WORLD_RANDOM_SEED = new Random().nextLong();
    /**The random number generator used for terrain and terrain texture generation.*/public static final Random WORLD_RANDOM = new Random(WORLD_RANDOM_SEED);
    /**The frame that the game is in.*/public static JFrame frame = new JFrame();
    /**The layered pane that the textures are shown on, and that separates the layers into manageable sections.*/public static JLayeredPane layeredPane = new JLayeredPane();
    /**The timer that the game ticks are looped in.*/public Timer mainGameLoop;
    /**The 2D-list of tiles that make up the world.*/public static Tiles.Tile[][] tiles;
    /**The list of chunks that store the textures and handle tile breaking.*/public static Tiles.TileGraphics[] chunks;
    /**A copy of tiles used to generate the background textures.*/public static Tiles.Tile[][] backgroundTiles;
    /**The size of the screen.*/protected static Dimension screenSize;
    /**The list of currently active moving objects to update each game tick. The Player will always be the 1st object in the ArrayList.*/public static ArrayList<MovingObject> movingObjects = new ArrayList<>();
    /**The number of tiles to randomly select per tick. The same tile can be selected multiple times.*/private static final int RANDOM_SELECTIONS_PER_CHUNK_PER_UPDATE = 1;
    /**The current iteration that the game is on. Updated as the final step of each tick*/public static long updateNum = 0;
    /**The flag to save all the chunk textures to a .png file.*/public final boolean SAVE_MAP_AS_PHOTO = false;
    /**The task that defines what happens in a game tick.*/public static TimerTask mainGameLoopTask = new TimerTask() {
        @Override
        public void run() {
            /*------------------------------------BEGIN MAIN LOOP------------------------------------*/
            for (int i = 0; i < movingObjects.size(); i++) {
                int oldSize = movingObjects.size();
                movingObjects.get(i).onUpdate(); //Do not replace with enhanced loop, deconstruction will throw a ConcurrentModificationExceptions
                if(movingObjects.size() == oldSize-1){
                    i--;
                }
            }

            for (int x = 0; x < chunks.length; x++) {
                if(chunks[x] != null && chunks[x].yValues.size()>0){
                    chunks[x].modifyChunk(x, chunks[x].yValues);
                    for (int i = 0; i < chunks[x].yValues.size(); i++) {
                        chunks[x].yValues.remove(i);
                    }
                }
            }

            for (int x = 0; x < tiles.length; x++) {
                for (int i = 0; i < RANDOM_SELECTIONS_PER_CHUNK_PER_UPDATE; i++) {
                    int y = (int) Math.floor(Math.random() * tiles[x].length);
                    tiles[x][y].onRandomUpdate(tiles, x, y);
                }
            }

            updateNum++;
            /*-------------------------------------END MAIN LOOP-------------------------------------*/
        }
    };

    /**
     * The method to start the game&#46; First the map is generated, then individual tile textures are generated, then the game starts&#46; After the game starts, chunks are created, their textures are stitched, and they are inserted into the world.
     */
    public Game(){
        tiles = new Tiles.Tile[256][256]; //[# of chunks][length of each chunk]
        backgroundTiles = new Tiles.Tile[tiles.length][tiles[0].length];

        frame.setTitle("the game is loading lol"); //Best loading message.
        frame.setIconImage(ImageProcessing.getImageFromResources("textures/missingTexture.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        frame.setLayout(null);
        frame.getContentPane().setBackground(ImageProcessing.SKY_COLOR);
        screenSize = frame.getSize();
        frame.getContentPane().add(layeredPane);
        layeredPane.setLocation(0, 0);
        layeredPane.setSize(Tiles.TILE_WIDTH * tiles.length, tiles[0].length * Tiles.TILE_HEIGHT);//(tiles.length * Tiles.TILE_WIDTH)
        layeredPane.setLayout(null);
        layeredPane.setVisible(true);

        frame.setVisible(true);

        Tiles.WorldGeneration.OverworldGeneration owg = new Tiles.WorldGeneration.OverworldGeneration(){
            @Override
            public Tiles.Tile[][] generate(Tiles.Tile[][] tiles, String levelName) {
                System.out.println();
                System.out.println("Beginning " + levelName + " map generation...");
                long startTime = System.nanoTime();
                tiles = generateOres(generateBase(tiles));
                for (int i = 0; i < tiles.length; i++) {
                    backgroundTiles[i] = Arrays.copyOf(tiles[i], tiles[i].length);
                }
                tiles = generateDetails(generatePortals(generateWorldFloor(generateCavities(tiles))));
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

        /***/
        int spawnChunksLength = 0;
        chunks = new Tiles.TileGraphics[tiles.length];

        int playerSpawnX = 0;
        for (int y = 0; y < tiles[playerSpawnX].length; y++) {
            if(tiles[playerSpawnX][y].itemID != ItemID.TILE_AIR && tiles[playerSpawnX][y].itemID != ItemID.TILE_AIR){
                movingObjects.add(new Player(playerSpawnX * Tiles.TILE_WIDTH, (y * Tiles.TILE_HEIGHT)-Player.PLAYER_HEIGHT));
                break;
            }
        }

        frame.setTitle("add title here"); //TODO: Come up with, and add, a proper title.

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
                chunks[i].redrawChunk(ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(chunks[i].texture), 4));
            }
            System.out.println("100.000% (" + chunks.length + "/" + chunks.length + ")");
            System.out.println("Chunk texture stitching complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime1)*0.000001) + " Milliseconds");
            if(SAVE_MAP_AS_PHOTO){
                System.out.println();
                System.out.println("Saving textures as photo...");
                saveChunkTexturesAsImage("output\\World Maps\\" + WORLD_RANDOM_SEED, chunks);
            }
        });
        gameBackgroundGeneration.start();

        mainGameLoop = new Timer();
        mainGameLoop.scheduleAtFixedRate(mainGameLoopTask, 0, MILLISECONDS_PER_UPDATE);
    }

    /**
     * Moves the camera to a specific position, setting it in the middle of the given width and height, while keeping it within the bounds of the JLayeredPane screen.
     * @param x The x position of the object that the camera will follow.
     * @param y The y position of the object that the camera will follow.
     * @param width The width the object that the camera will follow.
     * @param height The width the object that the camera will follow.
     */
    public static void moveCamera(double x, double y, int width, int height){
        int paneNewX = ((int) -Math.round(x)) + ((frame.getWidth() - width)/2);
        int paneNewY = ((int) -Math.round(y)) + ((frame.getHeight() - height)/2);

        if(paneNewX > 0){
            paneNewX = 0;
        }else if(paneNewX < frame.getWidth() - layeredPane.getWidth() - (width/2)){
            paneNewX = frame.getWidth() - layeredPane.getWidth() - (width/2);
        }

        if(paneNewY > 0){
            paneNewY = 0;
        }else if(paneNewY < frame.getHeight() - layeredPane.getHeight() - (height/2)){
            paneNewY = frame.getHeight() - layeredPane.getHeight() - (height/2);
        }

        layeredPane.setLocation(paneNewX, paneNewY);
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
}
