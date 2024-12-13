/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents an item in the game, which includes its name, position, image,
 * and a reference to the next item in a linked list.
 */

 package com.cow;

 import java.awt.image.BufferedImage;
 
 public class Item 
 {
     private String name;
     private int x, y;
     private BufferedImage image;
     private Item nextItem; //reference to the next item in the list
 
     public Item(String name, int x, int y, BufferedImage image) 
     {
         this.name = name;
         this.x = x;
         this.y = y;
         this.image = image;
         this.nextItem = null; //initialize nextItem to null
     }
 
     public String getName() 
     {
         return name;
     }
 
     public int getX() 
     {
         return x;
     }
 
     public int getY() 
     {
         return y;
     }
 
     public BufferedImage getImage() 
     {
         return image;
     }
 
     public Item getNextItem() 
     {
         return nextItem;
     }
 
     public void setNextItem(Item nextItem) 
     {
         this.nextItem = nextItem;
     }
 }
 