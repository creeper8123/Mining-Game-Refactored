public class MainClass {
    public static void main(String[] args) {
        System.out.println("Starting Game...");
        long startTime = System.nanoTime();

        Game game = new Game();

        System.out.println();
        System.out.println("Game Started in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
    }
}
