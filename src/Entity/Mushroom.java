package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import main.GamePanel;
import main.KeyHandler;

public class Mushroom extends Entity{
    GamePanel gp;
    boolean direction = true;
    BufferedImage image = null;
    public Mushroom(GamePanel gp){
        this.gp = gp;
        getMushroomImage();
        setDefaultValues();
    }
    public void setDefaultValues(){
        x = 100;
        y = 400;
        speed = 2;
        state = "right";
    }
    public void getMushroomImage(){
        try {
            stillMushroom = ImageIO.read(getClass().getResourceAsStream("/resource/mushroom/mushroom.png"));
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
        image = stillMushroom;
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
