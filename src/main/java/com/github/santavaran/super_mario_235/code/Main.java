package com.github.santavaran.super_mario_235.code;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        ImageIcon img = new ImageIcon("/resource/misc/icon.jpg");
        window.setIconImage(img.getImage());
        GamePanel gamePanel = new GamePanel();
        gamePanel.startGameThread();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Super Mario 235");
        window.add(gamePanel.getJPanel());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
