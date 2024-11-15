/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: Represents the world map in the game. Handles loading and drawing of the map tiles.
 */

package com.cow;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class WorldMap
{

    GamePanel gp; //reference to the game panel
    Tile[][] tiles; //2D array to hold the tiles
    int tileSize; //size of each tile
    BufferedImage tile0, tile1, tile2; //images for different tile types

    public WorldMap(GamePanel gp)
    {
        this.gp = gp; //initialize the game panel reference
        this.tileSize = gp.tileSize; //initialize the tile size
        this.tiles = new Tile[gp.maxScreenCol][gp.maxScreenRow]; //initialize the tiles array
        loadTileImages(); //load the tile images
        createMap(); //create the map layout
    }

    private void loadTileImages()
    {
        try
        {
            //load images for different tile types
            tile0 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile0.png"));
            tile1 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile1.png"));
            tile2 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile2.png"));
        } catch (IOException e)
        {
            e.printStackTrace(); //handle exceptions
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
                tiles[col][row] = new Tile(mapLayout[row][col]); //initialize each tile
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        for (int row = 0; row < gp.maxScreenRow; row++)
        {
            for (int col = 0; col < gp.maxScreenCol; col++)
            {
                int tileType = tiles[col][row].type; //get the type of the tile
                BufferedImage image = null; //initialize the image

                if (tileType == 0)
                {
                    image = tile0; //set the image for tile type 0
                }
                else if (tileType == 1)
                {
                    image = tile1; //set the image for tile type 1
                }
                else if (tileType == 2)
                {
                    image = tile2; //set the image for tile type 2
                }

                if (image != null)
                {
                    //draw the image on the screen
                    g2.drawImage(image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                }
            }
        }
    }

    class Tile
    {
        int type; //type of the tile

        public Tile(int type)
        {
            this.type = type; //initialize the tile type
        }
    }
}

