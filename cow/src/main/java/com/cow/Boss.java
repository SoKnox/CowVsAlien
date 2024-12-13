/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents a boss mob in the game, which includes its position, health, damage,
 * animation images, and a loot item that the boss drops.
 */
package com.cow;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Boss extends Mob 
{

    private int damage;
    private BufferedImage[] bossImages;
    private int currentImageIndex;
    private Item spaceshipLoot; //spaceship item that the boss drops
    private Item bossDropped; //holds the dropped spaceship
    private int animationTimer;
    private int animationInterval = 10;
    private boolean reversing = false; //flag to indicate if the cycle is reversing

    public Boss(int x, int y, int speed, int health, int damage) 
    {
        super(x, y, speed, health, 100, 100); 
        this.damage = damage;
        loadBossImages();
        loadLootItem();
        currentImageIndex = 0; //start with the first image
    }

    //loads Boss images
    private void loadBossImages() 
    {
        bossImages = new BufferedImage[3];
        try 
        {
            bossImages[0] = ImageIO.read(getClass().getResourceAsStream("/Boss/Boss1.png"));
            bossImages[1] = ImageIO.read(getClass().getResourceAsStream("/Boss/Boss2.png"));
            bossImages[2] = ImageIO.read(getClass().getResourceAsStream("/Boss/Boss3.png"));
        } catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Failed to load boss images.");
        }
    }

    private void loadLootItem() 
    {
        try 
        {
            //load only the spaceship loot
            spaceshipLoot = new Item("SpaceShip", this.x, this.y,
                    ImageIO.read(getClass().getResourceAsStream("/Items/SpaceShip.png")));
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public Item dropLoot() 
    {
        if (!isAlive() && spaceshipLoot != null) 
        {
            bossDropped = new Item(spaceshipLoot.getName(), this.x, this.y, spaceshipLoot.getImage());
            spaceshipLoot = null; //clear the loot after it’s been dropped
            return bossDropped;
        }
        return null; //no loot to drop if boss is not dead or loot is already dropped
    }

    //scales/resizes boss image
    public BufferedImage getBossImage() 
    {
        BufferedImage originalImage = bossImages[currentImageIndex];
        int scaledWidth = originalImage.getWidth() * 2;
        int scaledHeight = originalImage.getHeight() * 2;
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g2.dispose();
        return scaledImage;
    }

    public Item getBossDropped() 
    {
        return bossDropped;
    }

    public Item getSpaceshipLoot() 
    {
        return spaceshipLoot;
    }

    public void updateAnimation() 
    {
        animationTimer++;
        if (animationTimer >= animationInterval) 
        {
            animationTimer = 0;
            //update the animation cycle
            if (!reversing) {
                if (currentImageIndex < 2) 
                {
                    currentImageIndex++; //Boss1 -> Boss2 -> Boss3
                } else 
                {
                    reversing = true; //start reversing
                }
            } else {
                if (currentImageIndex > 0) 
                {
                    currentImageIndex--; //Boss3 -> Boss2 -> Boss1
                } else 
                {
                    reversing = false; // start moving forward again
                }
            }
        }
    }

    @Override
    public void setHealth(int health) 
    {
        super.setHealth(health);
        if (health <= 0) {
            setAlive(false);
        }
    }

    @Override
    public void move() 
    {
        //not needed
    }

    @Override
    public void attack() 
    {
        //not needed
    }
}


