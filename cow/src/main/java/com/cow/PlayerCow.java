/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This is the player class that creates the cow (the player's) animation movements according to KeyPressed and KeyReleased. 
 * It loads all the cow sprite images creates an animation cycle.
 * Buffer Image Used because it hides all the complexity of different types of images whilst allowing access to the underlying data.
 * Handles cow holding sword as well.
 */
package com.cow;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerCow extends Being 
{
    GamePanel gp;
    KeyHandler keyH;
    private CollisionChecker collisionChecker; //reference to CollisionChecker
    private int animationCounter = 0; //counter for cycling images
    private Item heldItem = null;
    private LaserAnimation laserAnimation;
    private boolean isFiringLaser = false;
    private boolean wasSpacePressed = false;
    private BufferedImage[] swordImages = new BufferedImage[2];
    private int swordImageIndex = 0;
    private int swordAnimationCounter = 0;
    private final int swordAnimationInterval = 10; //interval between sword frames

    //constructor to initialize the player cow's position, speed, and images
    public PlayerCow(GamePanel gp, KeyHandler keyH, CollisionChecker collisionChecker) 
    {
        this.gp = gp;
        this.keyH = keyH;
        this.collisionChecker = collisionChecker; //initialize CollisionChecker
        x = 100; //initial x position
        y = 100; //initial y position
        speed = 4; //speed of movement
        getPlayerImage(); //load the player images
        direction = "down"; //default direction is down
        laserAnimation = new LaserAnimation();
        loadSwordImages();
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

    //loads sword's images
    private void loadSwordImages() 
    {
        try 
        {
            swordImages[0] = ImageIO.read(getClass().getResourceAsStream("/Items/S1.png"));
            swordImages[1] = ImageIO.read(getClass().getResourceAsStream("/Items/S2.png"));
        } catch (IOException e)
         {
            e.printStackTrace();
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
            y -= speed; //move up
            moving = true;
        } else if (keyH.downPressed) 
        {
            direction = "down"; //set direction to down
            y += speed; //move down
            moving = true;
        } else if (keyH.leftPressed) 
        {
            direction = "left"; //set direction to left
            x -= speed; //move left
            moving = true;
        } else if (keyH.rightPressed) 
        {
            direction = "right"; //set direction to right
            x += speed; //move right
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

        //update laser animation if space bar is held
        if (keyH.isSpaceHeld()) 
        {
            isFiringLaser = true;
            laserAnimation.startHoldingSequence();
            wasSpacePressed = true;
        } else 
        {
            if (wasSpacePressed) 
            {
                laserAnimation.startSpecialSequence();
                wasSpacePressed = false;
            } else 
            {
                isFiringLaser = false;
                laserAnimation.stopHoldingSequence();
            }
        }

        //update the laser animation
        laserAnimation.updateAnimation();

        //update the sword animation
        swordAnimationCounter++;
        if (swordAnimationCounter >= swordAnimationInterval) 
        {
            swordAnimationCounter = 0;
            swordImageIndex = (swordImageIndex + 1) % swordImages.length;
        }
    }

    //draws the cow's current image on the screen
    public void draw(Graphics2D g2) 
    {
        BufferedImage image = null; //holds the current image

        //calculate the position in the animation cycle (0 or 1) based on animation counter
        int cyclePosition = (animationCounter / 10) % 2; //only two frames for each direction animation

        //determine the image based on the direction and whether the player is moving
        switch (direction) 
        {
            case "up":
                if (animationCounter == 0) 
                {

                    image = up;
                } else 
                {
                    image = (cyclePosition == 0) ? up1 : up2;
                }
                break;
            case "down":
                if (animationCounter == 0) 
                {
                    image = down;
                } else 
                {
                    image = (cyclePosition == 0) ? down1 : down2;
                }
                break;
            case "left":
                if (animationCounter == 0) 
                {
                    image = left;
                } else 
                {
                    image = (cyclePosition == 0) ? left2 : left1;
                }
                break;
            case "right":
                if (animationCounter == 0) 
                {
                    image = right;
                } else 
                {
                    image = (cyclePosition == 0) ? right1 : right2;
                }
                break;
        }

        //draw the sword if the cow is holding it and not facing down
        if (heldItem != null && heldItem.getName().equals("Sword") && !direction.equals("down")) 
        {
            float swordX, swordY;

            // djusts the sword's position based on the direction
            switch (direction) 
            {
                case "left":
                    swordX = x - gp.tileSize / 3.25f;
                    swordY = y + gp.tileSize / 2f - gp.tileSize / 1.35f;

                    //flip the sword image horizontally
                    g2.drawImage(swordImages[swordImageIndex], (int) (swordX + gp.tileSize / 2f), (int) swordY, -gp.tileSize, gp.tileSize, null);
                    break;
                case "right":
                    swordX = x + gp.tileSize - gp.tileSize / 4.25f;
                    swordY = y + gp.tileSize / 2f - gp.tileSize / 1.35f;
                    g2.drawImage(swordImages[swordImageIndex], (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);
                    break;
                case "up":
                    swordX = x + gp.tileSize / 2f - gp.tileSize / 8f;
                    swordY = y - gp.tileSize / 4f;
                    g2.drawImage(swordImages[swordImageIndex], (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);
                    break;
                case "down":
                    swordX = x + gp.tileSize / 2f - gp.tileSize / 4.5f;
                    swordY = y + gp.tileSize / 2f - gp.tileSize / -10f;
                    g2.drawImage(swordImages[swordImageIndex], (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);
                    break;

                default:
                    swordX = x + gp.tileSize / 2f - gp.tileSize / 4.5f;
                    swordY = y + gp.tileSize / 2f - gp.tileSize / -10f;
                    g2.drawImage(swordImages[swordImageIndex], (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);
                    break;
            }
        }

        //draw the selected cow image on the screen at the player's position
        if (image != null) 
        {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }

        //draw the sword on top of the cow when facing down
        if(heldItem != null && heldItem.getName().equals("Sword") && direction.equals("down")) 
        {
            float swordX = x + gp.tileSize / 2f - gp.tileSize / 16f; //horizontal
            float swordY = y + gp.tileSize - gp.tileSize / 1.3f; //vertical

            //save the current transformation
            AffineTransform oldTransform = g2.getTransform();

            //rotate the graphics context by 32 degrees around the center of the sword
            g2.rotate(Math.toRadians(32), swordX + gp.tileSize / 2, swordY + gp.tileSize / 2);

            //draw the rotated sword
            g2.drawImage(swordImages[swordImageIndex], (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);

            //restore the original transformation
            g2.setTransform(oldTransform);
        }

        //draw the laser animation if the space bar is held or the special sequence is active
        if (isFiringLaser || laserAnimation.isSpecialSequence() || laserAnimation.isHoldingSequence()) {
            BufferedImage laserImage = laserAnimation.getCurrentImage();
            int laserX = x + gp.tileSize / 2 - gp.tileSize / 2; //center the laser horizontally
            int laserY = y - gp.tileSize; //position the laser above the cow
        
            //offsets for each direction
            int upOffsetX = -5;
            int upOffsetY = 80;
            int downOffsetX = 5;
            int downOffsetY = 125;
            int rightOffsetX = -10;
            int rightOffsetY = 95;
            int leftOffsetX = -95;
            int leftOffsetY = 95;
        
            //save the current transformation
            AffineTransform oldTransform = g2.getTransform();
        
            //adjusts the laser position and rotation based on the direction
            switch (direction) 
            {
                case "up":
                    laserY += upOffsetY;
                    laserX += upOffsetX;
        
                    //rotate the laser image 90 degrees
                    g2.rotate(Math.toRadians(90), laserX + gp.tileSize / 2, laserY + gp.tileSize / 2);
                    g2.drawImage(laserImage, laserX - 50, laserY + 5, gp.tileSize, gp.tileSize, null);
                    break;
                case "down":
                    laserX += downOffsetX;
                    laserY += downOffsetY;
        
                    //rotate the laser image 270 degrees
                    g2.rotate(Math.toRadians(270), laserX + gp.tileSize / 2, laserY + gp.tileSize / 2);
                    g2.drawImage(laserImage, laserX + 5, laserY + 6, gp.tileSize, gp.tileSize, null);
                    break;
                case "right":
                    laserY += rightOffsetY; 
                    laserX += rightOffsetX;
        
                    //flip the laser image horizontally
                    g2.scale(-1, 1);
                    g2.drawImage(laserImage, -laserX - gp.tileSize * 2, laserY, gp.tileSize, gp.tileSize, null);
                    break;
                case "left":
                    laserX += leftOffsetX; //move the laser to the left
                    laserY += leftOffsetY; //lower the position of the laser
                    g2.drawImage(laserImage, laserX, laserY, gp.tileSize, gp.tileSize, null);
                    break;
                default:
                    g2.drawImage(laserImage, laserX, laserY, gp.tileSize, gp.tileSize, null);
                    break;
            }
        
            //restore the original transformation
            g2.setTransform(oldTransform);
        
            //check for collisions with mobs
            collisionChecker.checkLaserCollision(laserX, laserY, gp.tileSize, gp.tileSize);
        }
        
    }
    //setter for the held item. Needed otherwise GamePanel will not run.
    public void setHeldItem(Item heldItem) 
    {
        this.heldItem = heldItem;
    }

    
}





