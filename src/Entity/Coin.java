package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import main.GamePanel;
public class Coin extends Entity{
    GamePanel gp;
    BufferedImage image;
    boolean closeFlag = false;
    public boolean isTouched = false;
    public boolean isUnlocked = false;
    int prevYContinous;
    public Coin(GamePanel gp){
        this.gp = gp;
        getCoinImage();
    }
    public int getCoords(boolean type){
        if(type){
            return this.x;
        }
        return this.y;
    }
    public void getCoinImage(){
        try {
            coin_still = ImageIO.read(getClass().getResourceAsStream("/resource/coin/coin.png"));
            System.out.println("Coin image uploaded");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void touch(){
        isTouched = true;
        speed = 5;
        System.out.println("Coin is touched ");
    }
    public void unlock(){
        isUnlocked = true;
        speed = 5;
        System.out.println("Coin is unlocked ");
    }
    public void setCoords(int x_set, int y_set){
        y = y_set;
        x = x_set;
    }
    public boolean isFalling(){
        if(prevYContinous != y){
            return true;
        }
        return false;
    }
    public void draw(Graphics2D g2) {
        image = coin_still;
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
    public void update() {
        prevYContinous = y;
        if(isUnlocked){
            if(y < gp.groundLevel){
                y = y + speed;
            }
            if(isTouched){
                x = 1000;
                speed = 1;
            }
        }
    }
}