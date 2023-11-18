package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import main.GamePanel;
public class LuckyBox extends Entity{
    GamePanel gp;
    BufferedImage image;
    public boolean isTouched = false;
    public LuckyBox(GamePanel gp){
        this.gp = gp;
        getBoxImage();
    }
    public int getCoords(boolean type){
        if(type){
            return this.x;
        }
        return this.y;
    }
    public void getBoxImage(){
        try {
            box_touched = ImageIO.read(getClass().getResourceAsStream("/resource/box/box_wait.png"));
            box_wait = ImageIO.read(getClass().getResourceAsStream("/resource/box/box_touched.png"));
            System.out.println("Box image uploaded");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void touch(){
        isTouched = true;
        speed = 5;
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
        y = y + speed;
    }
}