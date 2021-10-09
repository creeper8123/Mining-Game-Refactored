import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game{
    public static final long MILLISECONDS_PER_FRAME = 16;
    public static JFrame frame = new JFrame();
    public static JLayeredPane layeredPane = new JLayeredPane();
    public Timer mainGameLoop;
    public static Tiles.Tile[][] tiles;
    public static Tiles.TileGraphics[] chunks;
    public static BufferedImage[] chunkTextures;
    protected static Dimension screenSize;
    public static ArrayList<MovingObject> movingObjects = new ArrayList<>();
    protected static int scrollLevel = 0;

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
        layeredPane.setSize(frame.getSize().width + (5 * Tiles.TILE_WIDTH), tiles[0].length * Tiles.TILE_HEIGHT);//(tiles.length * Tiles.TILE_WIDTH)
        layeredPane.setLayout(null);
        layeredPane.setVisible(true);

        frame.setVisible(true);

        Tiles.WorldGeneration.OverworldGeneration owg = new Tiles.WorldGeneration.OverworldGeneration();
        tiles = owg.generate(tiles);

        System.out.println();
        System.out.println("Beginning Tile texture generation...");
        long startTime = System.nanoTime();
        for (int x = 0; x < tiles.length; x++) {
            System.out.printf("%07.3f%%\n", ((double) (x*100)/ tiles.length));
            for (int y = 0; y < tiles[x].length; y++) {
                tiles[x][y].generateTileTextures();
            }
        }
        System.out.println("100.000%");
        System.out.println("Tile texture generation complete");
        System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");

        chunks = new Tiles.TileGraphics[(layeredPane.getWidth()/Tiles.TILE_WIDTH) + 5];//tiles.length
        System.out.println();
        System.out.println("Beginning spawn chunk texture stitching...");
        startTime = System.nanoTime();
        chunkTextures = new BufferedImage[tiles.length];
        for (int i = 0; i < chunks.length; i++) {
            System.out.printf("%07.3f%%\n", ((double) (i*100)/chunks.length));
            chunks[i] = new Tiles.TileGraphics();
            chunks[i].textureLabel.setLocation(i* Tiles.TILE_WIDTH, 0);
            chunkTextures[i] = ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(chunks[0].stitchChunk(i)), 4);
            chunks[i].redrawChunk(chunkTextures[i]);
        }
        System.out.println("100.000%");
        System.out.println("Spawn chunk texture stitching complete");
        System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");

        movingObjects.add(new Player(0 * Tiles.TILE_WIDTH, 0 * Tiles.TILE_HEIGHT));

        Thread generateOtherTextures = new Thread(() -> {
            try {
                Thread.sleep(0, 1); //Done exclusively to print the console in the correct order >:)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
            System.out.println("Beginning non-spawn chunk texture stitching...");
            long startTime1 = System.nanoTime();
            for (int i = chunks.length; i < chunkTextures.length; i++) {
                System.out.printf("%07.3f%%\n", ((double) ((i - chunks.length)*100)/(chunkTextures.length - chunks.length)));
                chunkTextures[i] = ImageProcessing.resizeImage(ImageProcessing.imageToBufferedImage(chunks[0].stitchChunk(i)), 4);
            }
            System.out.println("100.000%");
            System.out.println("Non-spawn chunk texture stitching complete");
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
                /*------------------------------------END MAIN LOOP------------------------------------*/
            }
        }, 0, MILLISECONDS_PER_FRAME);
    }

    public static void moveCamera(double x, double y, int width, int height){
        /*
        if(x > 700){
            scrollLevel++;
        }else if(x < 500 && scrollLevel > 0){
            scrollLevel--;
        }
         */

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
