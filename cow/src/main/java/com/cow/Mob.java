/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: Abstract class representing a mob in the game. Provides shared behavior for enemy entities.
 */
package com.cow;

import java.awt.image.BufferedImage;

public abstract class Mob extends Being 
{
    //attributes
    private boolean isAlive;
    private int health;
    private BufferedImage currentImage;
    private int width;
    private int height;

    //constructor
    public Mob(int x, int y, int speed, int health, int width, int height) 
    {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.width = width;
        this.height = height;
        this.isAlive = true;
    }

    //getters and setters
    public boolean isAlive() 
    {
        return isAlive;
    }

    public void setAlive(boolean alive) 
    {
        isAlive = alive;
    }

    public int getHealth() 
    {
        return health;
    }

    public void setHealth(int health) 
    {
        this.health = health;
    }

    public BufferedImage getCurrentImage() 
    {
        return currentImage;
    }

    public void setCurrentImage(BufferedImage currentImage) 
    {
        this.currentImage = currentImage;
    }

    public int getWidth() 
    {
        return width;
    }

    public int getHeight() 
    {
        return height;
    }

    //abstract methods that will be implemented by Alien and FinalBoss subclasses
    public abstract void move();

    public abstract void attack();
}
