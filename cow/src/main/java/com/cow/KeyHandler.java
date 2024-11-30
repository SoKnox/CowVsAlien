/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class handles key input for controlling the movement of the player (Cow) character.
 * W, A, S, D keys control upPressed, downPressed, leftPressed, rightPressed respectivly.
 *  
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

    //handles key press events
    @Override
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode(); //gets the key code for the pressed key

        if(code == KeyEvent.VK_W)
        {
            upPressed = true;
        }
        if(code == KeyEvent.VK_S)
        {
            downPressed = true;
        }
        if(code == KeyEvent.VK_A)
        {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D)
        {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SPACE)
        {
            spacePressed = true;
        }
        if(code == KeyEvent.VK_P)
        {
            pausePressed = true;
        }
    }

    //handles key release events
    @Override
    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode(); //gets the key code for the released key

        //resets the movement flag based on the key released
        if(code == KeyEvent.VK_W)
        {
            upPressed = false;
        }
        if(code == KeyEvent.VK_S)
        {
            downPressed = false;
        }
        if(code == KeyEvent.VK_A)
        {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D)
        {
            rightPressed = false;
        }
        if(code == KeyEvent.VK_SPACE)
        {
            spacePressed = false;
        }
        if(code == KeyEvent.VK_P)
        {
            pausePressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        //not needed for this implementation
    }
}
