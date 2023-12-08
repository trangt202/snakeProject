package com.gamecodeschool.snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

public class AudioContext {
    private SoundPool soundPool;
    public int eatSoundId = -1;
    public int crashSoundId = -1;

    public AudioContext(Context context) {
        // Initialize the SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        loadSounds(context);
    }

    public void loadSounds(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load the eat sound
            descriptor = assetManager.openFd("get_apple.ogg");
            eatSoundId = soundPool.load(descriptor, 0);

            // Load the crash sound
            descriptor = assetManager.openFd("snake_death.ogg");
            crashSoundId = soundPool.load(descriptor, 0);

        } catch (IOException e) {
            // Handle error
        }
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }
}