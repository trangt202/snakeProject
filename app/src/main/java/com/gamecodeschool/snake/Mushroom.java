package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Mushroom {
    private Point location = new Point();
    private Point mSpawnRange;
    private int mSize;
    private Bitmap mBitmapMushroom;
    private boolean isActivated;

    public Mushroom(Context context, Point sr, int s) {
        mSpawnRange = sr;
        mSize = s;
        // Hide the mushroom off-screen until the game starts
        location.x = -10;

        // Load the image to the bitmap
        mBitmapMushroom = BitmapFactory.decodeResource(context.getResources(), R.drawable.mushroom);
        // Resize the bitmap
        mBitmapMushroom = Bitmap.createScaledBitmap(mBitmapMushroom, s, s, false);
    }

    public Point getLocation() {
        return location;
    }

    void spawn() {
        // Set the mushroom's position randomly
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;

      //  isActivated = false; // Reset activation status when spawning
    }

    //int getSize() {
        //return mSize;
   // }

    boolean isActivated() {
        return isActivated;
    }


    void draw(Canvas canvas, Paint paint) {
        // Draw the mushroom on the canvas
        canvas.drawBitmap(mBitmapMushroom, location.x*mSize, location.y*mSize , paint);
    }

}



