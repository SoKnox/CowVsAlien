package com.cow;

import java.awt.image.BufferedImage;

public class Item {
    private String name;
    private BufferedImage image;
    private int x;
    private int y;

    public Item(String name, BufferedImage image, int x, int y) {
        this.name = name;
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

