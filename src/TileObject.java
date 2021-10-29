public abstract class TileObject extends NonMovingObject {
    public abstract void whenClicked(Tiles.Tile[][] tiles, int x, int y);


    public void whenPlaced(Tiles.Tile[][] tiles, int x, int y){
        triggerUpdate(tiles, x, y);
    }


    public void whenBroken(Tiles.Tile[][] tiles, int x, int y){
        if(tiles[x][y].itemID != ItemID.TILE_AIR){
            tiles[x][y] = new Tiles.Tile(x * Tiles.TILE_WIDTH, y * Tiles.TILE_HEIGHT, Tiles.TILE_WIDTH, Tiles.TILE_HEIGHT, ItemID.TILE_AIR){


                @Override
                public void whenClicked(Tiles.Tile[][] tiles, int x, int y) {
                    //Do nothing
                }



                @Override
                public void whenPlaced(Tiles.Tile[][] tiles, int x, int y) {
                    super.whenPlaced(tiles, x, y);
                }



                @Override
                public void whenBroken(Tiles.Tile[][] tiles, int x, int y) {
                    super.whenBroken(tiles, x, y);
                }



                @Override
                public void onTileUpdate(Tiles.Tile[][] tiles, int x, int y) {
                    //Do nothing
                }



                @Override
                public void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y) {
                    //Do nothing
                }
            };
            triggerUpdate(tiles, x, y);
            Game.chunks[x].yValues.add(y);
        }
    }


    public abstract void onTileUpdate(Tiles.Tile[][] tiles, int x, int y);


    public abstract void onRandomUpdate(Tiles.Tile[][] tiles, int x, int y);



    private void triggerUpdate(Tiles.Tile[][] tiles, int x, int y){
        if(x>0){
            tiles[x-1][y].onTileUpdate(tiles, x-1, y);
        }
        if(x<tiles.length-1){
            tiles[x+1][y].onTileUpdate(tiles, x+1, y);
        }
        if(y>0){
            tiles[x][y-1].onTileUpdate(tiles, x, y-1);
        }
        if(y<tiles[0].length-1){
            tiles[x][y+1].onTileUpdate(tiles, x, y+1);
        }
    }
}
