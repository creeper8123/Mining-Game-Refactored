import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.*;

public class Game{
    public static final long MILLISECONDS_PER_UPDATE = 16;
    public static final long WORLD_RANDOM_SEED = new Random().nextLong();
    public static final Random WORLD_RANDOM = new Random(WORLD_RANDOM_SEED);
    public static JFrame frame = new JFrame();
    public static JLayeredPane layeredPane = new JLayeredPane();
    public Timer mainGameLoop;
    public static Tiles.Tile[][] tiles;
    public static Tiles.TileGraphics[] chunks;
    public static Tiles.Tile[][] backgroundTiles;
    protected static Dimension screenSize;
    public static ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private final int spawnChunksLength;
    private final int randomSelectionsPerChunkPerUpdate = 1;
    public long updateNum = 0;

    public final boolean saveMapAsPhoto = false;


    public Game(){
        tiles = new Tiles.Tile[256][256];
        backgroundTiles = new Tiles.Tile[tiles.length][tiles[0].length];

        frame.setTitle("Voxel Game But Better-ish");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(0, 0);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.CYAN);
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

        spawnChunksLength = 0;
        chunks = new Tiles.TileGraphics[tiles.length];

        int playerSpawnX = 0;
        for (int y = 0; y < tiles[playerSpawnX].length; y++) {
            if(tiles[playerSpawnX][y].itemID != ItemID.TILE_AIR && tiles[playerSpawnX][y].itemID != ItemID.TILE_AIR){
                movingObjects.add(new Player(playerSpawnX * Tiles.TILE_WIDTH, (y * Tiles.TILE_HEIGHT)-Player.PLAYER_HEIGHT));
                break;
            }
        }

        Thread gameBackgroundGeneration = new Thread(() -> {
            System.out.println();
            System.out.println("Beginning chunk texture stitching...");
            long startTime1 = System.nanoTime();
            for (int i = spawnChunksLength; i < chunks.length; i++) {
                System.out.printf("%07.3f%% (%0" + String.valueOf(chunks.length).length() + "d/%0" + String.valueOf(chunks.length).length() + "d)\n", ((double) ((i - spawnChunksLength)*100)/(chunks.length - spawnChunksLength)), i, chunks.length);
                chunks[i] = new Tiles.TileGraphics();
                chunks[i].stitchBackgroundTexture(backgroundTiles[i]);
                chunks[i].stitchTexture(tiles[i]);
                chunks[i].textureLabel.setLocation(i* Tiles.TILE_WIDTH, 0);
                chunks[i].redrawChunk(ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(chunks[i].texture), 4));
            }
            System.out.println("100.000% (" + chunks.length + "/" + chunks.length + ")");
            System.out.println("Chunk texture stitching complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime1)*0.000001) + " Milliseconds");
            if(saveMapAsPhoto){
                System.out.println();
                System.out.println("Saving textures as photo...");
                saveChunkTexturesAsImage(chunks);
            }
        });
        gameBackgroundGeneration.start();

        mainGameLoop = new Timer();
        mainGameLoop.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                /*------------------------------------BEGIN MAIN LOOP------------------------------------*/
                for (int i = 0; i < movingObjects.size(); i++) {
                    int oldSize = movingObjects.size();
                    movingObjects.get(i).onUpdate(); //Do not replace with enhanced loop, deconstruction will cause Concurrent Modification Exceptions
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
                    for (int i = 0; i < randomSelectionsPerChunkPerUpdate; i++) {
                        int y = (int) Math.floor(Math.random() * tiles[x].length);
                        tiles[x][y].onRandomUpdate(tiles, x, y);
                    }
                }

                updateNum++;
                /*-------------------------------------END MAIN LOOP-------------------------------------*/
            }
        }, 0, MILLISECONDS_PER_UPDATE);
    }


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

    private static void saveChunkTexturesAsImage(Tiles.TileGraphics[] chunks){
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
            ImageIO.write(worldTexture, "png", new File("output\\World Maps\\" + WORLD_RANDOM_SEED + ".png"));
            System.out.println("File saved!");
            System.out.println("File Saved to " + "output\\World Maps\\" + WORLD_RANDOM_SEED + ".png");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
