package com.gamecodeschool.snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import android.util.Log;

class SnakeGame extends SurfaceView implements Runnable{

    // Objects for the game loop/thread
    private Thread mThread = null;
    // Control pausing between updates
    private long mNextFrameTime;
    // Is the game currently playing and or paused?
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;

    // for playing sound effects
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;
    private int mBackgroundMusicID = -1;

    private int mEatMushroomID=-1;

    // The size in segments of the playable area
    public static final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    // How many points does the player have
    private int mScore;

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    // A snake ssss
    private Snake mSnake;
    // And an apple
    private Apple mApple;

    //declare a tree object
    private Tree mTree;
    private Mushroom mMushroom;
    private int mHighScore;

    private int mLives;


    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Context context, Point size) {
        super(context);

        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        mNumBlocksHigh = size.y / blockSize;

        // Initialize the SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("get_apple.ogg");
            mEat_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("eatmushroom.ogg");
            mEatMushroomID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("Snake_song.ogg");
            mBackgroundMusicID = mSP.load(descriptor, 0);

        } catch (IOException e) {
            // Error
        }
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("Snake_song.ogg");
            mBackgroundMusicID = mSP.load(descriptor, 0);

            Log.d("SnakeGame", "Background music loaded successfully");
        } catch (IOException e) {
            Log.e("SnakeGame", "Error loading background music: " + e.getMessage());
        }

        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        // Call the constructors of our two game objects
        mApple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mSnake = new Snake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        mTree = new Tree(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        // Initialize Mushroom
        // Assuming you are inside an Activity or another context-aware class
        mMushroom = new Mushroom(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mHighScore = 0;
        mLives = 3;

    }

    // Called to start a new game
    public void newGame() {

        if (mBackgroundMusicID != -1) {
            mSP.play(mBackgroundMusicID, 1, 1, 0, -1, 1);
        }

        // reset the snake
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        // Get the apple ready for dinner
        mApple.spawn();

        // Reset the mScore
        mScore = 0;

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();

        mTree.spawn();

        mMushroom.spawn();
    }

    private void updateHighScore() {
        if (mScore > mHighScore) {
            mHighScore = mScore;
            // You can save the high score to SharedPreferences or a database here
        }
    }

    // Handles the game loop
    @Override
    public void run() {
        while (mPlaying) {
            if(!mPaused) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }
                else {
                    // Game is paused or over, stop background music
                    mSP.stop(mBackgroundMusicID);
            }
            draw();
        }
    }


    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        final long TARGET_FPS = 10;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }

        return false;
    }


    // Update all the game objects
    public void update() {
        // Move the snake
        mSnake.move();

        // Did the head of the snake eat the apple?
        if (mSnake.checkDinner(mApple.getLocation())) {
            // One day the apple will be ready!
            mApple.spawn();
            // Add to mScore
            mScore = mScore + mApple.getScore();
            // Play a sound
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }

        // Did the snake die?
        if (mSnake.detectDeath()) {
            // Play a sound
            mSP.play(mCrashID, 1, 1, 0, 0, 1);

            // Reduce lives
            mLives--;

            // Check if the snake has no more lives
            if (mLives <= 0) {
                mPaused = true;
                updateHighScore();
                // Optionally, show a game over screen or handle game over logic
            } else {
                // If lives are remaining, reset the game components
                newGame();
            }
        }

        // Check for collision with the tree
        if (mSnake.checkCollisionWithTree(mTree.getLocation())) {
            // Handle collision, e.g., reduce snake length or speed
            mSnake.reduceSnakeLength();
            // Subtract the random value every time it hits a tree
            mScore = mScore - mTree.getScore();
            // You might want to add a method like reduceSnakeLength() in the Snake class
        }

        if (mSnake.checkDinnerMushroom(mMushroom.getLocation()) && !mMushroom.isActivated()) {
            mMushroom.spawn();
            // Activate the mushroom
            // mMushroom.activate();

            // Apply mushroom effects, e.g., increase snake speed
            mSnake.increaseSpeed();
            mSP.play(mEatMushroomID, 1, 1, 0, 0, 1);
        }

        updateHighScore();

        // Reset mLives to 3 if it reaches 0
        if (mLives <= 0) {
            mLives = 3;
        }
    }



    // Do all the drawing
    public void draw() {
        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Fill the screen with a color
            mCanvas.drawColor(Color.argb(255, 0, 0, 182));

            // Set the size and color of the mPaint for the text
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(60);

            // Draw the score
            mCanvas.drawText("Score: " + mScore, 20, 120, mPaint);

            // Draw the apple and the snake
            mApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            //draw the tree
            mTree.draw(mCanvas, mPaint);
            mMushroom.draw(mCanvas, mPaint);



            // Draw some text while paused
            if(mPaused){

                // Set the size and color of the mPaint for the text
                mPaint.setColor(Color.argb(255, 255, 255, 0));
                mPaint.setTextSize(100);

                // Draw the message
                // We will give this an international upgrade soon
                //mCanvas.drawText("Tap To Play!", 200, 700, mPaint);
                mCanvas.drawText(getResources().
                                getString(R.string.tap_to_play),
                        1650, 950, mPaint);
            }

            // Draw the high score
            mCanvas.drawText("High Score: " + mHighScore, 20, 240, mPaint);
            mPaint.setTextSize(60);
            mCanvas.drawText("Lives: " + mLives, 20, 360, mPaint);
            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    mPaused = false;
                    newGame();

                    // Don't want to process snake direction for this tap
                    return true;
                }

                // Let the Snake class handle the input
                mSnake.switchHeading(motionEvent);
                break;

            default:
                break;

        }
        return true;
    }


    // Stop the thread
    // Start the thread
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
        if (!mPaused && mBackgroundMusicID != -1) {
            mSP.play(mBackgroundMusicID, 1, 1, 0, -1, 1);
        }
    }

    // Stop the thread
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();

            // Stop the background music
            mSP.stop(mBackgroundMusicID);
        } catch (InterruptedException e) {
            // Error handling
        }
    }
}

