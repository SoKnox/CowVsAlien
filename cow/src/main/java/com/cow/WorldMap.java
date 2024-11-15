package com.cow;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class WorldMap
{

    GamePanel gp;
    Tile[][] tiles;
    int tileSize;
    BufferedImage tile0, tile1, tile2;

    public WorldMap(GamePanel gp)
    {
        this.gp = gp;
        this.tileSize = gp.tileSize;
        this.tiles = new Tile[gp.maxScreenCol][gp.maxScreenRow];
        loadTileImages();
        createMap();
    }

    private void loadTileImages()
    {
        try
        {
            tile0 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile0.png"));
            tile1 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile1.png"));
            tile2 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile2.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void createMap()
    {
        //use matrix to make map
        int[][] mapLayout =
        {
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 2, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {1, 2, 2, 2, 1, 0, 0, 1, 2, 2, 1, 0, 0, 1, 1, 0},
            {1, 2, 2, 2, 1, 0, 0, 1, 2, 2, 1, 0, 0, 1, 1, 0},
            {0, 1, 2, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 2, 1, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 2, 1, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        for (int row = 0; row < gp.maxScreenRow; row++)
        {
            for (int col = 0; col < gp.maxScreenCol; col++)
            {
                tiles[col][row] = new Tile(mapLayout[row][col]);
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        for (int row = 0; row < gp.maxScreenRow; row++)
        {
            for (int col = 0; col < gp.maxScreenCol; col++)
            {
                int tileType = tiles[col][row].type;
                BufferedImage image = null;

                if (tileType == 0)
                {
                    image = tile0;
                }
                else if (tileType == 1)
                {
                    image = tile1;
                }
                else if (tileType == 2)
                {
                    image = tile2;
                }

                if (image != null)
                {
                    g2.drawImage(image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                }
            }
        }
    }

    class Tile
    {
        int type;

        public Tile(int type)
        {
            this.type = type;
        }
    }
}

