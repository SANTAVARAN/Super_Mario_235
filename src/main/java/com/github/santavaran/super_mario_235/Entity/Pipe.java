package com.github.santavaran.super_mario_235.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import com.github.santavaran.super_mario_235.code.*;
public class Pipe extends Entity{
    GamePanel gp;
    BufferedImage image;
    public boolean isTop;
    public Pipe(GamePanel gp, boolean isTop){
        this.gp = gp;
        this.isTop = isTop;
        getPipeImage();
    }
    public int getCoords(boolean type){
        if(type){
            return this.x;
        }
        return this.y;
    }
    public void getPipeImage(){
        try {
            if(isTop) {
                image = ImageIO.read(new FileInputStream("src/main/resource/pipe/pipe_top.png"));
                System.out.println("Top pipe image uploaded");
            }
            else {
                image = ImageIO.read(new FileInputStream("src/main/resource/pipe/pipe.png"));
                System.out.println("Side pipe image uploaded");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void setCoords(int x_set, int y_set){
        y = y_set;
        x = x_set;
    }
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
