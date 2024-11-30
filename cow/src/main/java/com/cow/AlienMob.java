/**
 * Date: 11/24/24
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

    public AlienMob(int x, int y, int speed, int health, int damage, String[] imagePaths) 
    {
        super(x, y, speed, health);
        this.damage = damage;
        this.alienImages = new BufferedImage[imagePaths.length];
        this.currentImageIndex = 0;
        this.forwardCycle = true;
        this.animationTimer = 0;
        loadImages(imagePaths);
        loadLootItems();
    }

    private void loadImages(String[] imagePaths) 
    {
        for (int i = 0; i < imagePaths.length; i++) 
        {
            try {
                alienImages[i] = ImageIO.read(getClass().getResourceAsStream(imagePaths[i]));
            } catch (IOException e) 
            {
                e.printStackTrace();
                System.err.println("Failed to load image: " + imagePaths[i]);
            }
        }
    }

    private void loadLootItems() 
    {
        try 
        {
            Item swordItem = new Item("Sword", this.x, this.y, ImageIO.read(getClass().getResourceAsStream("/Items/Sword.png")));
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
        if (isAlive() && lootItems != null) {
            //determine the item to drop based on the first dropped item
            Item lootImage = null;
            String itemName = null;

            if (firstDroppedItem == null) 
            {
                //drop a random item if no item has been dropped yet
                Random rand = new Random();
                int index = rand.nextInt(2); //2 items on list (for now)
                lootImage = (index == 0) ? lootItems : lootItems.getNextItem();
                itemName = (index == 0) ? "Sword" : "Key";
                //remove the dropped item from the list
                if (index == 0) 
                {
                    lootItems = lootItems.getNextItem();
                } else 
                {
                    lootItems.setNextItem(null);
                }
            } else 
            {
                //ensure the remaining item is dropped
                if (firstDroppedItem.equals("Sword")) 
                {
                    lootImage = lootItems.getNextItem(); //key
                    itemName = "Key";
                    lootItems = null; //remove the last item
                } else 
                {
                    lootImage = lootItems; //sword
                    itemName = "Sword";
                    lootItems = lootItems.getNextItem(); //remove the first item
                }
            }

            return new Item(itemName, this.x, this.y, lootImage.getImage());
        }
        return null;
    }

    public BufferedImage getCurrentImage() 
    {
        if (currentImageIndex < 0 || currentImageIndex >= alienImages.length) 
        {
            System.err.println("Index out of bounds: " + currentImageIndex);
            return null;
        }
        return alienImages[currentImageIndex];
    }

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
    public void move() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    @Override
    public void attack() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attack'");
    }
}


