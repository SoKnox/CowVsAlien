/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class handles key input for controlling the movement of the player (Cow) character.
 * W, A, S, D keys control upPressed, downPressed, leftPressed, rightPressed respectivly.
 * P activates pause screen
 * Clicking during start screen or game over screen moves to next game state.
 * SpaceBar release causes laser animation
 * Holding spaceBar creates repeating power orb animation
 */
package com.cow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener 
{
    //movement flags for each direction
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean spacePressed; //flag for space bar press
    public boolean pausePressed; //flag for pause key press
    public boolean skipPressed; //flag for skip key press
    private boolean spaceHeld; //flag for space bar being held down

    //handles key press events
    @Override
    public void keyPressed(KeyEvent e) 
    {
        int code = e.getKeyCode(); //gets the key code for the pressed key

        if (code == KeyEvent.VK_W) 
        {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) 
        {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) 
        {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) 
        {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) 
        {
            if (!spacePressed) 
            { //ensure this only sets true once per key press
                spacePressed = true;
                spaceHeld = true;
            }
        }
        if (code == KeyEvent.VK_P) 
        {
            pausePressed = true;
        }
        if (code == KeyEvent.VK_ENTER) 
        {
            skipPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
        int code = e.getKeyCode(); //gets the key code for the released key

        if (code == KeyEvent.VK_W) 
        {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) 
        {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) 
        {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) 
        {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) 
        {
            spacePressed = false;
            spaceHeld = false;
        }
        if (code == KeyEvent.VK_P) 
        {
            pausePressed = false;
        }
        if (code == KeyEvent.VK_ENTER) 
        {
            skipPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {
        //not needed for this implementation
    }

    public boolean isSpaceHeld() 
    {
        return spaceHeld;
    }
}


