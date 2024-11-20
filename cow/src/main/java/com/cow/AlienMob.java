package com.cow;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.LinkedList;
import java.util.Random;

public class AlienMob extends Mob {
    private int damage;
    private BufferedImage[] alienImages;
    private int currentImageIndex;
    private boolean forwardCycle;
    private LinkedList<BufferedImage> lootItems;
    private int animationTimer;
    private int animationInterval = 10; // Adjust this value to control the speed of the animation

    public AlienMob(int x, int y, int speed, int health, int damage, String[] imagePaths) {
        super(x, y, speed, health);
        this.damage = damage;
        this.lootItems = new LinkedList<>();
        this.alienImages = new BufferedImage[imagePaths.length];
        this.currentImageIndex = 0;
        this.forwardCycle = true;
        this.animationTimer = 0;
        loadImages(imagePaths);
        loadLootItems();
    }

    private void loadImages(String[] imagePaths) {
        for (int i = 0; i < imagePaths.length; i++) {
            try {
                alienImages[i] = ImageIO.read(getClass().getResourceAsStream(imagePaths[i]));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to load image: " + imagePaths[i]);
            }
        }
    }

    private void loadLootItems() {
        try {
            lootItems.add(ImageIO.read(getClass().getResourceAsStream("/Items/Sword.png")));
            lootItems.add(ImageIO.read(getClass().getResourceAsStream("/Items/Key.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage dropLoot() {
        if (!lootItems.isEmpty()) {
            Random rand = new Random();
            int index = rand.nextInt(lootItems.size());
            return lootItems.remove(index);
        }
        return null;
    }

    @Override
    public void move() {
        // Implement movement logic if needed
    }

    @Override
    public void attack() {
        // Implement attack logic if needed
    }

    public int getDamage() {
        return damage;
    }

    public BufferedImage getCurrentImage() {
        if (currentImageIndex < 0 || currentImageIndex >= alienImages.length) {
            System.err.println("Index out of bounds: " + currentImageIndex);
            return null; // Return null or a default image if index is out of bounds
        }
        return alienImages[currentImageIndex];
    }

    public void updateAnimation() {
        animationTimer++;
        if (animationTimer >= animationInterval) {
            animationTimer = 0;
            if (forwardCycle) {
                currentImageIndex++;
                if (currentImageIndex >= alienImages.length) {
                    forwardCycle = false;
                    currentImageIndex = alienImages.length - 2; // Start reversing from the second last image
                }
            } else {
                currentImageIndex--;
                if (currentImageIndex < 0) {
                    forwardCycle = true;
                    currentImageIndex = 1; // Start forward from the second image
                }
            }
        }
    }

    @Override
    public void setHealth(int health) {
        super.setHealth(health);
        if (health <= 0) {
            setAlive(false);
        }
    }
}


