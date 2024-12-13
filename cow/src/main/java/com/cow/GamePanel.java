/**
 * Date: 12/13/24
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
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable 
{
    // SCREEN SETTINGS
    final int originalTileSize = 32;
    final int scale = 3;
    public int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;
    int FPS = 60;

    //game components
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    PlayerCow playerCow;
    WorldMap worldMap = new WorldMap(this);
    NPC npc;
    private AlienMob alien1;
    private AlienMob alien2;
    private Boss boss;

    private GameStateManager gameStateManager;
    EntityManager entityManager;
    private ItemManager itemManager;
    private CollisionChecker collisionChecker;

    //constructor
    public GamePanel() 
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        npc = new NPC(this);
        npc.x = 300;
        npc.y = 200;

        String[] alien1Images = {"/Alien1/Alien1.png", "/Alien1/Alien1.png", "/Alien1/Alien11.png", "/Alien1/Alien12.png", "/Alien1/Alien13.png", "/Alien1/Alien14.png"};
        String[] alien2Images = {"/Alien2/Alien2.png", "/Alien2/Alien2.png", "/Alien2/Alien21.png", "/Alien2/Alien22.png", "/Alien2/Alien23.png"};
        alien1 = new AlienMob(screenWidth / 3, 100, 2, 100, 10, alien1Images, FPS, FPS);
        alien2 = new AlienMob(2 * screenWidth / 3, 200, 2, 100, 10, alien2Images, FPS, FPS);

        boss = new Boss(screenWidth / 2 - tileSize / 2, screenHeight / 2 - tileSize / 2, 1, 200, 20);

        gameStateManager = new GameStateManager(this);
        itemManager = new ItemManager(this);
        collisionChecker = new CollisionChecker(this, alien1, alien2, boss, gameStateManager, itemManager);

        playerCow = new PlayerCow(this, keyH, collisionChecker); //create PlayerCow before EntityManager
        entityManager = new EntityManager(this, alien1, alien2, boss); //rreate EntityManager after PlayerCow
    }

    public void startGameThread() 
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() 
    {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) 
        {
            update();
            repaint();

            try 
            {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) 
                {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) 
            {
                //handle the interrupted exception
                break;
            }
        }
    }

    //updates game panel by checking key pressed and collisions
    public void update() 
    {
        gameStateManager.update();
        if (gameStateManager.getGameState() == GameState.PLAY) 
        {
            playerCow.update();
            entityManager.update();
            itemManager.update();

            if (keyH.spacePressed) 
            {
                collisionChecker.checkProximityAndKillAliens();
                keyH.spacePressed = false;
            }

            collisionChecker.checkCollisionWithSpaceship();

            if (keyH.pausePressed) 
            {
                gameStateManager.setGameState(GameState.PAUSE);
                keyH.pausePressed = false;
            }
        } else if (gameStateManager.getGameState() == GameState.PAUSE) 
        {
            if (keyH.pausePressed) 
            {
                gameStateManager.setGameState(GameState.PLAY);
                keyH.pausePressed = false;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameStateManager.getGameState() == GameState.PLAY)
        {
            worldMap.draw(g2);
            entityManager.draw(g2);
            itemManager.draw(g2);
        }

        gameStateManager.draw(g2);
        g2.dispose();
    }

    public ItemManager getItemManager()
    {
        return itemManager;
    }

    public Boss getBoss()
    {
        return boss;
    }

    public CollisionChecker getCollisionChecker()
    {
        return collisionChecker;
    }

    public void restartGame() 
    {
        //reset game components
        playerCow = new PlayerCow(this, keyH, collisionChecker);

        //reinitialize mobs
        String[] alien1Images = {"/Alien1/Alien1.png", "/Alien1/Alien1.png", "/Alien1/Alien11.png", "/Alien1/Alien12.png", "/Alien1/Alien13.png", "/Alien1/Alien14.png"};
        String[] alien2Images = {"/Alien2/Alien2.png", "/Alien2/Alien2.png", "/Alien2/Alien21.png", "/Alien2/Alien22.png", "/Alien2/Alien23.png"};
        alien1 = new AlienMob(screenWidth / 3, 100, 2, 100, 10, alien1Images, FPS, FPS);
        alien2 = new AlienMob(2 * screenWidth / 3, 200, 2, 100, 10, alien2Images, FPS, FPS);
        boss = new Boss(screenWidth / 2 - tileSize / 2, screenHeight / 2 - tileSize / 2, 1, 200, 20);

        entityManager = new EntityManager(this, alien1, alien2, boss);
        itemManager = new ItemManager(this);
        collisionChecker = new CollisionChecker(this, alien1, alien2, boss, gameStateManager, itemManager);

        gameStateManager.setGameState(GameState.START_SCREEN);
        gameStateManager.resetStartScreenCounter();

        //properly interrupt and join the current game thread
        if (gameThread != null) 
        {
            gameThread.interrupt();
            try 
            {
                gameThread.join();
            } catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }

        //restart the game thread
        startGameThread();
    }
}















    







