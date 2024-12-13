/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents an alien mob in the game, which includes its position, health, damage,
 * animation images, and a linked list of loot items. 
 */
package com.cow;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class AlienMob extends Mob 
{
    private int damage;
    private BufferedImage[] alienImages;
    private int currentImageIndex;
    private boolean forwardCycle;
    private Item lootItems; //head of the linked list of loot items
    private int animationTimer;
    private int animationInterval = 10;
    private boolean isHit; //state to indicate if the alien is hit

    public AlienMob(int x, int y, int speed, int health, int damage, String[] imagePaths, int width, int height) 
    {
        super(x, y, speed, health, width, height);
        this.damage = damage;
        this.alienImages = new BufferedImage[imagePaths.length];
        this.currentImageIndex = 0;
        this.forwardCycle = true;
        this.animationTimer = 0;
        this.isHit = false; //initialize the hit state to false
        loadImages(imagePaths);
        loadLootItems();
        setCurrentImage(alienImages[currentImageIndex]);
    }

    //load alien images
    private void loadImages(String[] imagePaths) 
    {
        for (int i = 0; i < imagePaths.length; i++) 
        {
            try 
            {
                alienImages[i] = ImageIO.read(getClass().getResourceAsStream(imagePaths[i]));
            } catch (IOException e) 
            {
                e.printStackTrace();
                System.err.println("Failed to load image: " + imagePaths[i]);
            }
        }
    }

    //loads loot items
    private void loadLootItems() 
    {
        try 
        {
            Item swordItem = new Item("Sword", this.x, this.y, ImageIO.read(getClass().getResourceAsStream("/Items/S1.png")));
            Item keyItem = new Item("Key", this.x, this.y, ImageIO.read(getClass().getResourceAsStream("/Items/Key.png")));
            swordItem.setNextItem(keyItem); //link the items
            this.lootItems = swordItem; //set the head of the list
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public Item dropLoot(String firstDroppedItem) 
    {
        if (isAlive() && lootItems != null) 
        {
            Item lootImage = null;
            String itemName = null;

            if (firstDroppedItem == null) 
            {
                //random item drop
                Random rand = new Random();
                int index = rand.nextInt(2); //two items in the list
                lootImage = (index == 0) ? lootItems : lootItems.getNextItem();
                itemName = (index == 0) ? "Sword" : "Key";

                //remove the dropped item from the list
                if (index == 0) 
                {
                    lootItems = lootItems.getNextItem();
                } else 
                {
                    lootItems.setNextItem(null); //only one item left
                }
            } else 
            {
                //drop the remaining item if one item was already dropped
                if (firstDroppedItem.equals("Sword")) 
                {
                    lootImage = lootItems.getNextItem(); //drop Key
                    itemName = "Key";
                    lootItems = null; //no items left
                } else 
                {
                    lootImage = lootItems; //drop Sword
                    itemName = "Sword";
                    lootItems = lootItems.getNextItem(); //remove Sword
                }
            }

            //return the dropped loot item
            return new Item(itemName, this.x, this.y, lootImage.getImage());
        }
        return null; //no loot to drop
    }

    //alien animation by changing alien image
    public void updateAnimation() 
    {
        animationTimer++;
        if (animationTimer >= animationInterval) 
        {
            animationTimer = 0;
            if (forwardCycle) 
            {
                currentImageIndex++;
                if (currentImageIndex >= alienImages.length) 
                {
                    forwardCycle = false;
                    currentImageIndex = alienImages.length - 2; //reversing from the second last image
                }
            } else 
            {
                currentImageIndex--;
                if (currentImageIndex < 0) 
                {
                    forwardCycle = true;
                    currentImageIndex = 1; //forward from the second image
                }
            }
            setCurrentImage(alienImages[currentImageIndex]);
        }
    }

    @Override
    public void setHealth(int health) 
    {
        super.setHealth(health);
        if (health <= 0) 
        {
            setAlive(false);
        }
    }

    @Override
    public void move() 
    {
        
    }

    @Override
    public void attack()
    {
        
    }

    public void setHit(boolean hit) 
    {
        isHit = hit;
    }

    public boolean isHit() 
    {
        return isHit;
    }
}
