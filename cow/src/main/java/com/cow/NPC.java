/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: Represents a non-player character (the chicken) in the game. Handles NPC interactions and dialogue display.
 */

package com.cow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NPC extends Being
{

    private String[] dialogue; //array to hold the dialogue lines
    private BufferedImage image; //image of the NPC
    private boolean showDialogue; //flag to indicate if dialogue should be shown
    private GamePanel gp; //reference to the GamePanel

    public NPC(GamePanel gp, String[] dialogue)
    {
        this.gp = gp; //initialize the GamePanel reference
        this.dialogue = dialogue; //initialize the dialogue lines
        loadImage(); //load the NPC image
        showDialogue = false; //initialize the dialogue flag to false
    }

    private void loadImage()
    {
        try
        {
            //load the image for the NPC
            image = ImageIO.read(getClass().getResourceAsStream("/NPC/Chicken.png"));
        } catch (IOException e)
        {
            e.printStackTrace(); //handle exceptions
        }
    }

    public void interact()
    {
        showDialogue = true; //set the dialogue flag to true when interacting
    }

    public void draw(Graphics2D g2)
    {
        if (image != null) {
            int scaledWidth = gp.tileSize / 2; //calculate the scaled width
            int scaledHeight = gp.tileSize / 2; //calculate the scaled height
            g2.drawImage(image, x, y, scaledWidth, scaledHeight, null); //draw the NPC image
        }

        if (showDialogue)
        {
            drawDialogueBox(g2); //draw the dialogue box if the flag is true
        }
    }

    private void drawDialogueBox(Graphics2D g2)
    {
        int boxWidth = 100; //width of the dialogue box
        int boxHeight = 25; //height of the dialogue box
        int boxX = x; //x position of the dialogue box
        int boxY = y - boxHeight; //y position of the dialogue box

        g2.setColor(Color.BLACK); //set the color of the dialogue box
        g2.fillRect(boxX, boxY, boxWidth, boxHeight); //draw the dialogue box

        g2.setColor(Color.WHITE); //set the color of the text
        g2.setFont(new Font("Times New Roman", Font.PLAIN, 12)); //set the font of the text

        int lineHeight = g2.getFontMetrics().getHeight(); //get the height of a line of text
        int yOffset = boxY + lineHeight; //calculate the initial y offset for the text

        for (String line : dialogue)
        {
            g2.drawString(line, boxX + 10, yOffset); //draw each line of dialogue
            yOffset += lineHeight; //update the y offset for the next line
        }
    }

    public boolean isShowingDialogue()
    {
        return showDialogue; //return the value of the dialogue flag
    }

    public void setShowDialogue(boolean showDialogue)
    {
        this.showDialogue = showDialogue; //set the value of the dialogue flag
    }
}
