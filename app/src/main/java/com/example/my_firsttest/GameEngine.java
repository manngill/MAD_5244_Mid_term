package com.example.my_firsttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {
    private final String TAG = "SPARROW";

    // game thread variables
    private Thread gameThread = null;
    private volatile boolean gameIsRunning;

    // drawing variables
    private Canvas canvas;
    private Paint paintbrush;
    private SurfaceHolder holder;

    // Screen resolution varaibles
    private int screenWidth;
    private int screenHeight;

    // VISIBLE GAME PLAY AREA
    // These variables are set in the constructor
    int VISIBLE_LEFT;
    int VISIBLE_TOP;
    int VISIBLE_RIGHT;
    int VISIBLE_BOTTOM;
    int Speed = 20;

    // SPRITES
    Square bullet;
    int SQUARE_WIDTH = 100;

    Square enemy;

    Sprite player;
    Sprite sparrow;
    Sprite robot;
    Sprite cat;


    int birdA, birdB;

    int xPosition;
    int yPosition;
    int direction = 1;

    ArrayList<Square> bullets = new ArrayList<Square>();

    // GAME STATS
    int score = 0;

    public GameEngine(Context context, int screenW, int screenH) {
        super(context);

        // intialize the drawing variables
        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        // set screen height and width
        this.screenWidth = screenW;
        this.screenHeight = screenH;

        // setup visible game play area variables
        this.VISIBLE_LEFT = 20;
        this.VISIBLE_TOP = 10;
        this.VISIBLE_RIGHT = this.screenWidth - 20;
        this.VISIBLE_BOTTOM = (int) (this.screenHeight * 0.8);


        // initalize sprites
        this.player = new Sprite(this.getContext(), 100, 400, R.drawable.player64);
        this.cat = new Sprite(this.getContext(), this.VISIBLE_RIGHT - 80 ,this.VISIBLE_BOTTOM-80, R.drawable.cat64);
        this.robot = new Sprite(this.getContext(), this.VISIBLE_RIGHT-80 , this.VISIBLE_TOP+20, R.drawable.robot64);

        this.bullet = new Square(context,100, 400, SQUARE_WIDTH);

    }

    @Override
    public void run() {
        while (gameIsRunning == true) {
            updateGame();    // updating positions of stuff
            redrawSprites(); // drawing the stuff
            controlFPS();
        }
    }


    public void updateGame() {
        if (robot.getxPosition() <= this.VISIBLE_LEFT) {
            robot.updateCagePosition();;
            robot.direction = 1;

        }
        else
        {
            robot.direction = 0;
            robot.updateCagePosition();
        }



        if(flag == true)
        {
            if(xx > screenWidth/2 && yy > screenHeight/2)
            {
                this.bullet.setyPosition(bullet.getyPosition() + 100);

            }

            else
            {
                this.bullet.setxPosition(bullet.getxPosition() + 100);
            }



            if (this.robot.getyPosition() == this.bullet.getyPosition() || this.robot.getxPosition() == this.bullet.getxPosition()) {

                Message = "You are Winner";
            }

        }


    }


    public void outputVisibleArea() {

        Random rand = new Random();

        try
        {
            Thread.sleep(3000);
        }

        catch (Exception e)
        {

        }

        this.birdA = rand.nextInt(this.screenWidth/2);
        this.birdB = rand.nextInt(this.screenHeight/2);

        this.sparrow = new Sprite(this.getContext(), birdA, birdB, R.drawable.bird64);

        Log.d(TAG, "DEBUG: The visible area of the screen is:");
        Log.d(TAG, "DEBUG: Maximum w,h = " + this.screenWidth +  "," + this.screenHeight);
        Log.d(TAG, "DEBUG: Visible w,h =" + VISIBLE_RIGHT + "," + VISIBLE_BOTTOM);
        Log.d(TAG, "-------------------------------------");
    }



    public void redrawSprites() {
        if (holder.getSurface().isValid()) {

            // initialize the canvas
            canvas = holder.lockCanvas();
            // --------------------------------

            // set the game's background color
            canvas.drawColor(Color.argb(255,255,255,255));

            // setup stroke style and width
            paintbrush.setStyle(Paint.Style.FILL);
            paintbrush.setStrokeWidth(8);

            // --------------------------------------------------------
            // draw boundaries of the visible space of app
            // --------------------------------------------------------
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setColor(Color.argb(255, 0, 128, 0));

            canvas.drawRect(VISIBLE_LEFT, VISIBLE_TOP, VISIBLE_RIGHT, VISIBLE_BOTTOM, paintbrush);
            this.outputVisibleArea();

            // --------------------------------------------------------
            // draw player and sparrow
            // --------------------------------------------------------

            // 1. player
            canvas.drawBitmap(this.player.getImage(), this.player.getxPosition(), this.player.getyPosition(), paintbrush);

            // 2. sparrow
            canvas.drawBitmap(this.sparrow.getImage(), this.sparrow.getxPosition(), this.sparrow.getyPosition(), paintbrush);


            canvas.drawBitmap(this.cat.getImage(), this.cat.getxPosition(), this.cat.getyPosition(), paintbrush);

            canvas.drawBitmap(this.robot.getImage(), this.robot.getxPosition(), this.robot.getyPosition(), paintbrush);


            // --------------------------------------------------------
            // draw hitbox on player
            // --------------------------------------------------------
            Rect r = player.getHitbox();
            paintbrush.setStyle(Paint.Style.STROKE);
            canvas.drawRect(r, paintbrush);

            // draw bullet

            if((int)xx != 0  && (int)yy !=0) {

                paintbrush.setColor(Color.BLACK);
                canvas.drawRect(
                        this.bullet.getxPosition(),
                        this.bullet.getyPosition(),
                        this.bullet.getxPosition() + this.bullet.getWidth(),
                        this.bullet.getyPosition() + this.bullet.getWidth(),
                        paintbrush
                );

            }
            // --------------------------------------------------------
            // draw hitbox on player
            // --------------------------------------------------------
            paintbrush.setTextSize(60);
            paintbrush.setStrokeWidth(5);
            String screenInfo = "Screen size: (" + this.screenWidth + "," + this.screenHeight + ")";
            canvas.drawText(screenInfo, 10, 100, paintbrush);
            canvas.drawText("", 30,30,paintbrush);

            // --------------------------------
            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void controlFPS() {
        try {
            gameThread.sleep(17);
        }
        catch (InterruptedException e) {

        }
    }


    // Deal with user input
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:

                // handling bullet

                xx = event.getX();
                yy  =  event.getY();
                flag = true;

                Log.d(TAG,"Bullet position: " + this.bullet.getxPosition() + ", " + this.bullet.getyPosition());
                Log.d(TAG,"Enemy position: " + this.player.getxPosition() + ", " + this.player.getyPosition());


                double a = this.robot.getxPosition() - this.bullet.getxPosition();
                double b = this.robot.getyPosition() - this.bullet.getyPosition();


                double d = Math.sqrt((a * a) + (b * b));

                Log.d(TAG, "Distance to cage: " + d);

                double xn = (a / d);
                double yn = (b / d);



                int newA = this.bullet.getxPosition() + (int) (xn * 10);
                int newB = this.bullet.getyPosition() + (int) (yn * 10);

                break;
        }
        return true;
    }

    // Game status - pause & resume
    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {

        }
    }
    public void  resumeGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}
