package com.javarush.task.task35.task3513;

import java.util.*;

//будет содержать игровую логику и хранить игровое поле.
public class Model {
    private static final int FIELD_WIDTH = 4; //ширина игрового поля
    private Tile[][] gameTiles;

    public int score = 0;
    public int maxTile = 0;

    public Model() {
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private void addTile() {
        List<Tile> list = getEmptyTiles();
        int randomNumberArray = (int) (list.size() * Math.random());
        if (!list.isEmpty()) {
            Tile tile = list.get(randomNumberArray);
            tile.value = (Math.random() < 0.9 ? 2 : 4);
        }
    }

    private List<Tile> getEmptyTiles() {
        ArrayList<Tile> listEmptyTiles = new ArrayList<>();
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                if (gameTiles[i][j].isEmpty()) {
                    listEmptyTiles.add(gameTiles[i][j]);
                }
            }
        }
        return listEmptyTiles;
    }

    private boolean compressTiles(Tile[] tiles) {
        int insertPosition = 0;
        boolean result = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (!tiles[i].isEmpty()) {
                if (i != insertPosition) {
                    tiles[insertPosition] = tiles[i];
                    tiles[i] = new Tile();
                    result = true;
                }
                insertPosition++;
            }
        }
        return result;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean result = false;
        LinkedList<Tile> tilesList = new LinkedList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (tiles[i].isEmpty()) {
                continue;
            }

            if (i < FIELD_WIDTH - 1 && tiles[i].value == tiles[i + 1].value) {
                int updatedValue = tiles[i].value * 2;
                if (updatedValue > maxTile) {
                    maxTile = updatedValue;
                }
                score += updatedValue;
                tilesList.addLast(new Tile(updatedValue));
                tiles[i + 1].value = 0;
                result = true;
            } else {
                tilesList.addLast(new Tile(tiles[i].value));
            }
            tiles[i].value = 0;
        }

        for (int i = 0; i < tilesList.size(); i++) {
            tiles[i] = tilesList.get(i);
        }

        return result;
    }

    public Tile[][] turn(Tile[][] tiles) {
        Tile[][] array = new Tile[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                array[j][tiles.length - 1 - i] = tiles[i][j];
            }
        }
        return array;
    }

    public void left() {
        boolean a = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                a = true;
            }
        }
        if (a) addTile();
    }

    public void right() {
        gameTiles = turn(gameTiles);
        gameTiles = turn(gameTiles);
        left();
        gameTiles = turn(gameTiles);
        gameTiles = turn(gameTiles);
    }

    public void up() {
        gameTiles = turn(gameTiles);
        gameTiles = turn(gameTiles);
        gameTiles = turn(gameTiles);
        left();
        gameTiles = turn(gameTiles);
    }

    public void down() {
        gameTiles = turn(gameTiles);
        left();
        gameTiles = turn(gameTiles);
        gameTiles = turn(gameTiles);
        gameTiles = turn(gameTiles);
    }

    private int getEmptyTilesCount() {
        return getEmptyTiles().size();
    }

    private boolean isFull() {
        return getEmptyTilesCount() == 0;
    }

    boolean canMove() {
        if (!isFull()) {
            return true;
        }

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                Tile tile = gameTiles[i][j];
                if((i < FIELD_WIDTH - 1 && tile.value == gameTiles[i+1][j].value)
                || (j < FIELD_WIDTH - 1 && tile.value == gameTiles[i][j+1].value))
                    return true;
            }
        }
        return false;
    }
}
