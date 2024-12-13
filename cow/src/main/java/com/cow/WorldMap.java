/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: Represents the world map in the game. Handles random loading and drawing of the map tiles.
 */
package com.cow;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class WorldMap 
{

    GamePanel gp; //reference to the game panel
    Tile[][] tiles; //2D array to hold the tiles
    int tileSize; //size of each tile
    BufferedImage tile0, tile2, tile3, tile4, tile5; //images for different tile types

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
            tile2 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile2.png"));
            tile3 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile3.png"));
            tile4 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile4.png"));
            tile5 = ImageIO.read(getClass().getResourceAsStream("/SampleTileSet/Tile5.png"));
        } catch (IOException e) 
        {
            e.printStackTrace(); //handle exceptions
        }
    }

    private void createMap() 
    {
        Random random = new Random();
        int[] validTiles = {0, 2, 3, 4, 5}; //array of valid tile types

        for (int row = 0; row < gp.maxScreenRow; row++) 
        {
            for (int col = 0; col < gp.maxScreenCol; col++) 
            {
                int tileType = validTiles[random.nextInt(validTiles.length)]; //generate a random tile type from the valid tiles
                tiles[col][row] = new Tile(tileType); //initialize each tile with the random type
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
                    case 2:
                        image = tile2; //set the image for tile type 2
                        break;
                    case 3:
                        image = tile3; //set the image for tile type 3
                        break;
                    case 4:
                        image = tile4; //set the image for tile type 4
                        break;
                    case 5:
                        image = tile5; //set the image for tile type 5
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

