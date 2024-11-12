/**
 * Date: 11/11/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This is the player class that creates the cow (the player's) animation movements according to KeyPressed and KeyReleased. 
 * It cloads all the cow sprite images creates an animation cycle.
 * Buffer Image Used because it hides all the complexity of different types of images whilst allowing access to the underlying data.
 */

package com.cow;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerCow extends Being 
{
    GamePanel gp;
    KeyHandler keyH;
    private int animationCounter = 0; //counter for cycling images

    //constructor to initialize the player cow's position, speed, and images
    public PlayerCow(GamePanel gp, KeyHandler keyH) 
    {
        this.gp = gp;
        this.keyH = keyH;
        x = 100; //initial x position
        y = 100; //initial y position
        speed = 4; //speed of movement
        getPlayerImage(); //load the player images
        direction = "down"; //default direction is down
    }

    //load all the player cow images
    public void getPlayerImage() 
    {
        try 
        {
            
            up = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowUp.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowUp1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowUp2.png")); 
            down = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowDown.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowDown1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowDown2.png"));
            left = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowLeft.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowLeft1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowLeft2.png"));
            right = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowRight.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowRight1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowRight2.png"));
        } catch (IOException e) 
        {
            e.printStackTrace(); //exception if image loading fails
        }
    }

    //updates the cow's position and animation cycle
    public void update() 
    {
        boolean moving = false; //checsk if the player is moving

        //checks for key presses and update position/direction 
        if (keyH.upPressed) 
        {
            direction = "up";//set direction to up
            y -= speed;//move up
            moving = true;
        } else if (keyH.downPressed) 
        {
            direction = "down";//set direction to down
            y += speed;//move down
            moving = true;
        } else if (keyH.leftPressed) 
        {
            direction = "left"; //set direction to left
            x -= speed;///move left
            moving = true;
        } else if (keyH.rightPressed) 
        {
            direction = "right";//set direction to right
            x += speed;//move right
            moving = true;
        }

        //if moving, increment the animation counter to cycle images
        if (moving) 
        {
            animationCounter++;
        } else 
        {
            animationCounter = 0; //reset animation when not moving
        }
    }

    //draws the cow's current image on the screen
    public void draw(Graphics2D g2) 
    {
        BufferedImage image = null; //holds the current image

        //calculate the position in the animation cycle (0 or 1) based on animation counter
        int cyclePosition = (animationCounter / 10) % 2;//only two frames for each direction animation

        //determine the image based on the direction and whether the player is moving
        switch (direction) 
        {
            case "up":
                //cycle CowUp1, CowUp2 when moving. CowUp when not moving
                if (animationCounter == 0) 
                {
                    image = up;//no movement, use CowUp
                } else 
                {
                    image = (cyclePosition == 0) ? up1 : up2;//alternating between CowUp1 and CowUp2
                }
                break;
            case "down":
                //cycle: CowDown1, CowDown2 when moving. CowDown when not moving
                if (animationCounter == 0) 
                {
                    image = down;  //no movement, use CowDown
                } else 
                {
                    image = (cyclePosition == 0) ? down1 : down2;//alternating between CowDown1 and CowDown2
                }
                break;
            case "left":
                //Cycle: CowLeft2, CowLeft1 when moving. CowLeft when not moving... Cow Left2 is first this time unlike the other movements.
                if (animationCounter == 0) 
                {
                    image = left;//no movement, use CowLeft
                } else 
                {
                    image = (cyclePosition == 0) ? left2 : left1;//alternating between CowLeft2 and CowLeft1
                }
                break;
            case "right":
                //cycle: CowRight1, CowRight2 when moving. CowRight when not moving
                if (animationCounter == 0) 
                {
                    image = right;  //no movement, use CowRight
                } else 
                {
                    image = (cyclePosition == 0) ? right1 : right2;//alternating between CowRight1 and CowRight2
                }
                break;
        }

        //draw the selected image on the screen at the player's position
        if (image != null) 
        {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }
    }
}