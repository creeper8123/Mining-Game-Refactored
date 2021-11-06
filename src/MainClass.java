/**
 * The class used to run the game.
 */
public class MainClass {
    /**
     * Main method.
     * @param args Input is ignored.
     */
    public static void main(String[] args) {
        System.out.println("Starting Game...");
        long startTime = System.nanoTime();

        Game game = new Game();

        System.out.println();
        System.out.println("Game Started in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
    }
}
