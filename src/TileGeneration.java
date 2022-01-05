import java.util.ArrayList;

public interface TileGeneration {
    //TODO: Consider making methods void, passing the parameter in this way will send the references instead of copying the array.

    Tile[][] generateBase(Tile[][] tiles);


    Tile[][] generateOres(Tile[][] tiles);


    Tile[][] generateCavities(Tile[][] tiles);


    Tile[][] generateWorldFloor(Tile[][] tiles);


    Tile[][] generatePortals(Tile[][] tiles);


    Tile[][] generateDetails(Tile[][] tiles);



    default Tile[][] generate(Tile[][] tiles, String levelName){
        System.out.println();
        System.out.println("Beginning " + levelName + " map generation...");
        long startTime = System.nanoTime();
        tiles = generatePortals(generateWorldFloor(generateCavities(generateOres(generateBase(tiles)))));
        System.out.println();
        System.out.println("Map generation complete");
        System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
        return tiles;
    }

    class OverworldGeneration implements TileGeneration {
        /***/private static final int SEA_LEVEL = Game.tiles[0].length/16;

        @Override
        public Tile[][] generateBase(Tile[][] tiles) {
            System.out.println();
            System.out.println("Beginning Overworld base generation...");
            long startTime = System.nanoTime();
            PerlinNoise.Perlin1D perlin1D = new PerlinNoise.Perlin1D(Game.WORLD_RANDOM.nextLong());
            for (int x = 0; x < tiles.length; x++) {
                for (int y = 0; y < tiles[x].length; y++) {
                    int perlinY = (int) Math.round(perlin1D.cosInterpolation((double) x/10)*8)-4;
                    if(y < (SEA_LEVEL + perlinY)){
                        tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_AIR);
                    }else{
                        tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_STONE);
                    }
                }
            }

