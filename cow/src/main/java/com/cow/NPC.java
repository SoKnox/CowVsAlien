package com.cow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NPC extends Being {

    private String[] dialogue;
    private BufferedImage image;
    private boolean showDialogue;
    private GamePanel gp; // Reference to the GamePanel

    public NPC(GamePanel gp, String[] dialogue) {
        this.gp = gp;
        this.dialogue = dialogue;
        loadImage();
        showDialogue = false;
    }

    private void loadImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/NPC/Chicken.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void interact() {
        showDialogue = true;
    }

    public void draw(Graphics2D g2) {
        if (image != null) {
            int scaledWidth = gp.tileSize / 2;
            int scaledHeight = gp.tileSize / 2;
            g2.drawImage(image, x, y, scaledWidth, scaledHeight, null);
        }

        if (showDialogue) {
            drawDialogueBox(g2);
        }
    }

    private void drawDialogueBox(Graphics2D g2) {
        int boxWidth = 100;
        int boxHeight = 25;
        int boxX = x;
        int boxY = y - boxHeight;

        g2.setColor(Color.BLACK);
        g2.fillRect(boxX, boxY, boxWidth, boxHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Times New Roman", Font.PLAIN, 12));

        int lineHeight = g2.getFontMetrics().getHeight();
        int yOffset = boxY + lineHeight;

        for (String line : dialogue) {
            g2.drawString(line, boxX + 10, yOffset);
            yOffset += lineHeight;
        }
    }

    public boolean isShowingDialogue() {
        return showDialogue;
    }

    public void setShowDialogue(boolean showDialogue) {
        this.showDialogue = showDialogue;
    }
}
