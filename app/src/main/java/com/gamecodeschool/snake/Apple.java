package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

class Apple {

    private Point location = new Point();
    private Point mSpawnRange;
    private int mSize;
    private Bitmap mBitmapApple;
    int mScore;

    // Private constructor to enforce the use of the builder
    private Apple(Point spawnRange, int size, Bitmap bitmapApple, int score) {
        this.mSpawnRange = spawnRange;
        this.mSize = size;
        this.mBitmapApple = bitmapApple;
        this.mScore = score;
    }

    // Builder class for constructing Apple objects
    static class Builder {
        private Point location = new Point();
        private Point spawnRange;
        private int size;
        private Bitmap bitmapApple;
        private int score;

        Builder(Context context) {
            Random random = new Random();
            spawnRange = new Point(random.nextInt(100) + 1, random.nextInt(100) + 1);
            size = random.nextInt(20) + 10;

            location.x = -10; // Default off-screen location

            bitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);

            // Generate a random score between 1 and 10
            score = random.nextInt(10) + 1;
        }

        Builder setLocation(int x, int y) {
            location.x = x;
            location.y = y;
            return this;
        }

        Builder setSpawnRange(Point spawnRange) {
            this.spawnRange = spawnRange;
            return this;
        }

        Builder setSize(int size) {
            this.size = size;
            return this;
        }

        Builder setBitmap(Bitmap bitmapApple) {
            this.bitmapApple = bitmapApple;
            return this;
        }

        Builder setScore(int score) {
            this.score = score;
            return this;
        }

        Apple build() {
            return new Apple(spawnRange, size, bitmapApple, score);
        }
    }

    // This is called every time an apple is eaten
    void spawn() {
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;

        // Update the score with a new random value
        mScore = random.nextInt(10) + 2;
    }

    Point getLocation() {
        return location;
    }

    int getScore() {
        return mScore;
    }

    // Draw the apple
    void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapApple, location.x * mSize, location.y * mSize, paint);
    }
}


