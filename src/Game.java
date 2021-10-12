import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game{
    public static final long MILLISECONDS_PER_UPDATE = 16;
    public static final Random WORLD_RANDOM = new Random();
    public static JFrame frame = new JFrame();
    public static JLayeredPane layeredPane = new JLayeredPane();
    public Timer mainGameLoop;
    public static Tiles.Tile[][] tiles;
    public static Tiles.TileGraphics[] chunks;
    public static BufferedImage[] chunkTextures;
    protected static Dimension screenSize;
    public static ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private final int spawnChunksLength;
    private final int randomSelectionsPerChunkPerUpdate = 3;

    public Game(){
        tiles = new Tiles.Tile[256][256];

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

        Tiles.WorldGeneration.OverworldGeneration owg = new Tiles.WorldGeneration.OverworldGeneration();
        tiles = owg.generate(tiles, "Overworld");

        System.out.println();
        System.out.println("Beginning Tile texture generation...");
        long startTime = System.nanoTime();
        for (int x = 0; x < tiles.length; x++) {
            System.out.printf("%07.3f%% (%0" + String.valueOf(tiles.length * tiles[0].length).length() + "d/%0" + String.valueOf(tiles.length * tiles[0].length).length() + "d)\n", ((double) (x*100)/ tiles.length), x * tiles[0].length, tiles.length * tiles[0].length); //Don't move to y loop, will double time to generate textures due to printing
            for (int y = 0; y < tiles[x].length; y++) {
                tiles[x][y].generateTileTextures();
            }
        }
        System.out.println("100.000% (" + tiles.length * tiles[0].length + "/" + tiles.length * tiles[0].length + ")");
        System.out.println("Tile texture generation complete");
        System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");

        spawnChunksLength = 0;
        chunks = new Tiles.TileGraphics[tiles.length];
        chunkTextures = new BufferedImage[tiles.length];

        int playerSpawnX = 0;
        for (int y = 0; y < tiles[playerSpawnX].length; y++) {
            if(!tiles[playerSpawnX][y].isBroken && tiles[playerSpawnX][y].itemID != ItemID.TILE_AIR){
                movingObjects.add(new Player(playerSpawnX * Tiles.TILE_WIDTH, (y * Tiles.TILE_HEIGHT)-Player.PLAYER_HEIGHT));
                break;
            }
        }

        Thread generateOtherTextures = new Thread(() -> {
            try {
                Thread.sleep(1); //Done exclusively to print the console in the correct order >:)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
            System.out.println("Beginning chunk texture stitching...");
            long startTime1 = System.nanoTime();
            for (int i = spawnChunksLength; i < chunkTextures.length; i++) {
                System.out.printf("%07.3f%% (%0" + String.valueOf(chunkTextures.length).length() + "d/%0" + String.valueOf(chunkTextures.length).length() + "d)\n", ((double) ((i - spawnChunksLength)*100)/(chunkTextures.length - spawnChunksLength)), i, chunkTextures.length);
                chunks[i] = new Tiles.TileGraphics();
                chunks[i].textureLabel.setLocation(i* Tiles.TILE_WIDTH, 0);
                chunkTextures[i] = ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(chunks[i].stitchChunk(i)), 4);
                chunks[i].redrawChunk(chunkTextures[i]);
            }
            System.out.println("100.000% (" + chunkTextures.length + "/" + chunkTextures.length + ")");
            System.out.println("Chunk texture stitching complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime1)*0.000001) + " Milliseconds");
        });
        generateOtherTextures.start();

        mainGameLoop = new Timer();
        mainGameLoop.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                /*------------------------------------BEGIN MAIN LOOP------------------------------------*/
                for (int i = 0; i < movingObjects.size(); i++) {
                    movingObjects.get(i).onUpdate();
                }
                for (int x = 0; x < tiles.length; x++) {
                    for (int i = 0; i < randomSelectionsPerChunkPerUpdate; i++) {
                        int y = (int) Math.floor(Math.random() * tiles[x].length);
                        tiles[x][y].onRandomUpdate(tiles, x, y);
                    }
                }
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
}
