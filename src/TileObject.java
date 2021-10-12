public abstract class TileObject extends NonMovingObject {
    boolean isBroken = false;
    public abstract void whenClicked(Tiles.Tile[][] tiles, int x, int y);
    public void whenPlaced(Tiles.Tile[][] tiles, int x, int y){
        triggerUpdate(tiles, x, y);
    }
    public void whenBroken(Tiles.Tile[][] tiles, int x, int y){
        if(!this.isBroken){
            this.isBroken = true;
            triggerUpdate(tiles, x, y);
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
