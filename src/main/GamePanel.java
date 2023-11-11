package main;

import Entity.Entity;
import Entity.Mushroom;
import Entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 758
    public final int screenHeight = tileSize * maxScreenRow; // 576
    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);
    Mushroom mushroom = new Mushroom(this);
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }
    public void startGameThread(){
       gameThread = new Thread(this);
       gameThread.start();
    }
    public boolean Collision(Entity a, Entity b){
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && a.y < b.y)
        {
            return true;
        }
        return false;
    }
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null){
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if(remainingTime < 0){
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void update(){
        player.update();
        mushroom.update();
        if(Collision(player, mushroom)){
            player.calculatedFallSpeed = 0;
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        player.draw(g2);
        mushroom.draw(g2);
        g2.dispose();
    }
}
