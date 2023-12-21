package com.github.santavaran.super_mario_235.Entity;
import com.github.santavaran.super_mario_235.code.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyHandler;
    SoundHandler soundPlayer = new SoundHandler();
    final static double velocity = 1;
    final static int jumpSpeed = 10;
    public BufferedImage image = null;
    public Player(GamePanel gp, KeyHandler keyHandler){
        this.gp = gp;
        this.keyHandler = keyHandler;
        getPlayerImage();
        setDefaultValues();
    }
    public void setDefaultValues(){
        calculatedFallSpeed = 0;
        x = 100;
        y = 16;
        speed = 4;
        state = "right";
        image = stay_r;
    }
    public void getPlayerImage(){
        try {
            walk_l = ImageIO.read(new FileInputStream("src/main/resource/player/walk_left.png"));
            walk_r = ImageIO.read(new FileInputStream("src/main/resource/player/walk_right.png"));
            jump_r = ImageIO.read(new FileInputStream("src/main/resource/player/jump_right.png"));
            jump_l = ImageIO.read(new FileInputStream("src/main/resource/player/jump_left.png"));
            fall_l = ImageIO.read(new FileInputStream("src/main/resource/player/fall_left.png"));
            fall_r = ImageIO.read(new FileInputStream("src/main/resource/player/fall_right.png"));
            stay_r = ImageIO.read(new FileInputStream("src/main/resource/player/stay_right.png"));
            stay_l = ImageIO.read(new FileInputStream("src/main/resource/player/stay_left.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            System.out.println("Mario images uploaded");
    }

    public void update() {
        if(keyHandler.aPressed){
            prevState = state;
            state = "left";
            x -= speed;
        }
        if(keyHandler.dPressed){
            prevState = state;
            state = "right";
            x += speed;
        }
        if(keyHandler.spacePressed){
            if(isFelt){
                soundPlayer.play("src/main/resource/sounds/smb_jump.wav");
            }
            prevState = state;
            state = "up";
            y -= jumpSpeed;
            isFelt = false;
        }
        /*if(Objects.equals(prevState, "up") && !Objects.equals(state, "up")){
            //calculatedFallSpeed = -calculatedFallSpeed;
            prevState = state;
            state = "falling";
        }
        */
        spriteCounter++;
        if(spriteCounter > 10){
            if(spriteNum == 1){
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        if(y < gp.groundLevel){
            y += calculatedFallSpeed / 4;
            calculatedFallSpeed += velocity;
        }
        else {
            setFeltState(gp.groundLevel);
            if(Objects.equals(prevState, "right")){
                image = stay_r;
            }
            if(Objects.equals(prevState, "left")){
                image = stay_l;
            }
        }
        if(!(keyHandler.aPressed || keyHandler.dPressed || keyHandler.spacePressed)){
            state = "still";
        }
    }
    public void draw(Graphics2D g2) {
        switch (state){
            case "up":
                if(prevState == "right"){
                    image = jump_r;
                }
                else{
                    image = jump_l;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = walk_l;
                }
                else{
                    image = stay_l;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = walk_r;
                }
                else{
                    image = stay_r;
                }
                break;
            case "still":
                if(Objects.equals(prevState, "right")){
                    image = stay_r;
                }
                if(Objects.equals(prevState, "left")){
                    image = stay_l;
                }
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
