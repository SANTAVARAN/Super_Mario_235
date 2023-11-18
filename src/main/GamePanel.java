package main;

import Entity.Entity;
import Entity.Mushroom;
import Entity.Player;
import Entity.Pipe;
import Entity.LuckyBox;
import Entity.Princess;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable{
    int score = 0;

    //Game settings
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 758
    public final int screenHeight = tileSize * maxScreenRow; // 576
    int FPS = 60;
    public int groundLevel = 576 - 3 * tileSize + 19;
    //Game settings

    //Images
    public BufferedImage gameOverImage;
    public BufferedImage gameWinImage;
    BufferedImage backgroundImage;
    //Images

    KeyHandler keyHandler = new KeyHandler();
    UI ui = new UI(this);
    Thread gameThread;

    //Flags
    boolean isGameOver = false;
    boolean isGameWin = false;
    //Flags

    //Objects initialization
    Player player = new Player(this, keyHandler);
    Mushroom mushroom = new Mushroom(this);
    Princess princess = new Princess(this);
    //Objects initialization

    //World generation
    int boxesStartHeight = 1;
    int pipesQuantity = 5;
    int boxesQuantity = 3;
    Random randomSeed = new Random();
    ArrayList<Pipe> topPipes = new ArrayList<Pipe>();
    ArrayList<LuckyBox> boxes = new ArrayList<LuckyBox>();
    ArrayList<Pipe> sidePipes = new ArrayList<Pipe>();
    //World generation

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
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/resource/misc/backgroundimage.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //World generation logic
        for(int j = 0; j < pipesQuantity; j++){
            Pipe pipe = new Pipe(this, true);
            pipe.setCoords(randomSeed.nextInt(0, 20) * tileSize, randomSeed.nextInt(screenHeight - 5 * tileSize, groundLevel));
            topPipes.add(pipe);
        }
        for(int j = 0; j < pipesQuantity; j++){
            for(int i = groundLevel; i > topPipes.get(j).getCoords(false); i-=48){
                Pipe pipe = new Pipe(this, false);
                pipe.setCoords(topPipes.get(j).getCoords(true), i);
                sidePipes.add(pipe);
            }
        }
        for(int j = 0; j < boxesQuantity; j++){
            LuckyBox luckyBox = new LuckyBox(this);
            luckyBox.setCoords(randomSeed.nextInt(0, screenWidth), randomSeed.nextInt(0, groundLevel - boxesStartHeight * tileSize));
            boxes.add(luckyBox);
        }
        //World generation logic

    }
    public boolean topCollision(Entity a, Entity b){
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && a.y < b.y)
        {
            a.calculatedFallSpeed = 0;
            a.y = b.y - tileSize;
            return true;
        }
        return false;
    }
    public boolean bottomCollision(Entity a, Entity b){
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && a.y > b.y)
        {
            a.calculatedFallSpeed = 0;
            a.y = b.y + tileSize;
            return true;
        }
        return false;
    }
    public boolean borderCollision(Entity a, Entity b){
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && (a.y >= b.y))
        {
            if(Objects.equals(a.state, "right")) {
                a.speed = -a.speed;
                a.x = a.x + a.speed;
                a.speed = -a.speed;
            }
            else if(Objects.equals(a.state, "left")) {
                a.speed = -a.speed;
                a.x = a.x - a.speed;
                a.speed = -a.speed;
            }
            else if (Objects.equals(a.state, "up")){
                a.speed = -a.speed;
                a.x = a.x - a.speed;
                a.speed = -a.speed;
            }
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
    public void gameWin() throws IOException {
        try {
            gameWinImage = ImageIO.read(getClass().getResourceAsStream("/resource/misc/win_screen.jpg"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        isGameWin = true;
    }
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null){
            try {
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
    public void update() throws IOException {
        player.update();
        mushroom.update();
        if(topCollision(player, mushroom)){
            mushroom.destroy();
            score++;
        }
        if(topCollision(player, princess) || borderCollision(player, princess)){
            princess.destroy();
            gameWin();
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
        g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        for(int j = 0; j < topPipes.size(); j++){
            borderCollision(mushroom, topPipes.get(j));
            borderCollision(player, topPipes.get(j));
            topCollision(player, topPipes.get(j));
        }
        for(int j = 0; j < sidePipes.size(); j++){
            borderCollision(sidePipes.get(j), mushroom);
            borderCollision(player, sidePipes.get(j));
            topCollision(player, sidePipes.get(j));
        }
        for(int j = 0; j < boxes.size(); j++){
            if(!boxes.get(j).isTouched) {
                borderCollision(player, boxes.get(j));
                if (bottomCollision(player, boxes.get(j))) {
                    score += 1;
                    boxes.get(j).touch();
                }
                topCollision(player, boxes.get(j));
            }
            boxes.get(j).update();
        }
        if(!isGameOver && !isGameWin) {
            player.draw(g2);
            mushroom.draw(g2);
            princess.draw(g2);
            for(int j = 0; j < topPipes.size(); j++){
                topPipes.get(j).draw(g2);
            }
            for(int j = 0; j < sidePipes.size(); j++){
                sidePipes.get(j).draw(g2);
            }
            for(int j = 0; j < boxes.size(); j++){
                boxes.get(j).draw(g2);
            }
            ui.draw(g2);
            g2.dispose();
        }
        else if(isGameOver){
            g2.drawImage(gameOverImage, 0, 0, screenWidth, screenHeight, null);
        } else if (isGameWin) {
            g2.drawImage(gameWinImage, 0, 0, screenWidth, screenHeight, null);
        }
    }
}
