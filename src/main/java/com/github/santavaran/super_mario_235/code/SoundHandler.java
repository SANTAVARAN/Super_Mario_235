package com.github.santavaran.super_mario_235.code;

import javax.sound.sampled.*;
import java.io.File;

public class SoundHandler {
    public void play(String filePath) {
        try {
            File wavFile = new File(filePath);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(wavFile));
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}