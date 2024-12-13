/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents a drawable entity in the game, which includes its position, dimensions,
 * a draw function, and a unique identifier.
 */
package com.cow;

import java.awt.Graphics2D;

public class DrawableEntity 


{
    public int x;
    public int y;
    public int width;
    public int height;
    public DrawFunction drawFunction;
    public Object identifier; //field to uniquely identify entities

    //constructor
    public DrawableEntity(int x, int y, int width, int height, DrawFunction drawFunction, Object identifier) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.drawFunction = drawFunction;
        this.identifier = identifier; //assign identifier here
    }

    public void draw(Graphics2D g2) 
    {
        drawFunction.draw(g2);
    }

    @FunctionalInterface
    public interface DrawFunction 
    {
        void draw(Graphics2D g2);
    }
}

