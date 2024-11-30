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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    // Game state variables
    private GameState gameState = GameState.START_SCREEN; // Initial state is START_SCREEN
    private int startScreenCounter = 0;
    private final int startScreenCycleDuration = 30; // Duration for each cycle in frames
    private final String[] startScreenImages = {
        "/CowSprites/CowRight.png",
        "/CowSprites/CowRight1.png",
        "/CowSprites/CowRight.png",
        "/CowSprites/CowRight2.png"
    };
    private BufferedImage[] startScreenImageBuffers;

    // SCREEN SETTINGS
    final int originalTileSize = 32; // Standard size for 2D pixel games... 16 and 64 are also standard. This one is best for some detail but not too much.
    final int scale = 3; // Scales tileSize
    public int tileSize = originalTileSize * scale; // 48x48 tile size
    final int maxScreenCol = 16; // Maximum number of columns
    final int maxScreenRow = 12; // Maximum number of rows
    final int screenWidth = tileSize * maxScreenCol; // Screen width in pixels
    final int screenHeight = tileSize * maxScreenRow; // Screen height in pixels
    int FPS = 60; // Frames per second

    // Game components
    KeyHandler keyH = new KeyHandler(); // Handles player's input
    Thread gameThread; // Game loop thread
    PlayerCow playerCow; // Player character (Cow)
    WorldMap worldMap = new WorldMap(this); // World map
    NPC npc; // NPC character
    private AlienMob alien1;
    private AlienMob alien2;
    private Boss boss; // Boss character

    private LinkedList<Integer> itemX = new LinkedList<>();
    private LinkedList<Integer> itemY = new LinkedList<>();
    private LinkedList<Item> droppedItems = new LinkedList<>();
    private String firstDroppedItem = null; // Tracks the first item dropped ("Sword" or "Key")

    // Constructor for the game panel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set panel size
        this.setBackground(Color.white); // Set background color to white (will change when creating map)
        this.setDoubleBuffered(true); // Enable double buffering for smooth rendering
        this.addKeyListener(keyH); // Add key listener for input handling
        this.setFocusable(true); // Allow the panel to receive key events

        // Initialize NPC
        npc = new NPC(this);
        npc.x = 300; // Set initial x position
        npc.y = 200; // Set initial y position

        // Initialize PlayerCow with the NPC instance
        playerCow = new PlayerCow(this, keyH);

        // Initialize AlienMob instances
        String[] alien1Images = {"/Alien1/Alien1.png", "/Alien1/Alien1.png", "/Alien1/Alien11.png", "/Alien1/Alien12.png", "/Alien1/Alien13.png", "/Alien1/Alien14.png"};
        String[] alien2Images = {"/Alien2/Alien2.png", "/Alien2/Alien2.png", "/Alien2/Alien21.png", "/Alien2/Alien22.png", "/Alien2/Alien23.png"};
        alien1 = new AlienMob(screenWidth / 3, 100, 2, 100, 10, alien1Images);
        alien2 = new AlienMob(2 * screenWidth / 3, 200, 2, 100, 10, alien2Images);

        // Initialize the boss at the center of the screen
        boss = new Boss(screenWidth / 2 - tileSize / 2, screenHeight / 2 - tileSize / 2, 1, 200, 20);

        // Load start screen images
        startScreenImageBuffers = new BufferedImage[startScreenImages.length];
        for (int i = 0; i < startScreenImages.length; i++) {
            try {
                startScreenImageBuffers[i] = ImageIO.read(getClass().getResourceAsStream(startScreenImages[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Start the game loop thread
    public void startGameThread() {
        gameThread = new Thread(this); // Create a new game loop thread
        gameThread.start(); // Start the game loop
    }

    // Game loop
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS; // Time interval between frames (in nanoseconds... more precise than milliseconds)
        double nextDrawTime = System.nanoTime() + drawInterval; // Sets the next frame time

        // Game loop: Keeps running as long as gameThread is active
        while (gameThread != null) {
            update(); // Update game state
            repaint(); // Repaint the screen to reflect changes

            try {
                double remainingTime = nextDrawTime - System.nanoTime(); // Time remaining until next frame
                remainingTime = remainingTime / 1000000; // Convert to milliseconds

                if (remainingTime < 0) {
                    remainingTime = 0; // Prevent negative time
                }

                // Sleep to control frame rate
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval; // Set next frame time
            } catch (InterruptedException e) {
                e.printStackTrace(); // Handle exceptions
            }
        }
    }

    // Updates player movements
    public void update() {
        if (gameState == GameState.START_SCREEN) {
            startScreenCounter++;
            if (startScreenCounter >= startScreenCycleDuration * 3 * startScreenImages.length) {
                gameState = GameState.PLAY; // Transition to play state after cycles
            }
        } else if (gameState == GameState.PLAY) {
            playerCow.update(); // Update the player's position

            alien1.updateAnimation();
            alien2.updateAnimation();
            boss.updateAnimation();

            // Check if space bar is pressed for attacking aliens
            if (keyH.spacePressed) {
                checkProximityAndKillAliens();
                keyH.spacePressed = false; // Reset the space bar flag
            }

            // Check collision with dropped items
            checkItemPickup();

            // Check collision with the spaceship
            checkCollisionWithSpaceship();

            // Check if pause key is pressed
            if (keyH.pausePressed) {
                gameState = GameState.PAUSE;
                keyH.pausePressed = false; // Reset the pause key flag
            }
        } else if (gameState == GameState.PAUSE) {
            // Check if pause key is pressed again to resume
            if (keyH.pausePressed) {
                gameState = GameState.PLAY;
                keyH.pausePressed = false; // Reset the pause key flag
            }
        }
    }

    private void checkItemPickup() {
        for (int i = 0; i < droppedItems.size(); i++) {
            Item item = droppedItems.get(i);

            // Check collision: Cow's bounding box overlaps item's position
            int cowCenterX = playerCow.x + tileSize / 2;
            int cowCenterY = playerCow.y + tileSize / 2;

            if (Math.abs(cowCenterX - item.getX()) < tileSize / 2 &&
                Math.abs(cowCenterY - item.getY()) < tileSize / 2) {

                if (item.getName().equals("Sword")) {
                    playerCow.setHeldItem(item); // Attach the sword to the cow
                    droppedItems.remove(i); // Remove the item from the dropped list
                    break;
                }
            }
        }
    }

    private void checkProximityAndKillAliens() {
        if (alien1.isAlive() && isWithinProximity(playerCow, alien1)) {
            Item loot = alien1.dropLoot(firstDroppedItem); // Handle alien1 loot
            if (loot != null) {
                droppedItems.add(loot);
                if (firstDroppedItem == null) {
                    firstDroppedItem = loot.getName();
                }
            }
            alien1.setHealth(0);
            alien1.setAlive(false);
        }

        if (alien2.isAlive() && isWithinProximity(playerCow, alien2)) {
            Item loot = alien2.dropLoot(firstDroppedItem); // Handle alien2 loot
            if (loot != null) {
                droppedItems.add(loot);
                if (firstDroppedItem == null) {
                    firstDroppedItem = loot.getName();
                }
            }
            alien2.setHealth(0);
            alien2.setAlive(false);
        }

        // Check proximity to the boss and kill it if within range
        if (boss.isAlive() && isWithinProximity(playerCow, boss)) {
            System.out.println("Boss defeated!");
            boss.setHealth(0);
            boss.setAlive(false);

            // Debug: Check the state of the boss and loot
            System.out.println("Boss health: " + boss.getHealth());
            System.out.println("Boss is alive: " + boss.isAlive());
            System.out.println("Spaceship loot: " + boss.getSpaceshipLoot());

            // Drop the boss loot
            Item loot = boss.dropLoot();
            if (loot != null) {
                System.out.println("Boss dropped loot: " + loot.getName());
                // No need to add to droppedItems list, as it's handled separately
            } else {
                System.out.println("Boss did not drop any loot.");
            }
        }
    }

    // Check if the player is within 150 pixels of the alien
    private boolean isWithinProximity(PlayerCow player, Mob mob) {
        int proximityDistance = 150;
        return Math.abs(player.x - mob.x) <= proximityDistance && Math.abs(player.y - mob.y) <= proximityDistance;
    }

    // Check collision with the spaceship
    private void checkCollisionWithSpaceship() {
        if (boss.getBossDropped() != null) {
            int cowCenterX = playerCow.x + tileSize / 2;
            int cowCenterY = playerCow.y + tileSize / 2;
            int spaceshipCenterX = boss.getBossDropped().getX() + tileSize / 2;
            int spaceshipCenterY = boss.getBossDropped().getY() + tileSize / 2;

            if (Math.abs(cowCenterX - spaceshipCenterX) < tileSize / 2 &&
                Math.abs(cowCenterY - spaceshipCenterY) < tileSize / 2) {
                gameState = GameState.GAME_OVER; // Set game state to GAME_OVER
            }
        }
    }

    // Draws game screen
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the screen
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == GameState.START_SCREEN) {
            drawStartScreen(g2);
        } else if (gameState == GameState.PLAY) {
            // Draw the world map
            worldMap.draw(g2);

            // Collect all entities (playerCow, NPC, AlienMobs, Boss)
            LinkedList<DrawableEntity> entities = new LinkedList<>();

            // Add playerCow
            entities.add(new DrawableEntity(playerCow.x, playerCow.y, tileSize, tileSize, graphics -> playerCow.draw(graphics)));

            // Add NPC
            entities.add(new DrawableEntity(npc.x, npc.y, tileSize, tileSize, graphics -> npc.draw(graphics)));

            // Add AlienMob1 if alive
            if (alien1.isAlive()) {
                entities.add(new DrawableEntity(alien1.x, alien1.y, tileSize, tileSize, graphics -> {
                    BufferedImage image = alien1.getCurrentImage();
                    if (image != null) {
                        graphics.drawImage(image, alien1.x, alien1.y, tileSize, tileSize, null);
                    }
                }));
            }

            // Add AlienMob2 if alive
            if (alien2.isAlive()) {
                entities.add(new DrawableEntity(alien2.x, alien2.y, tileSize, tileSize, graphics -> {
                    BufferedImage image = alien2.getCurrentImage();
                    if (image != null) {
                        graphics.drawImage(image, alien2.x, alien2.y, tileSize, tileSize, null);
                    }
                }));
            }

            // Add Boss if alive
            if (boss.isAlive()) {
                entities.add(new DrawableEntity(boss.x, boss.y, tileSize, tileSize, graphics -> {
                    BufferedImage image = boss.getBossImage();
                    if (image != null) {
                        graphics.drawImage(image, boss.x, boss.y, tileSize, tileSize, null);
                    }
                }));
            }

            // Sort entities by their y position
            entities.sort((e1, e2) -> Integer.compare(e1.y, e2.y));

            // Draw all entities in sorted order
            for (DrawableEntity entity : entities) {
                entity.draw(g2);
            }

            // Draw dropped items
            for (Item item : droppedItems) {
                if (item.getImage() != null) {
                    g2.drawImage(item.getImage(), item.getX(), item.getY(), tileSize, tileSize, null);
                }
            }

            // Draw the boss dropped item
            if (boss.getBossDropped() != null && boss.getBossDropped().getImage() != null) {
                g2.drawImage(boss.getBossDropped().getImage(), boss.getBossDropped().getX(), boss.getBossDropped().getY(), tileSize, tileSize, null);
            }
        } else if (gameState == GameState.PAUSE) {
            drawPauseScreen(g2);
        } else if (gameState == GameState.GAME_OVER) {
            drawGameOverScreen(g2);
        }

        g2.dispose(); // Clean up the Graphics2D object
    }

    private void drawStartScreen(Graphics2D g2) {
        int imageIndex = (startScreenCounter / startScreenCycleDuration) % startScreenImages.length;
        BufferedImage image = startScreenImageBuffers[imageIndex];
        if (image != null) {
            g2.drawImage(image, screenWidth / 2 - tileSize / 2, screenHeight / 2 - tileSize / 2, tileSize, tileSize, null);
        }
    }

    private void drawPauseScreen(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 72));
        String pauseText = "PAUSED";
        int x = screenWidth / 2 - g2.getFontMetrics().stringWidth(pauseText) / 2;
        int y = screenHeight / 2 - g2.getFontMetrics().getHeight() / 2;
        g2.drawString(pauseText, x, y);
    }

    private void drawGameOverScreen(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setColor(Color.RED);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 72));
        String gameOverText = "GAME OVER";
        int x = screenWidth / 2 - g2.getFontMetrics().stringWidth(gameOverText) / 2;
        int y = screenHeight / 2 - g2.getFontMetrics().getHeight() / 2;
        g2.drawString(gameOverText, x, y);
    }

    // Helper class for sorting and drawing
    private class DrawableEntity {
        int x, y, width, height;
        java.util.function.Consumer<Graphics2D> drawFunction;

        DrawableEntity(int x, int y, int width, int height, java.util.function.Consumer<Graphics2D> drawFunction) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.drawFunction = drawFunction;
        }

        void draw(Graphics2D g) {
            drawFunction.accept(g);
        }
    }
}





    







