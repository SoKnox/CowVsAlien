/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This is the player class that creates the cow (the player's) animation movements according to KeyPressed and KeyReleased. 
 * It cloads all the cow sprite images creates an animation cycle.
 * Buffer Image Used because it hides all the complexity of different types of images whilst allowing access to the underlying data.
 */

 package com.cow;

 import java.awt.Graphics2D;
 import java.awt.geom.AffineTransform;
 import java.awt.image.BufferedImage;
 import java.io.IOException;
 import javax.imageio.ImageIO;
 
 public class PlayerCow extends Being {
 
     GamePanel gp;
     KeyHandler keyH;
     private int animationCounter = 0; // Counter for cycling images
     private Item heldItem = null;
 
     // Constructor to initialize the player cow's position, speed, and images
     public PlayerCow(GamePanel gp, KeyHandler keyH) {
         this.gp = gp;
         this.keyH = keyH;
         x = 100; // Initial x position
         y = 100; // Initial y position
         speed = 4; // Speed of movement
         getPlayerImage(); // Load the player images
         direction = "down"; // Default direction is down
     }
 
     // Load all the player cow images
     public void getPlayerImage() {
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
         } catch (IOException e) {
             e.printStackTrace(); // Exception if image loading fails
         }
     }
 
     // Updates the cow's position and animation cycle
     public void update() {
         boolean moving = false; // Checks if the player is moving
 
         // Check for key presses and update position/direction
         if (keyH.upPressed) {
             direction = "up"; // Set direction to up
             y -= speed; // Move up
             moving = true;
         } else if (keyH.downPressed) {
             direction = "down"; // Set direction to down
             y += speed; // Move down
             moving = true;
         } else if (keyH.leftPressed) {
             direction = "left"; // Set direction to left
             x -= speed; // Move left
             moving = true;
         } else if (keyH.rightPressed) {
             direction = "right"; // Set direction to right
             x += speed; // Move right
             moving = true;
         }
 
         // If moving, increment the animation counter to cycle images
         if (moving) {
             animationCounter++;
         } else {
             animationCounter = 0; // Reset animation when not moving
         }
     }
 
     // Draws the cow's current image on the screen
     public void draw(Graphics2D g2) {
         BufferedImage image = null; // Holds the current image
 
         // Calculate the position in the animation cycle (0 or 1) based on animation counter
         int cyclePosition = (animationCounter / 10) % 2; // Only two frames for each direction animation
 
         // Determine the image based on the direction and whether the player is moving
         switch (direction) {
             case "up":
                 if (animationCounter == 0) {
                     image = up;
                 } else {
                     image = (cyclePosition == 0) ? up1 : up2;
                 }
                 break;
             case "down":
                 if (animationCounter == 0) {
                     image = down;
                 } else {
                     image = (cyclePosition == 0) ? down1 : down2;
                 }
                 break;
             case "left":
                 if (animationCounter == 0) {
                     image = left;
                 } else {
                     image = (cyclePosition == 0) ? left2 : left1;
                 }
                 break;
             case "right":
                 if (animationCounter == 0) {
                     image = right;
                 } else {
                     image = (cyclePosition == 0) ? right1 : right2;
                 }
                 break;
         }
 
         // Draw the sword if the cow is holding it and not facing down
         if (heldItem != null && heldItem.getName().equals("Sword") && !direction.equals("down")) {
             float swordX, swordY;
 
             // Adjusts the sword's position based on the direction
             switch (direction) {
                 case "left":
                     swordX = x - gp.tileSize / 3.25f;
                     swordY = y + gp.tileSize / 2f - gp.tileSize / 1.35f;
 
                     // Flip the sword image horizontally
                     g2.drawImage(heldItem.getImage(), (int) (swordX + gp.tileSize / 2f), (int) swordY, -gp.tileSize, gp.tileSize, null);
                     break;
                 case "right":
                     swordX = x + gp.tileSize - gp.tileSize / 4.25f;
                     swordY = y + gp.tileSize / 2f - gp.tileSize / 1.35f;
                     g2.drawImage(heldItem.getImage(), (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);
                     break;
                 case "up":
                     swordX = x + gp.tileSize / 2f - gp.tileSize / 8f;
                     swordY = y - gp.tileSize / 4f;
                     g2.drawImage(heldItem.getImage(), (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);
                     break;
                 default:
                     swordX = x + gp.tileSize / 2f - gp.tileSize / 4.5f;
                     swordY = y + gp.tileSize / 2f - gp.tileSize / .95f;
                     g2.drawImage(heldItem.getImage(), (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);
                     break;
             }
         }
 
         // Draw the selected cow image on the screen at the player's position
         if (image != null) {
             g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
         }
 
         // Draw the sword on top of the cow when facing down and rotate it by 15 degrees
         if (heldItem != null && heldItem.getName().equals("Sword") && direction.equals("down")) {
             float swordX = x + gp.tileSize / 2f - gp.tileSize / 16f; // Horizontal
             float swordY = y + gp.tileSize - gp.tileSize / 1.3f; // Vertical
 
             // Save the current transformation
             AffineTransform oldTransform = g2.getTransform();
 
             // Rotate the graphics context by x degrees around the center of the sword
             g2.rotate(Math.toRadians(32), swordX + gp.tileSize / 2, swordY + gp.tileSize / 2);
 
             // Draw the rotated sword
             g2.drawImage(heldItem.getImage(), (int) swordX, (int) swordY, gp.tileSize, gp.tileSize, null);
 
             // Restore the original transformation
             g2.setTransform(oldTransform);
         }
     }
 
     // Setter for the held item. Needed otherwise GamePanel will not run.
     public void setHeldItem(Item heldItem) {
         this.heldItem = heldItem;
     }
 }
 
 
 
     