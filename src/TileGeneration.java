public interface TileGeneration {


    Tiles.Tile[][] generateBase(Tiles.Tile[][] tiles);


    Tiles.Tile[][] generateOres(Tiles.Tile[][] tiles);


    Tiles.Tile[][] generateCavities(Tiles.Tile[][] tiles);


    Tiles.Tile[][] generateWorldFloor(Tiles.Tile[][] tiles);


    Tiles.Tile[][] generatePortals(Tiles.Tile[][] tiles);


    Tiles.Tile[][] generateDetails(Tiles.Tile[][] tiles);



    default Tiles.Tile[][] generate(Tiles.Tile[][] tiles, String levelName){
        System.out.println();
        System.out.println("Beginning " + levelName + " map generation...");
        long startTime = System.nanoTime();
        tiles = generatePortals(generateWorldFloor(generateCavities(generateOres(generateBase(tiles)))));
        System.out.println();
        System.out.println("Map generation complete");
        System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
        return tiles;
    }
}
