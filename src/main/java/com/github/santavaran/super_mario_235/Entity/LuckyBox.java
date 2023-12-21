package com.github.santavaran.super_mario_235.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import com.github.santavaran.super_mario_235.code.*;
public class LuckyBox extends Entity{
    GamePanel gp;
    BufferedImage image;
    Player player;
    public int prevTime, prevX, prevY;
    boolean closeFlag = false;
    public boolean isTouched = false;
    public LuckyBox(GamePanel gp, Player player){
        this.gp = gp;
        getBoxImage();
        this.player = player;
    }
    public int getCoords(boolean type){
        if(type){
            return this.x;
        }
        return this.y;
    }
    public void getBoxImage(){
        try {
            box_touched = ImageIO.read(new FileInputStream("src/main/resource/box/box_touched.png"));
            box_wait = ImageIO.read(new FileInputStream("src/main/resource/box/box_wait.png"));
            System.out.println("Box image uploaded");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void touch(){
        prevTime = gp.internalTime;
        isTouched = true;
        speed = 5;
        System.out.println("Box is touched " + prevTime);
    }
    public void setCoords(int x_set, int y_set){
        y = y_set;
        x = x_set;
    }
    public void draw(Graphics2D g2) {
        if(isTouched){
            image = box_wait;
        } else {
            image = box_touched;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

    public void update() {
        if(isTouched){
            if(!closeFlag){
                prevX = x;
                prevY = y;
                prevTime = gp.internalTime;
                closeFlag = true;
            }
            speed = player.speed;
            if(prevTime + 8 > gp.internalTime){
                y = y - speed;
            }
            else if(!(prevX == x && prevY == y)){
                y = y + speed;
            }
        }
    }
}