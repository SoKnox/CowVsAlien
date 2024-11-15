/**
 * Date: 11/14/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: Abstract class representing a mob in the game. Provides shared behavior for enemy entities.
 */

package com.cow;

public abstract class Mob extends Being 
{
    //attributes
    private boolean isAlive;
    private int health;

    //constructor
    public Mob(int x, int y, int speed, int health) 
    {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.isAlive = true;
    }

    //getters and Setters
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

    //abstract methods that will implemented by Alien and FinalBoss subclasses
    public abstract void move();

    public abstract void attack();
}
