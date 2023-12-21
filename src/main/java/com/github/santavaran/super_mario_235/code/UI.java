package com.github.santavaran.super_mario_235.code;
import java.awt.*;

public class UI {
    GamePanel gp;
    Font customFont;
    public UI(GamePanel gp) {
        this.gp = gp;

    }
    public void draw(Graphics2D g2){
        g2.setFont(customFont);
        g2.setColor(Color.white);
        g2.drawString("SCORE" + gp.score, 100, 100);
    }
}
