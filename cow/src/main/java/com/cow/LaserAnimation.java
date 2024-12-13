/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents a laser animation in the game, which includes methods to load laser images,
 * update the animation, get the current image, start and stop special and holding sequences, and check the status of these sequences.
 */

package com.cow; 

import java.awt.image.BufferedImage; 
import java.io.IOException; 
import javax.imageio.ImageIO;

public class LaserAnimation 
{
    private BufferedImage[] laserImages = new BufferedImage[6]; //array to hold the laser images (LaserB1 to LaserB6)
    private BufferedImage[] specialLaserImages = new BufferedImage[5]; //array to hold the special laser images (Laser1 to Laser5)
    private int laserImageIndex = 0; //index to keep track of the current laser image
    private int specialLaserImageIndex = 0; //index to keep track of the current special laser image
    private int animationCounter = 0; //counter to keep track of the animation frames
    private final int animationInterval = 5; //interval between animation frames
    private boolean isSpecialSequence = false; //flag to indicate if the special sequence is active
    private boolean isHoldingSequence = false; //flag to indicate if the holding sequence is active

    public LaserAnimation() 
    {
        loadLaserImages(); //load the laser images
        loadSpecialLaserImages(); //load the special laser images
    }

    private void loadLaserImages() 
    {
        for (int i = 0; i < laserImages.length; i++) 
        {
            try 
            {
                //load each laser image from the resources folder
                laserImages[i] = ImageIO.read(getClass().getResourceAsStream("/Laser/LaserB" + (i + 1) + ".png"));
            } catch (IOException e) 
            {
                e.printStackTrace(); //print stack trace if there is an error loading the image
            }
        }
    }

    private void loadSpecialLaserImages() 
    {
        for (int i = 0; i < specialLaserImages.length; i++) 
        {
            try 
            {
                //load each special laser image from the resources folder
                specialLaserImages[i] = ImageIO.read(getClass().getResourceAsStream("/Laser/Laser" + (i + 1) + ".png"));
            } catch (IOException e) 
            {
                e.printStackTrace(); //print stack trace if there is an error loading the image
            }
        }
    }

    public void updateAnimation() 
    {
        animationCounter++; //increment the animation counter
        if (isSpecialSequence) 
        {
            if (animationCounter >= animationInterval) 
            {
                animationCounter = 0; //reset the animation counter
                specialLaserImageIndex++; //move to the next special laser image
                if (specialLaserImageIndex >= specialLaserImages.length) 
                {
                    specialLaserImageIndex = 0; //reset to the first special laser image
                    isSpecialSequence = false; //rpecial sequence completes
                }
            }
        } else if (isHoldingSequence) 
        {
            if (animationCounter >= animationInterval) 
            {
                animationCounter = 0; //reset the animation counter
                laserImageIndex = (laserImageIndex + 1) % laserImages.length; //cycle through the laser images
            }
        } 
    }

    public BufferedImage getCurrentImage() 
    {
        //return the current image based on whether the special sequence is active
        return isSpecialSequence ? specialLaserImages[specialLaserImageIndex] : laserImages[laserImageIndex];
    }

    public void startSpecialSequence() 
    {
        isSpecialSequence = true; //start the special sequence
        specialLaserImageIndex = 0; //reset to the first special laser image
    }

    public void startHoldingSequence() 
    {
        isHoldingSequence = true; //start the holding sequence
    
    }

    public void stopHoldingSequence() 
    {
        isHoldingSequence = false; //stop the holding sequence
    }

    public boolean isSpecialSequence() 
    {
        return isSpecialSequence; //return whether the special sequence is active
    }

    public boolean isHoldingSequence() 
    {
        return isHoldingSequence; //return whether the holding sequence is active
    }
}

