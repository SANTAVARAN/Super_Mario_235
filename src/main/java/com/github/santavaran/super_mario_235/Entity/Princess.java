package com.github.santavaran.super_mario_235.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.santavaran.super_mario_235.code.GamePanel;

public class Princess extends Entity{
    GamePanel gp;
    BufferedImage image = null;
    public Princess(GamePanel gp){
        getPrincessImage();
        this.gp = gp;
        setDefaultValues();
    }
    public void setDefaultValues(){
        x = 500;
        y = 0;
    }
    public void destroy(){
        x = 0;
        y = 0;
        speed = 0;
    }
    public void getPrincessImage(){
        try {
            princessImage = ImageIO.read(new FileInputStream("src/main/resource/princess/princess-2.png"));
            System.out.println("Princess image uploaded");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {
        image = princessImage;
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
