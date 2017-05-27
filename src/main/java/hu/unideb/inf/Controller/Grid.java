package hu.unideb.inf.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Grid class implements the main grid of the 2048 game.
 */
public class Grid {

    /** The size of the grid.*/
    public static final int SIZE = 4;

    /** All the tiles.*/
    public Tile[][] tiles = new Tile[SIZE][SIZE];

    /** Constructor of the Grid class.*/
    public Grid(){
        for (int i = 0; i < tiles[0].length; i++){
            for (int j = 0; j < tiles.length; j++){
                tiles[i][j] = new Tile();
            }
        }
        generateNewTile();
        generateNewTile();
    }

    /**
     * This method gives value to an empty tile.
     * @return true.
     */
    public boolean generateNewTile(){
        if (!(hasEmptyTile())){
            return false;
        }

        Random random = new Random();

        int x = random.nextInt(SIZE);
        int y = random.nextInt(SIZE);

        if (tiles[x][y].getValue() == 0){
            tiles[x][y].setValue(getNewTileValue());
            return true;
        }
        return true;
    }

    private int getNewTileValue(){
        Random random = new Random();
        int rng = random.nextInt(2) + 1;
        return (rng * 2);
    }

    /**
     * Move the tiles to a specific direction.
     *
     * @param direction The direction.
     *
     */
    public void move(Direction direction){
        for (int i = 0; i < SIZE; i++){
            List<Tile> tileSet = new ArrayList<Tile>();
            for (int j = 0; j < SIZE; j++){
                switch(direction){
                    case UP: tileSet.add(tiles[i][j]); break;
                    case DOWN: tileSet.add(tiles[i][SIZE - j - 1]); break;
                    case LEFT: tileSet.add(tiles[j][i]); break;
                    case RIGHT: tileSet.add(tiles[SIZE - j - 1][i]); break;
                    default: break;
                }
            }
            if (!(isEmptyTile(tileSet))){
                slide(tileSet);
            }
        }
    }

    private boolean isEmptyTile(List<Tile> tileSet) {
        for (Tile tile: tileSet){
            if (tile.getValue() != 0){
                return false;
            }
        }
        return true;
    }

    private void slide(List<Tile> tileSet){
        slideToEdge(tileSet);
        mergeTile(tileSet);
    }

    private void slideToEdge(List<Tile> tileSet){
        for (int i = 0; i < tileSet.size(); i++){
            if (remainingIsZero(tileSet, i)){
                return;
            }
            while (tileSet.get(i).getValue() == 0){
                slideTo(tileSet, i);
            }
        }
    }

    private boolean remainingIsZero(List<Tile> tileSet, int i) {
        List<Tile> remainingTile = new ArrayList<Tile>();
        for (int j = i; j < tileSet.size(); j++){
            remainingTile.add(tileSet.get(j));
        }
        return (isEmptyTile(remainingTile));
    }

    private void slideTo(List<Tile> tileSet, int index){
        for (int j = index; j < tileSet.size() - 1; j++){
            tileSet.get(j).setValue(tileSet.get(j + 1).getValue());
        }
        tileSet.get(tileSet.size() - 1).clear();
    }

    private void mergeTile(List<Tile> tileSet){
        for (int i = 0; i < tileSet.size() - 1; i++){
            if (tileSet.get(i).equals(tileSet.get(i + 1))){
                tileSet.get(i).merge(tileSet.get(i + 1));
                tileSet.get(i + 1).clear();
                slideTo(tileSet, i + 1);
                Main.score+=tileSet.get(i).getValue();

            }
        }
    }

    /**
     * Check the grid to is has possible moves.
     * @return true.
     */
    public boolean noPossibleMove(){
        return !(hasEmptyTile()) && !(hasEqualNeighbour());
    }

    /**
     * Check the value of the tile.
     * The grid has no possible moves, when no tiles has 0 value.
     * @return true.
     */
    public boolean hasEmptyTile(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (tiles[i][j].getValue() == 0){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasEqualNeighbour() {
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (j < SIZE - 1){
                    if (tiles[i][j].equals(tiles[i][j + 1])){
                        return true;
                    }
                }

                if (i < SIZE - 1){
                    if (tiles[i][j].equals(tiles[i + 1][j])){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /** Set 0 value for every tile.*/
    public void resetGrid(){
        for (int i = 0; i < tiles[0].length; i++){
            for (int j = 0; j < tiles.length; j++){
                tiles[i][j].setValue(0);
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Tile[] tileRow: tiles){
            for (Tile tile: tileRow){
                sb.append(tile);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}