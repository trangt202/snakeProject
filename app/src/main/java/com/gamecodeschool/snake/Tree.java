package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.Random;

class Tree {

    private Point location = new Point();
    private Point mSpawnRange;
    private int mSize;
    private Bitmap mBitmapObstacle;

    public Tree(Context context, Point sr, int s) {
        mSpawnRange = sr;
        mSize = s;
        location.x = -10;

        // Load the obstacle bitmap
        try {
            mBitmapObstacle = BitmapFactory.decodeResource(context.getResources(), R.drawable.tree);
            mBitmapObstacle = Bitmap.createScaledBitmap(mBitmapObstacle, s, s, false);
        } catch (Exception e) {
            Log.e("Tree", "Error loading obstacle bitmap: " + e.getMessage());
        }
    }

    public void spawn() {
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    public Point getLocation() {
        return location;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (mBitmapObstacle != null) {
            canvas.drawBitmap(mBitmapObstacle, location.x * mSize, location.y * mSize, paint);
        }
    }
}
