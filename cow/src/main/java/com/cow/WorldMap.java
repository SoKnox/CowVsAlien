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
    BufferedImage tile0, tile01, tile02, tileGround, tile11, tile12; //images for different tile types

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
            tile01 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile0.1.png"));
            tile02 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile0.2.png"));
            tileGround = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile1.png"));
            tile11 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile1.1.png"));
            tile12 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile1.2.png"));
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
            {0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 2, 0, 0, 2, 0, 1, 0, 0, 0, 2, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {0, 1, 0, 0, 0, 0, 0, 2, 0, 1, 2, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}
        };

        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                tiles[col][row] = new Tile(mapLayout[row][col]); // initialize each tile
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

                switch (tileType) 
                {
                    case 0:
                        image = tile0; //set the image for tile type 0
                        break;
                    case 1:
                        image = tile01; //set the image for tile type 1
                        break;
                    case 2:
                        image = tile02; //set the image for tile type 2
                        break;
                    case 3:
                        image = tileGround; //set the image for tile type 3
                        break;
                    case 4:
                        image = tile11; //set the image for tile type 4
                        break;
                    case 5:
                        image = tile12; //set the image for tile type 5
                        break;
                    default:
                        break;
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

