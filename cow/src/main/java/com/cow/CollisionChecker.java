/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents a collision checker in the game, which includes methods to check proximity and kill aliens,
 * check collision with a spaceship, and check laser collision with mobs.
 */
package com.cow;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CollisionChecker 
{
    private GamePanel gamePanel;
    private AlienMob alien1;
    private AlienMob alien2;
    private Boss boss;
    private GameStateManager gameStateManager;
    private ItemManager itemManager;

    public CollisionChecker(GamePanel gamePanel, AlienMob alien1, AlienMob alien2, Boss boss, GameStateManager gameStateManager, ItemManager itemManager) 
    {
        this.gamePanel = gamePanel;
        this.alien1 = alien1;
        this.alien2 = alien2;
        this.boss = boss;
        this.gameStateManager = gameStateManager;
        this.itemManager = itemManager;
    }

    public void checkProximityAndKillAliens() 
    {
        if (alien1.isAlive() && isWithinProximity(gamePanel.playerCow, alien1)) 
        {
            Item loot = alien1.dropLoot(itemManager.getFirstDroppedItem());
            if (loot != null) 
            {
                itemManager.addDroppedItem(loot);
            }
            alien1.setHealth(0);
            alien1.setAlive(false);
            gamePanel.entityManager.removeMob(alien1); //remove the alien from the game panel
        }

        if (alien2.isAlive() && isWithinProximity(gamePanel.playerCow, alien2)) 
        {
            Item loot = alien2.dropLoot(itemManager.getFirstDroppedItem());
            if (loot != null) 
            {
                itemManager.addDroppedItem(loot);
            }
            alien2.setHealth(0);
            alien2.setAlive(false);
            gamePanel.entityManager.removeMob(alien2); //remove the alien from the game panel
        }

        if (boss.isAlive() && isWithinProximity(gamePanel.playerCow, boss)) 
        {
            System.out.println("Boss defeated!");
            boss.setHealth(0);
            boss.setAlive(false);

            Item loot = boss.dropLoot();
            if (loot != null) 
            {
                System.out.println("Boss dropped loot: " + loot.getName()); //debugging statements
            } else 
            {
                System.out.println("Boss did not drop any loot."); //debugging statements
            }
        }
    }

    private boolean isWithinProximity(PlayerCow player, Mob mob) 
    {
        int proximityDistance = 150;
        return Math.abs(player.x - mob.x) <= proximityDistance && Math.abs(player.y - mob.y) <= proximityDistance;
    }

    public void checkCollisionWithSpaceship() 
    {
        if (boss.getBossDropped() != null) 
        {
            int cowCenterX = gamePanel.playerCow.x + gamePanel.tileSize / 2;
            int cowCenterY = gamePanel.playerCow.y + gamePanel.tileSize / 2;
            int spaceshipCenterX = boss.getBossDropped().getX() + gamePanel.tileSize / 2;
            int spaceshipCenterY = boss.getBossDropped().getY() + gamePanel.tileSize / 2;

            if (Math.abs(cowCenterX - spaceshipCenterX) < gamePanel.tileSize / 2 &&
                Math.abs(cowCenterY - spaceshipCenterY) < gamePanel.tileSize / 2) 
                {
                //check if both aliens are removed and the key is gone
                //only once other aliens are dead and key is obtained can player activate game over
                if (!alien1.isAlive() && !alien2.isAlive() && !itemManager.containsKey()) 
                {
                    gameStateManager.setGameState(GameState.GAME_OVER);
                }
            }
        }
    }

    //laser collision kills alien
    public void checkLaserCollision(int laserX, int laserY, int laserWidth, int laserHeight) 
    {
        for (Mob mob : gamePanel.entityManager.getMobs()) 
        {
            if (mob.isAlive() && laserX < mob.x + mob.getWidth() && laserX + laserWidth > mob.x &&
                laserY < mob.y + mob.getHeight() && laserY + laserHeight > mob.y) 
                {
                System.out.println("Collision detected with mob: " + mob);
                mob.setHealth(0); //set the mob's health to 0
                mob.setAlive(false); //set the mob to not alive
                if (mob instanceof AlienMob) 
                {
                    ((AlienMob) mob).setHit(true); //set the alien's hit state to true
                }
                gamePanel.entityManager.removeMob(mob); //remove the mob from the game
                System.out.println("Mob removed: " + mob);
            }
        }
    }
}

