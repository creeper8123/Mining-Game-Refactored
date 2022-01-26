package AllClasses;

/**
 * The class used to run the game.
 */
public class MainClass {
    /**
     * Main method.
     * @param args Input is ignored.
     */
    public static void main(String[] args) {
        System.out.println("Starting AllClasses.Game...");
        long startTime = System.nanoTime();

        Game game = new Game();

        System.out.println();
        System.out.println("AllClasses.Game Started in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
    }
}
