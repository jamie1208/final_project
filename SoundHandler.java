package com.martio.game;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//clip run in its own thread
public class SoundHandler extends Thread implements LineListener{
    
    //private String AUDIO_NAME;
    private AudioInputStream audioInputStream;
    private Clip audioClip;
    Thread tsound;

    public SoundHandler(String AUDIO_NAME) throws UnsupportedAudioFileException,IOException,LineUnavailableException{
        audioInputStream = AudioSystem.getAudioInputStream(new File(AUDIO_NAME).getAbsoluteFile());
        audioClip = AudioSystem.getClip();
        audioClip.open(audioInputStream);
    }

    @Override
    public void update(LineEvent e){
        if(e.getType() == LineEvent.Type.START){
            System.out.println("Playback started!");
        }
        if(e.getType() == LineEvent.Type.STOP){
            System.out.println("Playback stop!");
        }
    }
    
    public synchronized void run(){
        audioClip.setMicrosecondPosition(0);
        audioClip.start();
    }
}