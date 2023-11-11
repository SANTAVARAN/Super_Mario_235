package Entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y, speed;
    public BufferedImage stay_l, stay_r, walk_l, walk_r, jump_l, jump_r, fall_l, fall_r, stillMushroom;
    public String state;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public String prevState;
}
