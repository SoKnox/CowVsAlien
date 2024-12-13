/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents an entity manager in the game, which includes methods to initialize entities,
 * update their states, draw them on the screen, remove mobs, and get a list of mobs.
 */


package com.cow;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class EntityManager 
{
    private GamePanel gamePanel;
    private LinkedList<DrawableEntity> entities = new LinkedList<>();
    private AlienMob alien1;
    private AlienMob alien2;
    private Boss boss;

    public EntityManager(GamePanel gamePanel, AlienMob alien1, AlienMob alien2, Boss boss) 
    {
        this.gamePanel = gamePanel;
        this.alien1 = alien1;
        this.alien2 = alien2;
        this.boss = boss;
        initializeEntities();
    }

    private void initializeEntities() 
    {
        //add the PlayerCow to the list as a drawable entity
        entities.add(new DrawableEntity(
            gamePanel.playerCow.x, gamePanel.playerCow.y, gamePanel.tileSize, gamePanel.tileSize,
            graphics -> gamePanel.playerCow.draw(graphics), gamePanel.playerCow
        ));

        //add the NPC
        entities.add(new DrawableEntity(
            gamePanel.npc.x, gamePanel.npc.y, gamePanel.tileSize, gamePanel.tileSize,
            graphics -> gamePanel.npc.draw(graphics), gamePanel.npc
        ));

        //add AlienMob 1
        if (alien1.isAlive()) 
        {
            entities.add(new DrawableEntity(
                alien1.x, alien1.y, gamePanel.tileSize, gamePanel.tileSize,
                graphics -> 
                {
                    BufferedImage image = alien1.getCurrentImage();
                    if (image != null) 
                    {
                        graphics.drawImage(image, alien1.x, alien1.y, gamePanel.tileSize, gamePanel.tileSize, null);
                    }
                }, alien1
            ));
        }

        //add AlienMob 2
        if (alien2.isAlive()) 
        {
            entities.add(new DrawableEntity(
                alien2.x, alien2.y, gamePanel.tileSize, gamePanel.tileSize,
                graphics -> 
                {
                    BufferedImage image = alien2.getCurrentImage();
                    if (image != null) 
                    {
                        graphics.drawImage(image, alien2.x, alien2.y, gamePanel.tileSize, gamePanel.tileSize, null);
                    }
                }, alien2
            ));
        }

        //add the Boss
        if (boss.isAlive()) 
        {
            entities.add(new DrawableEntity(
                boss.x, boss.y, gamePanel.tileSize, gamePanel.tileSize,
                graphics -> 
                {
                    BufferedImage image = boss.getBossImage();
                    if (image != null) 
                    {
                        graphics.drawImage(image, boss.x, boss.y, gamePanel.tileSize, gamePanel.tileSize, null);
                    }
                }, boss
            ));
        }
    }

    public void update() 
    {
        //update animations for mobs
        alien1.updateAnimation();
        alien2.updateAnimation();
        boss.updateAnimation();

        //remove entities whose associated mob is no longer alive
        entities.removeIf(entity -> 
            (entity.identifier == alien1 && !alien1.isAlive()) ||
            (entity.identifier == alien2 && !alien2.isAlive()) ||
            (entity.identifier == boss && !boss.isAlive())
        );
    }

    public void draw(Graphics2D g2) 
    {
        //update player position in the list
        entities.stream()
            .filter(entity -> entity.identifier == gamePanel.playerCow)
            .forEach(entity -> {
                entity.x = gamePanel.playerCow.x;
                entity.y = gamePanel.playerCow.y;
            });

        //sort entities based on y-coordinate (ascending order)
        entities.sort(Comparator.comparingInt(entity -> entity.y));

        //draw all entities in sorted order
        for (DrawableEntity entity : entities) 
        {
            entity.draw(g2);
        }
    }

    public void removeMob(Mob mob) 
    {
        System.out.println("Removing mob: " + mob);
        entities.removeIf(entity -> entity.identifier == mob);
        System.out.println("Mob removed from entities list: " + mob);
    }

    public LinkedList<Mob> getMobs() 
    {
        LinkedList<Mob> mobs = new LinkedList<>();
        if (alien1.isAlive()) 
        {
            mobs.add(alien1);
        }
        if (alien2.isAlive()) 
        {
            mobs.add(alien2);
        }
        if (boss.isAlive()) 
        {
            mobs.add(boss);
        }
        return mobs;
    }
}


