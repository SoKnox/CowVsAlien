/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class handles the main game panel.
 * It sets up the game screen, manages game loop, updates the player character, 
 * and draws the player's current state on the screen.
 */
package com.cow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable 
{

    // SCREEN SETTINGS
    final int originalTileSize = 32; //standard size for 2D pixel games... 16 and 64 are also standard. This one is best for some detail but not too much.
    final int scale = 3; //scales tileSize
    public int tileSize = originalTileSize * scale; //48x48 tile size
    final int maxScreenCol = 16; //maximum number of columns
    final int maxScreenRow = 12; //maximum number of rows
    final int screenWidth = tileSize * maxScreenCol; //screen width in pixels
    final int screenHeight = tileSize * maxScreenRow; //screen height in pixels
    int FPS = 60; //frames per second

    // Game components
    KeyHandler keyH = new KeyHandler(); //handles player's input
    Thread gameThread; //game loop thread
    PlayerCow playerCow; //player character (Cow)
    WorldMap worldMap = new WorldMap(this); //world map
    NPC npc; //NPC character
    private AlienMob alien1;
    private AlienMob alien2;

    private Item droppedItems; //head of the linked list of dropped items
    private String firstDroppedItem = null; //tracks the first item dropped ("Sword" or "Key")

    //constructor for the game panel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set panel size
        this.setBackground(Color.white); //set background color to white (will change when creating map)
        this.setDoubleBuffered(true); //enable double buffering for smooth rendering
        this.addKeyListener(keyH); //add key listener for input handling
        this.setFocusable(true); //allow the panel to receive key events

        //initialize NPC with some dialogue
        String[] dialogue = {"Cluck cluck!"};
        npc = new NPC(this, dialogue);
        npc.x = 300; //set initial x position
        npc.y = 200; //set initial y position

        //initialize PlayerCow with the NPC instance
        playerCow = new PlayerCow(this, keyH, npc);

        //initialize AlienMob instances
        String[] alien1Images = {"/Alien1/Alien1.png", "/Alien1/Alien1.png", "/Alien1/Alien11.png", "/Alien1/Alien12.png", "/Alien1/Alien13.png", "/Alien1/Alien14.png"};
        String[] alien2Images = {"/Alien2/Alien2.png", "/Alien2/Alien2.png", "/Alien2/Alien21.png", "/Alien2/Alien22.png", "/Alien2/Alien23.png"};
        alien1 = new AlienMob(screenWidth / 3, 100, 2, 100, 10, alien1Images);
        alien2 = new AlienMob(2 * screenWidth / 3, 200, 2, 100, 10, alien2Images);
    }

    //start the game loop thread
    //Game loop: The continuous cycle of updating and rendering the game state
    //Game loop thread: Runs the game loop independently and ensures smooth game performance
    public void startGameThread() 
    {
        gameThread = new Thread(this); //create a new game loop thread
        gameThread.start(); //start the game loop
    }

    //game loop
    @Override
    public void run() 
    {
        double drawInterval = 1000000000.0 / FPS; //time interval between frames (in nanoseconds... more precise than milliseconds)
        double nextDrawTime = System.nanoTime() + drawInterval; //sets the next frame time

        //game loop: Keeps running as long as gameThread is active
        while (gameThread != null) 
        {
            update(); //update game state
            repaint(); //repaint the screen to reflect changes

            try 
            {
                double remainingTime = nextDrawTime - System.nanoTime(); //time remaining until next frame
                remainingTime = remainingTime / 1000000; //convert to milliseconds

                if (remainingTime < 0) 
                {
                    remainingTime = 0; //prevent negative time
                }

                //sleep to control frame rate
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval; //set next frame time
            } catch (InterruptedException e) 
            {
                e.printStackTrace(); //handle exceptions
            }
        }
    }

    //updates player movements
    public void update() 
    {
        playerCow.update(); //update the player's position

        alien1.updateAnimation();
        alien2.updateAnimation();

        //check if space bar is pressed for attacking aliens
        if (keyH.spacePressed) 
        {
            checkProximityAndKillAliens();
            keyH.spacePressed = false; //reset the space bar flag
        }

        //check collision with dropped items
        checkItemPickup();
    }

    private void checkItemPickup() 
    {
        Item current = droppedItems; //start from the head of the list

        //go through the list until the end (nextItem == null)
        while (current != null) 
        {
            //check collision: Cow's bounding box overlaps item's position
            int cowCenterX = playerCow.x + tileSize / 2;
            int cowCenterY = playerCow.y + tileSize / 2;

            if (Math.abs(cowCenterX - current.getX()) < tileSize / 2 &&
                Math.abs(cowCenterY - current.getY()) < tileSize / 2) 
                {

                if (current.getName().equals("Sword")) 
                {
                    playerCow.setHeldItem(current); //attach the sword to the cow
                    droppedItems = current.getNextItem(); //remove the item from the dropped list
                    break;
                }
            }

            current = current.getNextItem(); //move to the next item in the list
        }
    }

    //check proximity to aliens and kill them if within range
    private void checkProximityAndKillAliens() 
    {
        if (alien1.isAlive() && isWithinProximity(playerCow, alien1)) 
        {
            Item loot = alien1.dropLoot(firstDroppedItem); //pass the dropped item to restrict options
            if (loot != null) 
            {
                if (droppedItems == null) 
                {
                    droppedItems = loot; //set the head of the list
                } else 
                {
                    Item current = droppedItems;
                    while (current.getNextItem() != null) 
                    {
                        current = current.getNextItem(); //move to the last item in the list
                    }
                    current.setNextItem(loot); //add the new item to the end of the list
                }
                if (firstDroppedItem == null) 
                {
                    firstDroppedItem = loot.getName(); //record the first dropped item
                }
            }
            alien1.setHealth(0);
            alien1.setAlive(false);
        }

        if (alien2.isAlive() && isWithinProximity(playerCow, alien2)) 
        {
            Item loot = alien2.dropLoot(firstDroppedItem); //pass the dropped item to restrict options
            if (loot != null) 
            {
                if (droppedItems == null) 
                {
                    droppedItems = loot; //set the head of the list
                } else 
                {
                    Item current = droppedItems;
                    while (current.getNextItem() != null)
                    {
                        current = current.getNextItem(); //move to the last item in the list
                    }
                    current.setNextItem(loot); //add the new item to the end of the list
                }
                if (firstDroppedItem == null) 
                {
                    firstDroppedItem = loot.getName(); //record the first dropped item
                }
            }
            alien2.setHealth(0);
            alien2.setAlive(false);
        }
    }

    //check if the player is within 150 pixels of the alien
    private boolean isWithinProximity(PlayerCow player, AlienMob alien) 
    {
        int proximityDistance = 150;
        return Math.abs(player.x - alien.x) <= proximityDistance && Math.abs(player.y - alien.y) <= proximityDistance;
    }

    //draws game screen
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g); //call the superclass's paintComponent method
        Graphics2D g2 = (Graphics2D) g; //cast Graphics to Graphics2D for advanced drawing

        //draw the world map
        worldMap.draw(g2);

        //draw the player character
        playerCow.draw(g2);

        //draw the NPC
        npc.draw(g2);

        //draw AlienMob instances
        if (alien1.isAlive()) 
        {
            BufferedImage image = alien1.getCurrentImage();
            if (image != null) 
            {
                g2.drawImage(image, alien1.x, alien1.y, tileSize, tileSize, null);
            }
        }
        if (alien2.isAlive()) 
        {
            BufferedImage image = alien2.getCurrentImage();
            if (image != null) 
            {
                g2.drawImage(image, alien2.x, alien2.y, tileSize, tileSize, null);
            }
        }

        //draw dropped items
        Item current = droppedItems; //start from the head of the list
        while (current != null) 
        {
            if (current.getImage() != null) 
            {
                g2.drawImage(current.getImage(), current.getX(), current.getY(), tileSize, tileSize, null);
            }
            current = current.getNextItem(); //move to the next item in the list
        }

        g2.dispose(); //clean up the Graphics2D object
    }
}





