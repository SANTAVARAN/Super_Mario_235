package com.github.santavaran.super_mario_235.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.santavaran.super_mario_235.code.*;

public class Mushroom extends Entity{
    GamePanel gp;
    boolean isAlive = true;
    boolean direction = true;
    BufferedImage image = null;
    public Mushroom(GamePanel gp){
        this.gp = gp;
        getMushroomImage();
        setDefaultValues();
    }
    public void setDefaultValues(){
        x = 200;
        y = gp.groundLevel;
        speed = 2;
        state = "mushroom";
    }
    public void destroy(){
        x = 0;
        y = 0;
        speed = 0;
        isAlive = false;
    }
    public void getMushroomImage(){
        try {
            stillMushroom = ImageIO.read(new FileInputStream("src/main/resource/mushroom/mushroom.png"));
            System.out.println("Mushroom image uploaded");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void update() {
        if(x >= gp.screenWidth - gp.tileSize){
            direction = !direction;
        }
        if(x <= 0){
            direction = true;
        }
        if(direction){
            x += speed;
        }
        else{
            x -= speed;
        }
    }
    public void draw(Graphics2D g2) {
        if(isAlive) {
            image = stillMushroom;
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }
    }
}