            //Add grass and dirt to surface
            perlin1D = new PerlinNoise.Perlin1D(Game.WORLD_RANDOM.nextLong());
            for (int x = 0; x < tiles.length; x++) {
                for (int y = 0; y < tiles[x].length; y++) {
                    if(tiles[x][y].itemID == ItemID.TILE_STONE){
                        tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_DIRT_GRASS);
                        for (int z = 1; z < ((perlin1D.cosInterpolation(((double) x/6)) * 4)+1); z++) {
                            tiles[x][y + z] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, (y + z) * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_DIRT);
                        }
                        break;
                    }
                }
            }
            System.out.println("Overworld base generation complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
            return tiles;
        }

        @Override
        public Tile[][] generateOres(Tile[][] tiles) {
            System.out.println();
            System.out.println("Beginning Overworld ore generation...");
            long startTime = System.nanoTime();

            //TODO: Fix ore dispersion at different levels, make only certain ores available at certain z levels.
            //TODO: Use only copper and tin in the overworld.
            //TODO: Add copper and tin ore.

            //To increase # of ore pockets, lower threshold and stretch values
            PerlinNoise.Perlin2D perlin2D;

            //Coal Ore
            //Generates primarily in mid-area of the overworld
            int coalVerticalStretch = 3;
            int coalHorizontalStretch = 7;
            perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
            generateOreVeins(tiles, perlin2D, ItemID.TILE_STONE, ItemID.TILE_COAL_ORE, 1, 0.8, coalVerticalStretch, coalHorizontalStretch, SEA_LEVEL, tiles[0].length/3);
            generateOreVeins(tiles, perlin2D, ItemID.TILE_STONE, ItemID.TILE_COAL_ORE, 0.85, 0.85, coalVerticalStretch, coalHorizontalStretch, tiles[0].length/3, (2*tiles[0].length)/3);
            generateOreVeins(tiles, perlin2D, ItemID.TILE_STONE, ItemID.TILE_COAL_ORE, 0.85, 0.95, coalVerticalStretch, coalHorizontalStretch, (2*tiles[0].length)/3, tiles[0].length);

            //Copper Ore
            //Generates primarily in low area of the overworld. Doesn't generate at all in upper levels. Generates in tall narrow columns.
            int copperVerticalStretch = 6;
            int copperHorizontalStretch = 2;
            perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
            generateOreVeins(tiles, perlin2D,  ItemID.TILE_STONE, ItemID.TILE_COPPER_ORE, 0.925, 0.875, copperVerticalStretch, copperHorizontalStretch, tiles[0].length/2, tiles[0].length);

            //Tin Ore
            //Generates primarily in low area of the overworld. Doesn't generate at all in upper levels. Generates in short wide rows.
            int tinVerticalStretch = 3;
            int tinHorizontalStretch = 7;
            perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
            generateOreVeins(tiles, perlin2D,  ItemID.TILE_STONE, ItemID.TILE_TIN_ORE, 0.95, 0.9, tinVerticalStretch, tinHorizontalStretch, tiles[0].length/2, tiles[0].length);

            System.out.println("Overworld ore generation complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
            return tiles;
        }

        @Override
        public Tile[][] generateCavities(Tile[][] tiles) {
            System.out.println();
            System.out.println("Beginning Overworld cavity generation...");
            long startTime = System.nanoTime();

            //TODO: Make cavities more common/larger higher in the level and less common/smaller lower in the level

            //Air Cavities
            PerlinNoise.Perlin2D perlin2D = new PerlinNoise.Perlin2D(Game.WORLD_RANDOM.nextLong());
            int caveVerticalStretch = 15;
            int caveHorizontalStretch = 5;
            generateCavitySection(tiles, perlin2D, 1, 0.5, caveVerticalStretch, caveHorizontalStretch, SEA_LEVEL, tiles[0].length-45);
            generateCavitySection(tiles, perlin2D, 0.5, 0.75, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-45, tiles[0].length-20);
            generateCavitySection(tiles, perlin2D, 0.75, 0, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-20, tiles[0].length-3);
            generateCavitySection(tiles, perlin2D, 0, 0, caveVerticalStretch, caveHorizontalStretch, tiles[0].length-3, tiles[0].length-1);

            System.out.println("Overworld cavity generation complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
            return tiles;
        }

        @Override
        public Tile[][] generateWorldFloor(Tile[][] tiles) {
            System.out.println();
            System.out.println("Beginning Overworld floor generation...");
            long startTime = System.nanoTime();

            for (int x = 0; x < Game.tiles.length; x++) {
                tiles[x][Game.tiles[x].length-1].canBeBroken = false;
            }

            System.out.println("Overworld floor generation complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
            return tiles;
        }

        @Override
        public Tile[][] generatePortals(Tile[][] tiles) {
            System.out.println();
            System.out.println("Beginning Overworld portal generation...");
            long startTime = System.nanoTime();



            System.out.println("Overworld portal generation complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
            return tiles;
        }

        @Override
        public Tile[][] generateDetails(Tile[][] tiles) {
            System.out.println();
            System.out.println("Beginning Overworld detail generation...");
            long startTime = System.nanoTime();

            final double STALACTITE_CHANCE = 0.1;
            final double STALAGMITE_CHANCE = 0.5;

            final double TREE_CHANCE = 0.1;

            final ArrayList<ItemID> stalactiteRocks = new ArrayList<>();
            stalactiteRocks.add(ItemID.TILE_STONE);
            stalactiteRocks.add(ItemID.TILE_COAL_ORE);
            stalactiteRocks.add(ItemID.TILE_IRON_ORE);
            stalactiteRocks.add(ItemID.TILE_GOLD_ORE);
            stalactiteRocks.add(ItemID.TILE_DIAMOND_ORE);

            for (int x = 0; x < tiles.length; x++) {
                boolean hasStalactite = false;
                for (int y = 0; y < tiles[x].length - 1; y++) {
                    if (stalactiteRocks.contains(tiles[x][y].itemID) && tiles[x][y + 1].itemID == ItemID.TILE_AIR) {
                        if (STALACTITE_CHANCE > Game.WORLD_RANDOM.nextDouble()) {
                            tiles[x][y + 1] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, (y + 1) * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_STALACTITE);
                            hasStalactite = true;
                        }
                    }
                    if (stalactiteRocks.contains(tiles[x][y].itemID) && tiles[x][y].itemID != ItemID.TILE_AIR && tiles[x][y - 1].itemID == ItemID.TILE_AIR && hasStalactite) {
                        if (STALAGMITE_CHANCE > Game.WORLD_RANDOM.nextDouble()) {
                            tiles[x][y - 1] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, (y - 1) * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_STALAGMITE);
                        }
                        hasStalactite = false;
                    }
                }
            }

            for (int x = 5 ; x < tiles.length-5 ; x++) {
                for (int y = 0 ; y < tiles[x].length ; y++) {
                    if(tiles[x][y].itemID == ItemID.TILE_DIRT_GRASS && y > 0 && tiles[x][y-1].itemID == ItemID.TILE_AIR){
                        if(Game.WORLD_RANDOM.nextDouble() < TREE_CHANCE){
                            tiles[x][y - 1] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, (y - 1) * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_TREE_STARTER);
                            tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_DIRT);
                            for (int i = 0 ; i < (Game.WORLD_RANDOM.nextDouble() * 4) + 4 ; i++) {
                                tiles[x][y-1].onRandomUpdate(tiles, x, y-1, true);
                            }
                            x += 4;
                            break;
                        }
                    }
                }
            }
            System.out.println("Overworld detail generation complete");
            System.out.println("Completed in " + (int) ((System.nanoTime() - startTime)*0.000001) + " Milliseconds");
            return tiles;
        }

        /**
         *
         * @param tiles
         * @param perlin2D
         * @param targetTileID
         * @param oreTileID
         * @param startingThreshold
         * @param endingThreshold
         * @param verticalStretch
         * @param horizontalStretch
         * @param startingLevel
         * @param endingLevel
         */
        private void generateOreVeins(Tile[][] tiles, PerlinNoise.Perlin2D perlin2D, ItemID targetTileID, ItemID oreTileID, double startingThreshold, double endingThreshold, int verticalStretch, int horizontalStretch, int startingLevel, int endingLevel){
            for (int x = 0; x < tiles.length; x++) {
                for (int y = startingLevel; y < endingLevel; y++) {
                    double currentThreshold = startingThreshold * (1 - ((double) (y-startingLevel)/((endingLevel-startingLevel)-1))) + endingThreshold * ((double) (y-startingLevel)/((endingLevel-startingLevel)-1));
                    if(perlin2D.cosInterpolation((double) x/horizontalStretch, (double) y/verticalStretch) >= currentThreshold && (tiles[x][y].itemID == targetTileID || targetTileID == null)){
                        tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, oreTileID);
                    }
                }
            }
        }

        /**
         *
         * @param tiles
         * @param perlin2D
         * @param startingThreshold
         * @param endingThreshold
         * @param verticalStretch
         * @param horizontalStretch
         * @param startingLevel
         * @param endingLevel
         */
        private void generateCavitySection(Tile[][] tiles, PerlinNoise.Perlin2D perlin2D, double startingThreshold, double endingThreshold, int verticalStretch, int horizontalStretch, int startingLevel, int endingLevel){
            for (int x = 0; x < tiles.length; x++) {
                for (int y = startingLevel; y < endingLevel; y++) {

                    double currentThreshold = startingThreshold * (1 - ((double) (y-startingLevel)/((endingLevel-startingLevel)-1))) + endingThreshold * ((double) (y-startingLevel)/((endingLevel-startingLevel)-1));

                    if(perlin2D.cosInterpolation((double) x/horizontalStretch, (double) y/verticalStretch) >= currentThreshold){
                        tiles[x][y] = TilePresets.getTilePreset(x * TileGraphics.TILE_WIDTH, y * TileGraphics.TILE_HEIGHT, TileGraphics.TILE_WIDTH, TileGraphics.TILE_HEIGHT, ItemID.TILE_AIR);
                    }
                }
            }
        }
    }
}
