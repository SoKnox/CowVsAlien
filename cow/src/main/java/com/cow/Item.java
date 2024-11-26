package com.cow;

import java.awt.image.BufferedImage;

public class Item {
    private String name;
    private int x, y;
    private BufferedImage image;
    private Item nextItem; // Reference to the next item in the list

    public Item(String name, int x, int y, BufferedImage image) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.image = image;
        this.nextItem = null; // Initialize nextItem to null
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Item getNextItem() {
        return nextItem;
    }

    public void setNextItem(Item nextItem) {
        this.nextItem = nextItem;
    }
}

