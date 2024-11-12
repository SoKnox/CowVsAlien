/**
 * Date: 11/11/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This public class acts as the framework for game entities, handling position, speed,
 * and image references for animations.
 * 
 */

 package com.cow;

 import java.awt.image.BufferedImage;
 
 public class Being 
 {
     //position and speed int
     public int x, y;
     public int speed;
 
     //buffer image for cow
     public BufferedImage up, up1, up2;
     public BufferedImage down, down1, down2;
     public BufferedImage left, left1, left2;
     public BufferedImage right, right1, right2;
 
     //current direction (of cow for now)
     public String direction;
 }
 