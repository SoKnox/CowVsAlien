/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents an item manager in the game, which includes methods to update the state of the item manager,
 * draw items on the screen, check if the player picks up an item, add dropped items to the list, get the name of the first dropped item,
 * and check if the dropped items contain a key.
 */
package com.cow;

import java.awt.Graphics2D;

public class ItemManager
{
    private GamePanel gamePanel;
    private ItemLinkedList droppedItems = new ItemLinkedList();
    private String firstDroppedItem = null;

    //constructor to initialize the itemmanager with a gamepanel
    public ItemManager(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;
    }

    //method to update the state of the itemmanager
    public void update()
    {
        checkItemPickup();
    }

    //method to draw the items on the screen
    public void draw(Graphics2D g2)
    {
        Item current = droppedItems.getHead();
        while (current != null)
        {
            if (current.getImage() != null)
            {
                g2.drawImage(current.getImage(), current.getX(), current.getY(), gamePanel.tileSize, gamePanel.tileSize, null);
            }
            current = current.getNextItem();
        }

        if (gamePanel.getBoss().getBossDropped() != null && gamePanel.getBoss().getBossDropped().getImage() != null)
        {
            g2.drawImage(gamePanel.getBoss().getBossDropped().getImage(), gamePanel.getBoss().getBossDropped().getX(), gamePanel.getBoss().getBossDropped().getY(), gamePanel.tileSize, gamePanel.tileSize, null);
        }
    }

    //method to check if the player cow picks up an item
    private void checkItemPickup()
    {
        Item current = droppedItems.getHead();
        while (current != null)
        {
            int cowCenterX = gamePanel.playerCow.x + gamePanel.tileSize / 2;
            int cowCenterY = gamePanel.playerCow.y + gamePanel.tileSize / 2;

            if (Math.abs(cowCenterX - current.getX()) < gamePanel.tileSize / 2 &&
                Math.abs(cowCenterY - current.getY()) < gamePanel.tileSize / 2)
            {
                if (current.getName().equals("Sword"))
                {
                    gamePanel.playerCow.setHeldItem(current);
                    droppedItems.remove(current.getName());
                    break;
                }
                else if (current.getName().equals("Key"))
                {
                    droppedItems.remove(current.getName());
                    break;
                }
            }
            current = current.getNextItem();
        }
    }

    //method to add a dropped item to the list
    public void addDroppedItem(Item item)
    {
        droppedItems.insertAtEnd(item);
        if (firstDroppedItem == null)
        {
            firstDroppedItem = item.getName();
        }
    }

    //method to get the name of the first dropped item
    public String getFirstDroppedItem()
    {
        return firstDroppedItem;
    }

    //method to check if the dropped items contain a key
    public boolean containsKey()
    {
        Item current = droppedItems.getHead();
        while (current != null)
        {
            if (current.getName().equals("Key"))
            {
                return true;
            }
            current = current.getNextItem();
        }
        return false;
    }
}

