package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;

class Snake {

    // The location in the grid of all the segments
    private ArrayList<Point> segmentLocations;

    // How big is each segment of the snake?
    private int segmentSize;

    // How big is the entire grid
    private Point moveRange;

    // Where is the centre of the screen
    // horizontally in pixels?
    private int halfWayPoint;

    // For tracking movement Heading
    private enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;

    // A bitmap for each direction the head can face
    private Bitmap bitmapHeadRight;
    private Bitmap bitmapHeadLeft;
    private Bitmap bitmapHeadUp;
    private Bitmap bitmapHeadDown;

    // A bitmap for the body
    private Bitmap bitmapBody;


    Snake(Context context, Point mr, int ss) {

        // Initialize our ArrayList
        segmentLocations = new ArrayList<>();

        // Initialize the segment size and movement
        // range from the passed in parameters
        segmentSize = ss;
        moveRange = mr;

        // Create and scale the bitmaps
        bitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Create 3 more versions of the head for different headings
        bitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        bitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        bitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Modify the bitmaps to face the snake head
        // in the correct direction
        bitmapHeadRight = Bitmap
                .createScaledBitmap(bitmapHeadRight,
                        ss, ss, false);

        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        bitmapHeadLeft = Bitmap
                .createBitmap(bitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // A matrix for rotating
        matrix.preRotate(-90);
        bitmapHeadUp = Bitmap
                .createBitmap(bitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Matrix operations are cumulative
        // so rotate by 180 to face down
        matrix.preRotate(180);
        bitmapHeadDown = Bitmap
                .createBitmap(bitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Create and scale the body
        bitmapBody = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body);

        bitmapBody = Bitmap
                .createScaledBitmap(bitmapBody,
                        ss, ss, false);

        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
        halfWayPoint = mr.x * ss / 2;
    }

    // Get the snake ready for a new game
    void reset(int w, int h) {

        // Reset the heading
        heading = Heading.RIGHT;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single snake segment
        segmentLocations.add(new Point(w / 2, h / 2));
    }


    void move() {
        // Move the body and head
        moveBody();
        moveHead();
    }

    private void moveBody() {
        // Move the body segments
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }
    }

    private void moveHead() {
        // Get the head's current location
        Point p = segmentLocations.get(0);

        // Update the head's position based on the current heading
        switch (heading) {
            case UP:
                p.y--;
                break;
            case RIGHT:
                p.x++;
                break;
            case DOWN:
                p.y++;
                break;
            case LEFT:
                p.x--;
                break;
        }
    }
    boolean detectDeath() {
        // Has the snake died?
        boolean dead = false;

        // Hit any of the screen edges
        if (segmentLocations.get(0).x == -1 ||
                segmentLocations.get(0).x > moveRange.x ||
                segmentLocations.get(0).y == -1 ||
                segmentLocations.get(0).y > moveRange.y) {

            dead = true;
        }

        // Eaten itself?
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            // Have any of the sections collided with the head
            if (segmentLocations.get(0).x == segmentLocations.get(i).x &&
                    segmentLocations.get(0).y == segmentLocations.get(i).y) {

                dead = true;
            }
        }
        return dead;
    }

    boolean checkDinner(Point l) {
        //if (snakeXs[0] == l.x && snakeYs[0] == l.y) {
        if (segmentLocations.get(0).x == l.x &&
                segmentLocations.get(0).y == l.y) {

            // Add a new Point to the list
            // located off-screen.
            // This is OK because on the next call to
            // move it will take the position of
            // the segment in front of it
            segmentLocations.add(new Point(-10, -10));
            return true;
        }
        return false;
    }

    void draw(Canvas canvas, Paint paint) {

        // Don't run this code if ArrayList has nothing in it
        if (!segmentLocations.isEmpty()) {
            // All the code from this method goes here
            // Draw the head
            switch (heading) {
                case RIGHT:
                    canvas.drawBitmap(bitmapHeadRight,
                            segmentLocations.get(0).x
                                    * segmentSize,
                            segmentLocations.get(0).y
                                    * segmentSize, paint);
                    break;

                case LEFT:
                    canvas.drawBitmap(bitmapHeadLeft,
                            segmentLocations.get(0).x
                                    * segmentSize,
                            segmentLocations.get(0).y
                                    * segmentSize, paint);
                    break;

                case UP:
                    canvas.drawBitmap(bitmapHeadUp,
                            segmentLocations.get(0).x
                                    * segmentSize,
                            segmentLocations.get(0).y
                                    * segmentSize, paint);
                    break;

                case DOWN:
                    canvas.drawBitmap(bitmapHeadDown,
                            segmentLocations.get(0).x
                                    * segmentSize,
                            segmentLocations.get(0).y
                                    * segmentSize, paint);
                    break;
            }

            // Draw the snake body one block at a time
            for (int i = 1; i < segmentLocations.size(); i++) {
                canvas.drawBitmap(bitmapBody,
                        segmentLocations.get(i).x
                                * segmentSize,
                        segmentLocations.get(i).y
                                * segmentSize, paint);
            }
        }
    }


    // Handle changing direction
    void switchHeading(MotionEvent motionEvent) {

        // Is the tap on the right hand side?
        if (motionEvent.getX() >= halfWayPoint) {
            switch (heading) {
                // Rotate right
                case UP:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.UP;
                    break;

            }
        } else {
            // Rotate left
            switch (heading) {
                case UP:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.UP;
                    break;
            }
        }
    }
}
