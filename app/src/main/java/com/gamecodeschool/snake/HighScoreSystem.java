package com.gamecodeschool.snake;
import android.content.Context;
import android.content.SharedPreferences;

public class HighScoreSystem {
    private static final String PREFERENCES_KEY = "HighScorePreferences";
    private static final String HIGH_SCORE_KEY = "HighScore";

    private int highScore;
    private SharedPreferences preferences;

    public HighScoreSystem(Context context) {
        // Load the high score from SharedPreferences
        preferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        highScore = loadHighScore();
    }

    public int getHighScore() {
        return highScore;
    }

    public void updateHighScore(int newScore) {
        if (newScore > highScore) {
            highScore = newScore;
            // Save the updated high score to SharedPreferences
            saveHighScore();
        }
    }

    private int loadHighScore() {
        // Load the high score from SharedPreferences
        return preferences.getInt(HIGH_SCORE_KEY, 0);
    }

    private void saveHighScore() {
        // Save the high score to SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(HIGH_SCORE_KEY, highScore);
        editor.apply();
    }
}


