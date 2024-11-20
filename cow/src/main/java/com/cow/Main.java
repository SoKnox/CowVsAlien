/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This is the main class that initializes and runs the "Cow vs Alien" game. 
 * It creates a window (JFrame), sets up the game panel, and starts the game thread.
 */

 package com.cow;

 import javax.swing.JFrame;
 
 public class Main 
 {
     
     
      //initializes the game window + game panel, and then starts the game thread
     public static void main(String[] args) 
     {
         //creates a new JFrame window
         JFrame window = new JFrame();
         
         //sets the default close operation to exit the application when the window is closed
         window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
         //sets the window title to "Cow vs Alien"
         window.setTitle("Cow vs Alien");
 
         //creates a new GamePanel to hold the game's content
         GamePanel gamePanel = new GamePanel();
         
         //adds the game panel to the window
         window.add(gamePanel);
         
         //adjust the window size to fit the preferred size of the game panel
         window.pack();
         
         //sets the window location to the center of the screen
         window.setLocationRelativeTo(null);
         
         //makes the window visible
         window.setVisible(true);
 
         //starts the game thread which rusn the game loop
         gamePanel.startGameThread();
     }
 }
 