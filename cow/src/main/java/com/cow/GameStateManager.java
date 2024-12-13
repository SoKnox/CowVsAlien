/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents a game state manager in the game, which includes methods to update the game state,
 * draw different screens, handle mouse events, and manage the game state transitions.
 */

package com.cow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameStateManager implements MouseListener 
{
    private GameState gameState = GameState.START_SCREEN;
    private int startScreenCounter = 0;
    private final int startScreenCycleDuration = 60; //increased duration for slower image cycling
    private final String[] startScreenImages = 
    {
        "/Start/Start1.png",
        "/Start/Start2.png",
        "/Start/Start3.png",
        "/Start/Start4.png",
        "/Start/Start5.png",
        "/Start/Start6.png",
        "/Start/Start7.png",
        "/Start/Start8.png",
        "/Start/Start9.png",
        "/Start/Start10.png",
        "/Start/Start11.png",
        "/Start/Start12.png"
    };
    private BufferedImage[] startScreenImageBuffers;
    private BufferedImage gameOverImage; //image for the game over screen
    private BufferedImage[] cowAnimationImages; //images for the cow animation
    private int cowAnimationCounter = 0;
    private final int cowAnimationCycleDuration = 10; //duration for each frame of the cow animation
    private GamePanel gamePanel;
    private final int margin = 200; //margin around the images
    private final int verticalOffset = 200; //vertical offset to move the images up

    public GameStateManager(GamePanel gamePanel) 
    {
        this.gamePanel = gamePanel;
        startScreenImageBuffers = new BufferedImage[startScreenImages.length];
        for (int i = 0; i < startScreenImages.length; i++) 
        {
            try 
            {
                startScreenImageBuffers[i] = ImageIO.read(getClass().getResourceAsStream(startScreenImages[i]));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //load the game over image
        try {
            gameOverImage = ImageIO.read(getClass().getResourceAsStream("/GameOver/Restart.png"));
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        //initialize cow animation images for pause
        cowAnimationImages = new BufferedImage[3];
        try 
        {
            cowAnimationImages[0] = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowRight.png"));
            cowAnimationImages[1] = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowRight1.png"));
            cowAnimationImages[2] = ImageIO.read(getClass().getResourceAsStream("/CowSprites/CowRight2.png"));
        } catch (IOException e) 
        {
            e.printStackTrace();
        }

        //add mouse listener to the game panel
        gamePanel.addMouseListener(this);
    }

    public void update() 
    {
        if (gameState == GameState.START_SCREEN) 
        {
            startScreenCounter++;
            if (startScreenCounter >= startScreenCycleDuration * startScreenImages.length || gamePanel.keyH.skipPressed) 
            {
                gameState = GameState.PLAY;
                gamePanel.keyH.skipPressed = false; //reset the skip flag
            }
        } else if (gameState == GameState.PLAY) 
        {
            cowAnimationCounter = 0; //reset animation counters if needed
        } else if (gameState == GameState.PAUSE) 
        {
            cowAnimationCounter++;
        }
    }

    public void draw(Graphics2D g2) 
    {
        if (gameState == GameState.START_SCREEN) 
        {
            drawStartScreen(g2);
        } else if (gameState == GameState.PAUSE) 
        {
            drawPauseScreen(g2);
        } else if (gameState == GameState.GAME_OVER) 
        {
            drawGameOverScreen(g2);
        }
    }

    private void drawStartScreen(Graphics2D g2) 
    {
        int imageIndex = (startScreenCounter / startScreenCycleDuration) % startScreenImages.length;
        BufferedImage image = startScreenImageBuffers[imageIndex];
        if (image != null) 
        {
            int imageWidth = gamePanel.screenWidth - 2 * margin;
            int imageHeight = gamePanel.screenHeight - 2 * margin;

            //draw the image with the margin and vertical offset
            g2.drawImage(image, margin, margin - verticalOffset, imageWidth, imageHeight, null);
        }
    }

    private void drawPauseScreen(Graphics2D g2) 
    {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        int imageIndex = (cowAnimationCounter / cowAnimationCycleDuration) % cowAnimationImages.length;
        BufferedImage image = cowAnimationImages[imageIndex];
        if (image != null) 
        {
            //Pause screen
            int imageWidth = gamePanel.tileSize * 2; 
            int imageHeight = gamePanel.tileSize * 2; 

            //draw the cow animation image in the center of the screen
            int x = gamePanel.screenWidth / 2 - imageWidth / 2;
            int y = gamePanel.screenHeight / 2 - imageHeight / 2;
            g2.drawImage(image, x, y, imageWidth, imageHeight, null);
        }
    }

    private void drawGameOverScreen(Graphics2D g2) 
    {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        if (gameOverImage != null) 
        {
            
            int imageWidth = gamePanel.screenWidth - 2 * margin;
            int imageHeight = gamePanel.screenHeight - 2 * margin;

            //draw the game over image with the margin and vertical offset
            g2.drawImage(gameOverImage, margin, margin - verticalOffset, imageWidth, imageHeight, null);
        }
    }

    public GameState getGameState() 
    {
        return gameState;
    }

    public void setGameState(GameState gameState) 
    {
        this.gameState = gameState;
    }

    public void resetStartScreenCounter() 
    {
        startScreenCounter = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        if (gameState == GameState.GAME_OVER) 
        {
            //restart the game
            gamePanel.restartGame();
        } else if (gameState == GameState.START_SCREEN) 
        {
            //transition to the PLAY state directly when clicked on the start screen
            gameState = GameState.PLAY;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        //not used
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        //not used
    }

    @Override
    public void mouseEntered(MouseEvent e) 
    {
        //not used
    }

    @Override
    public void mouseExited(MouseEvent e) 
    {
        //not used
    }
}






