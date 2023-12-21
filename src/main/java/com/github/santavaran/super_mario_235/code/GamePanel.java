package com.github.santavaran.super_mario_235.code;

import com.github.santavaran.super_mario_235.Entity.*;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable {
    CardLayout cardLayout = new CardLayout();
    public final DataBase dataBase;
    JPanel cardPanel = new JPanel(cardLayout);
    JPanel startScreen = new JPanel();
    JPanel gameOverScreen = new JPanel();
    JPanel gameWinScreen = new JPanel();
    public DataBase database;
    int score = 0;
    public int internalTime = 0;
    //Game settings
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 758
    public final int screenHeight = tileSize * maxScreenRow; // 576
    public int FPS = 60;
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
    SoundHandler soundPlayer = new SoundHandler();

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
    int boxesStartHeight = 5;
    int boxesEndHeight = 6;
    int pipesQuantity = 4;
    int boxesQuantity = 4;
    Random randomSeed = new Random();
    ArrayList<Pipe> topPipes = new ArrayList<Pipe>();
    ArrayList<Coin> coins = new ArrayList<Coin>();
    ArrayList<LuckyBox> boxes = new ArrayList<LuckyBox>();
    ArrayList<Pipe> sidePipes = new ArrayList<Pipe>();
    //World generation

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        database = new DataBase();
        cardPanel.add(this, "game");
        cardPanel.add(startScreen, "start");
        cardPanel.add(gameOverScreen, "gameover");
        cardPanel.add(gameWinScreen, "gamewin");
        //cardLayout.show(cardPanel, "start");
        JLabel startText = new JLabel("Super Mario 235");
        JButton startButton = new JButton("START");
        startScreen.add(startText);
        startScreen.add(startButton);
        startButton.addActionListener(new ActionListenerSwitch(cardLayout, cardPanel, "game"));
        soundPlayer.play("src/main/resource/sounds/background_music.wav");
        dataBase = new DataBase();
    }

    public JPanel getJPanel() {
        return cardPanel;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        try {
            backgroundImage = ImageIO.read(new FileInputStream("src/main/resource/misc/backgroundimage.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //World generation logic
        for (int j = 0; j < pipesQuantity; j++) {
            Pipe pipe = new Pipe(this, true);
            pipe.setCoords(randomSeed.nextInt(0, 20) * tileSize, randomSeed.nextInt(screenHeight - 5 * tileSize, groundLevel));
            topPipes.add(pipe);
        }
        for (int j = 0; j < pipesQuantity; j++) {
            for (int i = groundLevel; i > topPipes.get(j).getCoords(false); i -= 48) {
                Pipe pipe = new Pipe(this, false);
                pipe.setCoords(topPipes.get(j).getCoords(true), i);
                sidePipes.add(pipe);
            }
        }
        for (int j = 0; j < boxesQuantity; j++) {
            LuckyBox luckyBox = new LuckyBox(this, player);
            Coin coin = new Coin(this);
            luckyBox.setCoords(randomSeed.nextInt(0, screenWidth), randomSeed.nextInt(groundLevel - boxesEndHeight * tileSize, groundLevel - boxesStartHeight * tileSize));
            coin.setCoords(luckyBox.x, luckyBox.y);
            coins.add(coin);
            boxes.add(luckyBox);
        }
        //World generation logic

    }

    public boolean topCollision(Entity a, Entity b) {
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && a.y < b.y) {
            a.setFeltState(b.y - tileSize);
            return true;
        }
        return false;
    }

    public boolean bottomCollision(Entity a, Entity b) {
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && a.y > b.y) {
            a.setFeltState(b.y + tileSize);
            return true;
        }
        return false;
    }

    public boolean borderCollision(Entity a, Entity b) {
        Rectangle r = new Rectangle(a.x, a.y, tileSize, tileSize);
        Rectangle p = new Rectangle(b.x, b.y, tileSize, tileSize);
        if (r.intersects(p) && (a.y >= b.y)) {
            if (Objects.equals(a.state, "right")) {
                a.speed = -a.speed;
                a.x = a.x + a.speed;
                a.speed = -a.speed;
            } else if (Objects.equals(a.state, "left")) {
                a.speed = -a.speed;
                a.x = a.x - a.speed;
                a.speed = -a.speed;
            } else if (Objects.equals(a.state, "up")) {
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
            gameOverImage = ImageIO.read(new FileInputStream("src/main/resource/misc/game_over.png"));
            player.y = 0;
            player.x = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBase.saveData(score);
        System.out.println("Score records: ");
        for (int i = 0; i < database.getAll().size(); i++) {
            System.out.println(Arrays.toString(database.getAll().get(i)));
        }
        isGameOver = true;
    }

    public void gameWin() throws IOException {
        try {
            gameWinImage = ImageIO.read(new FileInputStream("src/main/resource/misc/win_screen.jpg"));
            JLabel screenFill = new JLabel();
            JButton exit = new JButton();
            screenFill.setBounds(0, 0, screenWidth, screenHeight);
            screenFill.setIcon(new ImageIcon(gameWinImage));
            gameWinScreen.add(screenFill);
            cardLayout.show(cardPanel, "gamewin");
            exit.addActionListener(new ActionListenerSwitch(cardLayout, cardPanel, "exit"));
            exit.setActionCommand("exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBase.saveData(score);
        System.out.println("Score records: ");
        for (int i = 0; i < database.getAll().size(); i++) {
            System.out.println(Arrays.toString(database.getAll().get(i)));
        }
        isGameWin = true;
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            try {
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            internalTime += 1;
        }
    }

    public void update() throws IOException {
        player.update();
        mushroom.update();
        if (topCollision(player, mushroom)) {
            mushroom.destroy();
            score++;
        }
        if (topCollision(player, princess) || borderCollision(player, princess)) {
            soundPlayer.play("src/main/resource/sounds/smb_world_clear.wav");
            princess.destroy();
            gameWin();
        }
        if (borderCollision(player, mushroom)) {
            try {
                soundPlayer.play("src/main/resource/sounds/smb_mariodie.wav");
                gameOver();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        for (int j = 0; j < topPipes.size(); j++) {
            borderCollision(mushroom, topPipes.get(j));
            borderCollision(player, topPipes.get(j));
            for (int i = 0; i < coins.size(); i++) {
                topCollision(coins.get(i), topPipes.get(j));
            }
            topCollision(player, topPipes.get(j));
        }
        for (int j = 0; j < sidePipes.size(); j++) {
            borderCollision(sidePipes.get(j), mushroom);
            borderCollision(player, sidePipes.get(j));
            topCollision(player, sidePipes.get(j));
        }
        for (int j = 0; j < boxes.size(); j++) {
            borderCollision(player, boxes.get(j));
            if (bottomCollision(player, boxes.get(j))) {
                soundPlayer.play("src/main/resource/sounds/smb_bump.wav");
                coins.get(j).unlock();
                boxes.get(j).touch();
            }
            topCollision(player, boxes.get(j));
            boxes.get(j).update();
        }
        for (int j = 0; j < coins.size(); j++) {
            if ((topCollision(player, coins.get(j)) || borderCollision(player, coins.get(j))) && coins.get(j).y > boxesEndHeight * tileSize + 50) {
                soundPlayer.play("src/main/resource/sounds/smb_coin.wav");
                score += 1;
                coins.get(j).touch();
            }
            coins.get(j).update();
        }
        if (!isGameOver && !isGameWin) {
            player.draw(g2);
            mushroom.draw(g2);
            princess.draw(g2);
            for (int j = 0; j < topPipes.size(); j++) {
                topPipes.get(j).draw(g2);
            }
            for (int j = 0; j < sidePipes.size(); j++) {
                sidePipes.get(j).draw(g2);
            }
            for (int j = 0; j < coins.size(); j++) {
                coins.get(j).draw(g2);
            }
            for (int j = 0; j < boxes.size(); j++) {
                boxes.get(j).draw(g2);
            }
            ui.draw(g2);
            g2.dispose();
        } else if (isGameOver) {
            g2.drawImage(gameOverImage, 0, 0, screenWidth, screenHeight, null);
        } else if (isGameWin) {
            g2.drawImage(gameWinImage, 0, 0, screenWidth, screenHeight, null);
        }
    }
}
