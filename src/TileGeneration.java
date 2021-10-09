public interface TileGeneration {
    Tiles.Tile[][] generateBase(Tiles.Tile[][] tiles);
    Tiles.Tile[][] generateOres(Tiles.Tile[][] tiles);
    Tiles.Tile[][] generateCavities(Tiles.Tile[][] tiles);
    Tiles.Tile[][] generateWorldFloor(Tiles.Tile[][] tiles);
    Tiles.Tile[][] generatePortals(Tiles.Tile[][] tiles);
    Tiles.Tile[][] generateDetails(Tiles.Tile[][] tiles);

    default Tiles.Tile[][] generate(Tiles.Tile[][] tiles){
        System.out.println();
        System.out.println("Beginning map generation...");
        long startTime = System.nanoTime();
        tiles = generateBase(tiles);
        tiles = generateOres(tiles);
        tiles = generateCavities(tiles);
        tiles = generateWorldFloor(tiles);
        tiles = generatePortals(tiles);
        tiles = generateDetails(tiles);
        System.out.println();
        System.out.println("Map generation complete");
        System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
        return tiles;
    }
}