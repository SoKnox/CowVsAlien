/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: Represents a non-player character (the duck) in the game. Handles NPC interactions and dialogue display.
 */
package com.cow;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NPC extends Being 
{

    private BufferedImage defaultImage; //default image of the NPC
    private BufferedImage proximityImage; //image displayed when the cow is within 125 pixels
    private GamePanel gp; //reference to the GamePanel
    private static final int PROXIMITY_DISTANCE = 125; //proximity distance in pixels

    public NPC(GamePanel gp) 
    {
        this.gp = gp; //initialize the GamePanel reference
        loadImages(); //load the NPC images
    }

    private void loadImages() 
    {
        try 
        {
    
            //load the images for the NPC
            defaultImage = ImageIO.read(getClass().getResourceAsStream("/NPC/NPC.png"));
            proximityImage = ImageIO.read(getClass().getResourceAsStream("/NPC/NPC2.png"));
        } catch (IOException e) 
        {
            e.printStackTrace(); //handle exceptions
        }
    }

    public void draw(Graphics2D g2) 
    {
        BufferedImage currentImage = defaultImage; //default to the normal image

        //if the cow is within 125 pixels, use the proximity image
        if (isCowWithinProximity()) 
        {
            currentImage = proximityImage;
        }

        int scaledWidth = gp.tileSize;  //width of the NPC
        int scaledHeight = gp.tileSize; //height of the NPC

        //draw the image
        g2.drawImage(currentImage, x, y, scaledWidth, scaledHeight, null);
    }

    private boolean isCowWithinProximity() 
    {
        int cowX = gp.playerCow.x;
        int cowY = gp.playerCow.y;

        //calculate the distance between the cow and the NPC
        double distance = Math.sqrt(Math.pow(cowX - x, 2) + Math.pow(cowY - y, 2));
        return distance <= PROXIMITY_DISTANCE; //return true if within the proximity distance
    }
}


 