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
        try {
            //create the font to use. Specify the size!
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("/Users/santavaran/Documents/Study/Компы/Java/Super Mario/src/main/LLPixel.ttf");
            assert stream != null;
            customFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(48f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }
        customFont = new Font("LLPixel", Font.PLAIN, 40);
    }
    public void draw(Graphics2D g2){

        g2.setFont(customFont);
        g2.setColor(Color.white);
        g2.drawString("Scor" + gp.score, 100, 100);
    }
}
