/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This is the player class that creates the cow (the player's) animation movements according to KeyPressed and KeyReleased. 
 * It cloads all the cow sprite images creates an animation cycle.
 * Buffer Image Used because it hides all the complexity of different types of images whilst allowing access to the underlying data.
 */

 package com.cow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerCow extends Being 
{

    GamePanel gp;
    KeyHandler keyH;
    private int animationCounter = 0; //counter for cycling images
    private NPC npc; //Reference to the NPC for collision detection
    private boolean showDialogue = false; //Flag to show the dialogue text
    private Item heldItem = null;

    //constructor to initialize the player cow's position, speed, and images
    public PlayerCow(GamePanel gp, KeyHandler keyH, NPC npc) 
    {
        this.gp = gp;
        this.keyH = keyH;
        this.npc = npc;
        x = 100; //initial x position
        y = 100; //initial y position
        speed = 4; //speed of movement
        getPlayerImage(); //load the player images
        direction = "down"; //default direction is down
    }

    //load all the player cow images
    public void getPlayerImage() 
    {
        try {
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
        boolean moving = false; //checks if the player is moving

        //check for key presses and update position/direction
        if (keyH.upPressed) 
        {
            direction = "up"; //set direction to up
            if (!willCollide(x, y - speed)) 
            {
                y -= speed; //move up
                moving = true;
            }
        } else if (keyH.downPressed) 
        {
            direction = "down"; //set direction to down
            if (!willCollide(x, y + speed)) 
            {
                y += speed; //move down
                moving = true;
            }
        } else if (keyH.leftPressed) 
        {
            direction = "left"; //set direction to left
            if (!willCollide(x - speed, y)) 
            {
                x -= speed; //move left
                moving = true;
            }
        } else if (keyH.rightPressed) 
        {
            direction = "right"; //set direction to right
            if (!willCollide(x + speed, y)) 
            {
                x += speed; //move right
                moving = true;
            }
        }

        //if moving, increment the animation counter to cycle images
        if (moving) 
        {
            animationCounter++;
        } else 
        {
            animationCounter = 0; //reset animation when not moving
        }

        //check for proximity to the chicken
        if (isWithinProximity()) 
        {
            showDialogue = true;
            npc.interact();
        } else 
        {
            showDialogue = false;
            npc.setShowDialogue(false);
        }
    }

    //check if the cow is within 11 pixels of the chicken
    private boolean isWithinProximity() 
    {
        int chickenX = npc.x;
        int chickenY = npc.y;
        int proximityDistance = 100;

        return Math.abs(x - chickenX) <= proximityDistance && Math.abs(y - chickenY) <= proximityDistance;
    }

    //check if the cow will collide with the chicken
    private boolean willCollide(int nextX, int nextY) 
    {
        int chickenX = npc.x;
        int chickenY = npc.y;
        int chickenWidth = gp.tileSize / 2;
        int chickenHeight = gp.tileSize / 2;

        int cowWidth = gp.tileSize;
        int cowHeight = gp.tileSize;

        return nextX < chickenX + chickenWidth &&
               nextX + cowWidth > chickenX &&
               nextY < chickenY + chickenHeight &&
               nextY + cowHeight > chickenY;
    }

// Draws the cow's current image on the screen
public void draw(Graphics2D g2) {
    BufferedImage image = null; // Holds the current image

    // Calculate the position in the animation cycle (0 or 1) based on animation counter
    int cyclePosition = (animationCounter / 10) % 2; // Only two frames for each direction animation

    // Determine the image based on the direction and whether the player is moving
    switch (direction) {
        case "up":
            // Cycle CowUp1, CowUp2 when moving. CowUp when not moving
            if (animationCounter == 0) {
                image = up; // No movement, use CowUp
            } else {
                image = (cyclePosition == 0) ? up1 : up2; // Alternating between CowUp1 and CowUp2
            }
            break;
        case "down":
            // Cycle: CowDown1, CowDown2 when moving. CowDown when not moving
            if (animationCounter == 0) {
                image = down; // No movement, use CowDown
            } else {
                image = (cyclePosition == 0) ? down1 : down2; // Alternating between CowDown1 and CowDown2
            }
            break;
        case "left":
            // Cycle: CowLeft2, CowLeft1 when moving. CowLeft when not moving
            if (animationCounter == 0) {
                image = left; // No movement, use CowLeft
            } else {
                image = (cyclePosition == 0) ? left2 : left1; // Alternating between CowLeft2 and CowLeft1
            }
            break;
        case "right":
            // Cycle: CowRight1, CowRight2 when moving. CowRight when not moving
            if (animationCounter == 0) {
                image = right; // No movement, use CowRight
            } else {
                image = (cyclePosition == 0) ? right1 : right2; // Alternating between CowRight1 and CowRight2
            }
            break;
    }

    // Draw the sword if the cow is holding it
    if (heldItem != null && heldItem.getName().equals("Sword")) {
        int swordX = x + gp.tileSize - gp.tileSize / 4; // Adjust to the right of the cow
        int swordY = y + gp.tileSize / 2 - gp.tileSize / 4; // Adjust to the cow's middle
        g2.drawImage(heldItem.getImage(), swordX, swordY, gp.tileSize / 2, gp.tileSize / 2, null);
    }

    // Draw the selected cow image on the screen at the player's position
    if (image != null) {
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}


    // Getter for the held item (optional, if needed elsewhere)
public Item getHeldItem() {
    return heldItem;
}

// Setter for the held item (optional, if needed elsewhere)
public void setHeldItem(Item heldItem) {
    this.heldItem = heldItem;
}
   
    }

