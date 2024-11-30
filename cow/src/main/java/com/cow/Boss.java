package com.cow;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Boss extends Mob 
{

    private int damage;
    private BufferedImage bossImage;
    private Item spaceshipLoot; //spaceship item that the boss drops
    private Item bossDropped; //holds the dropped spaceship
    private int animationTimer;
    private int animationInterval = 10;

    public Boss(int x, int y, int speed, int health, int damage) 
    {
        super(x, y, speed, health);
        this.damage = damage;
        loadBossImage();
        loadLootItem();
    }

    private void loadBossImage() 
    {
        try 
        {
            bossImage = ImageIO.read(getClass().getResourceAsStream("/Boss/Boss.png"));
        } catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Failed to load boss image.");
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
            spaceshipLoot = null; //clear the loot 
            return bossDropped;
        }
        return null; 
    }

    public BufferedImage getBossImage() 
    {
        return bossImage;
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
        // Define the boss's movement behavior
        // For example, the boss could move randomly or follow a specific pattern
    }

    @Override
    public void attack() {
        // Define the boss's attack behavior
        // For example, the boss could attack the player or other entities
    }
}




