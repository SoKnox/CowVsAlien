/**
 * Date: 11/11/24
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
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 32; // standard size for 2D pixel games... 16 and 64 are also standard. This one is best for some detail but not too much.
    final int scale = 3; // scales tileSize
    public int tileSize = originalTileSize * scale; // 48x48 tile size
    final int maxScreenCol = 16; // maximum number of columns
    final int maxScreenRow = 12; // maximum number of rows
    final int screenWidth = tileSize * maxScreenCol; // screen width in pixels
    final int screenHeight = tileSize * maxScreenRow; // screen height in pixels
    int FPS = 60; // frames per second

    // game components
    KeyHandler keyH = new KeyHandler(); // handles player's input
    Thread gameThread; // game loop thread
    PlayerCow playerCow; // player character (Cow)
    WorldMap worldMap = new WorldMap(this); // world map
    NPC npc; // NPC character

    // constructor for the game panel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // set panel size
        this.setBackground(Color.white); // set background color to white (will change when creating map)
        this.setDoubleBuffered(true); // enable double buffering for smooth rendering
        this.addKeyListener(keyH); // add key listener for input handling
        this.setFocusable(true); // allow the panel to receive key events

        // Initialize NPC with some dialogue
        String[] dialogue = {"Cluck cluck!"};
        npc = new NPC(this, dialogue);
        npc.x = 300; // Set initial x position
        npc.y = 200; // Set initial y position

        // Initialize PlayerCow with the NPC instance
        playerCow = new PlayerCow(this, keyH, npc);
    }

    // start the game loop thread
    // Game loop: The continuous cycle of updating and rendering the game state
    // Game loop thread: runs the game loop independently and ensures smooth game performance
    public void startGameThread() {
        gameThread = new Thread(this); // create a new game loop thread
        gameThread.start(); // start the game loop
    }

    // game loop
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // time interval between frames (in nanoseconds... more precise than milliseconds)
        double nextDrawTime = System.nanoTime() + drawInterval; // sets the next frame time

        // game loop: keeps running as long as gameThread is active
        while (gameThread != null) {
            update(); // update game state
            repaint(); // repaint the screen to reflect changes

            try {
                double remainingTime = nextDrawTime - System.nanoTime(); // time remaining until next frame
                remainingTime = remainingTime / 1000000; // convert to milliseconds

                if (remainingTime < 0) {
                    remainingTime = 0; // prevent negative time
                }

                // sleep to control frame rate
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval; // set next frame time
            } catch (InterruptedException e) {
                e.printStackTrace(); // handle exceptions
            }
        }
    }

    // updates player movements
    public void update() {
        playerCow.update(); // update the player's position
    }

    // draws game screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call the superclass's paintComponent method
        Graphics2D g2 = (Graphics2D) g; // cast Graphics to Graphics2D for advanced drawing

        // draw the world map
        worldMap.draw(g2);

        // draw the player character
        playerCow.draw(g2);

        // draw the NPC
        npc.draw(g2);

        g2.dispose(); // clean up the Graphics2D object
    }
}
