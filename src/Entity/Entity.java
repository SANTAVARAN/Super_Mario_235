package Entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y, speed;
    public BufferedImage stay_l, stay_r, walk_l, walk_r, jump_l, jump_r, fall_l, fall_r, stillMushroom, princessImage, box_wait, box_touched, coin_still;
    public String state;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public String prevState;
    public int calculatedFallSpeed = 0;
}
