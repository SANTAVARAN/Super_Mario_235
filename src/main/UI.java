package main;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Font customFont;
    public UI(GamePanel gp) {
        this.gp = gp;

    }
    public void draw(Graphics2D g2){
        /*
        try {
            //create the font to use. Specify the size!
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("src/main/small_pixel.ttf");
            assert stream != null;
            customFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(48f);
            //register the font
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }
        customFont = new Font("small_pixel", Font.PLAIN, 40);
         */
        g2.setFont(customFont);
        g2.setColor(Color.white);
        g2.drawString("SCORE" + gp.score, 100, 100);
    }
}
