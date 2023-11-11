package main;

import Entity.Entity;
import Entity.Mushroom;
import Entity.Player;
import Entity.Pipe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 758
    public final int screenHeight = tileSize * maxScreenRow; // 576
    int FPS = 60;

    public BufferedImage gameOverImage;
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    boolean isGameOver = false;
    Player player = new Player(this, keyHandler);
    Mushroom mushroom = new Mushroom(this);

    //Pipes
    Pipe pipe1 = new Pipe(this, false);
    Pipe pipe2 = new Pipe(this, true);
    Pipe pipe3 = new Pipe(this, true);
    //Pipes

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
        //Pipes
        pipe1.setCoords(100, 400);
        pipe2.setCoords(100, 400 - tileSize);
        pipe3.setCoords(400, 400);
        //Pipes
    }
    public boolean topCollision(Entity a, Entity b){
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && a.y < b.y)
        {
            return true;
        }
        return false;
    }
    public boolean borderCollision(Entity a, Entity b){
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && (a.y == b.y))
        {
            return true;
        }
        return false;
    }
    public void gameOver() throws IOException {
        try {
            gameOverImage = ImageIO.read(getClass().getResourceAsStream("/resource/misc/game_over.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        isGameOver = true;
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

        if(topCollision(player, mushroom)){
            player.calculatedFallSpeed = 0;
            mushroom.destroy();
        }

        if(borderCollision(player, mushroom)){
            try {
                gameOver();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(!isGameOver) {
            player.draw(g2);
            mushroom.draw(g2);
            pipe1.draw(g2);
            pipe2.draw(g2);
            pipe3.draw(g2);
            g2.dispose();
        }
        else g2.drawImage(gameOverImage, 0, 0, screenWidth, screenHeight, null);
    }
}
