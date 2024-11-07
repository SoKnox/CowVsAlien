package com.cow;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable
{
    //SCREEN SETTINGS
    final int originalTilesSize = 16; //standard size for 2d pixel games... might do 32
    final int scale = 3;
    final int tileSize =  originalTilesSize * scale; //48x48 tile
    final int maxScreenCol = 20;
    final int maxScreenRow = 16;
    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreenRow;//526 pixels
    int FPS = 60;


    KeyHandler keyH = new KeyHandler ();
    //start and stop...keeps program running
    Thread gameThread;

    //set player's defult position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;


    //constructor
    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }


    public void startGameThread()
    {
        gameThread = new Thread(this); //passing gamepanel through thread
        gameThread.start();
    }
    @Override
    public void run() 
    {

        double drawInterval = 1000000000/FPS; //convert
        double nextDrawTime = System.nanoTime() + drawInterval;
        //game loop (as long as gameThread is running, it repeats this process)
        while (gameThread !=null)
        {
            long currentTime = System.nanoTime(); //very small unit
    

            //1) updates information like character permission
            update();
            //2)draw screen with updated info
            repaint();

           

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0)
                {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void update()
    {

        if(keyH.upPressed == true)
        {
            playerY = playerY - playerSpeed;
        }
        else if(keyH.downPressed == true)
        {
            playerY = playerY + playerSpeed;
        }
        else if(keyH.leftPressed == true)
        {
            playerX = playerX - playerSpeed;

        }
        else if(keyH.rightPressed == true)
        {
            playerX = playerX + playerSpeed;

        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }

    

}
