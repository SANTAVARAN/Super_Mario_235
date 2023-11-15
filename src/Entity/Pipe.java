package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import main.GamePanel;
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
                image = ImageIO.read(getClass().getResourceAsStream("/resource/pipe/pipe_top.png"));
            }
            else {
                image = ImageIO.read(getClass().getResourceAsStream("/resource/pipe/pipe.png"));
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
