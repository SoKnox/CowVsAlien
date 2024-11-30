/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This is the main class that initializes and runs the "Cow vs Alien" game. 
 * It creates a window (JFrame), sets up the game panel, and starts the game thread.
 */
package com.cow;

import javax.swing.*;

public class Main 
{
    public static void main(String[] args) 
    {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Cow vs Alien");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}



