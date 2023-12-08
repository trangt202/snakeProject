package com.gamecodeschool.snake;

import android.util.Log;

public class Audio {
    private AudioContext audioContext;

    public Audio(AudioContext audioContext) {
        this.audioContext = audioContext;
    }

    public void playEatSound() {
        try {
            if (audioContext.eatSoundId != -1) {
                audioContext.getSoundPool().play(audioContext.eatSoundId, 1, 1, 0, 0, 1);
                Log.d("SoundEffect", "Play eat sound effect");
            }
        } catch (Exception e) {
            Log.e("SoundEffect", "Error playing eat sound effect", e);
        }
    }

    public void playCrashSound() {
        try {
            if (audioContext.crashSoundId != -1) {
                audioContext.getSoundPool().play(audioContext.crashSoundId, 1, 1, 0, 0, 1);
                Log.d("SoundEffect", "Play crash sound effect");
            }
        } catch (Exception e) {
            Log.e("SoundEffect", "Error playing crash sound effect", e);
        }
    }
}
