package org.example.tile;

import org.example.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    private final GamePanel gp;
    public final Tile[] tile;
    public final int[][] mapTileNumber;

    private static final String TILE_IMAGE_PATH = "/tiles/tilesOldVersion/";

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNumber = new int[gp.maxWorldCol][gp.maxWorldRow];

        loadTileImages();
        loadMap("/maps/map02.txt");
    }

    private void loadTileImages() {
        try {
            loadSingleTile(0, "grass.png", false);
            loadSingleTile(1, "wall.png", true);
            loadSingleTile(2, "water.png", true);
            loadSingleTile(3, "earth.png", false);
            loadSingleTile(4, "tree.png", true);
            loadSingleTile(5, "sand.png", false);
        } catch (IOException e) {
            System.err.println("Error loading tile images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSingleTile(int index, String fileName, boolean collision) throws IOException {
        InputStream is = getClass().getResourceAsStream(TILE_IMAGE_PATH + fileName);
        if (is == null) {
            throw new IOException("Tile image not found: " + fileName);
        }
        tile[index] = new Tile();
        tile[index].image = ImageIO.read(is);
        tile[index].collision = collision;
    }

    public void loadMap(String fileName) {
        try (InputStream is = getClass().getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                throw new IOException("Map file not found: " + fileName);
            }

            int col = 0;
            int row = 0;
            while (row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null || line.isEmpty()) {
                    throw new IOException("Map file is malformed or incomplete.");
                }
                String[] numbers = line.split(" ");

                if (numbers.length != gp.maxWorldCol) {
                    throw new IOException("Map file does not match expected dimensions.");
                }

                while (col < gp.maxWorldCol) {
                    int num = Integer.parseInt(numbers[col]);
                    if (num < 0 || num >= tile.length) {
                        throw new IOException("Invalid tile number in map file at row " + row + ", col " + col);
                    }
                    mapTileNumber[col][row] = num;
                    col++;
                }
                col = 0;
                row++;
            }
        } catch (IOException e) {
            System.err.println("Error loading map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNumber[worldCol][worldRow];

            if (tileNum < 0 || tileNum >= tile.length) {
                System.err.println("Invalid tile number at col " + worldCol + ", row " + worldRow);
                tileNum = 0;  // Default to a safe tile, such as grass
            }

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (isTileVisible(worldX, worldY)) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    private boolean isTileVisible(int worldX, int worldY) {
        return worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY;
    }
}
