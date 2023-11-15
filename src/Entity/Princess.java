package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import main.GamePanel;
import main.KeyHandler;

public class Princess extends Entity{
    GamePanel gp;
    BufferedImage image = null;
    BufferedImage princessImage;
    public Princess(GamePanel gp){
        this.gp = gp;
        getFinishImage();
        setDefaultValues();
    }
    public void setDefaultValues(){
        x = 500;
        y = gp.groundLevel - 100;
    }
    public void destroy(){
        x = 0;
        y = 0;
        speed = 0;
    }
    public void getFinishImage(){
        try {
            princessImage = ImageIO.read(getClass().getResourceAsStream("/resource/princess/princess.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {
        image = stillMushroom;
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
