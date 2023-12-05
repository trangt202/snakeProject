// Tree.java
package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Tree {

    private Point location = new Point();
    private Point mSpawnRange;
    private int mSize;
    private Bitmap mBitmapObstacle;

    public Tree(Context context, Point sr, int s) {
        mSpawnRange = sr;
        mSize = s;
        // Hide the apple off-screen until the game starts
        location.x = -10;


        mBitmapObstacle = BitmapFactory.decodeResource(context.getResources(), R.drawable.tree);
        mBitmapObstacle = Bitmap.createScaledBitmap(mBitmapObstacle, s, s, false);
    }

    public void spawn() {
        Random random = new Random();
        // Change the range and adjust the spawn position as needed
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    public Point getLocation() {
        return location;
    }

    public void draw(Canvas canvas, Paint paint) {
        // Increase the size of the tree
        int treeSize = mSize * 2;  // You can adjust this multiplier based on how big you want the tree
        canvas.drawBitmap(mBitmapObstacle, location.x * mSize, location.y * mSize, paint);
    }
}





