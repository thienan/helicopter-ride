package net.net16.suvankar.helicopterride;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by suvankar on 11/1/17.
 */

public class Player extends GameObject{
    private int score;
    private int hiscore;    //this will not be deleted even if the activity gets killed
    private int level = 1;
    private int oldLevel = 1;
    private int fuelGauge;
    private final int MAX_FUEL = 250;
    public static final int FUEL_INCREASE = 60;
    private boolean up;     //up or down movement
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    private boolean gameOver = false;
    private float dyChange = 0.2f;
    private final int FUEL_X = 52;
    private final int FUEL_Y = 15;
    private float fuelEmptyRate = -2;
    private boolean levelUp = false;
    private Bitmap levelUpBitmap;
    public static final int X = 100;


    public Player(int w, int h, Bitmap[] sprites, Bitmap levelUp) {
        x = this.X;
        y = GamePanel.getmHeight() - (GamePanel.getmHeight() *3)/4;
        dy = 0;
        score = 0;
        height = h;
        width = w;
        animation.setFrames(sprites);
        animation.setDelay(5);
        startTime = System.nanoTime();
        fuelGauge = MAX_FUEL;
        levelUpBitmap = levelUp;
    }

    public void update() {
        //increment the score
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if(elapsed>600) {
            score++;
            startTime = System.nanoTime();
            fuelGauge += fuelEmptyRate;
            if(fuelGauge<0)
                fuelGauge = 0;
        }

        //set the level
        level = score / 100 + 1;
        //when level is changing
        if(level - oldLevel>0) {
            levelUp = true;
            oldLevel = level;
        }

        switch (level) {
            case 1:
                dyChange = 0.2f;
                fuelEmptyRate = -2;
                break;
            case 2:
                dyChange = 0.25f;
                fuelEmptyRate = -2.3f;
                break;
            case 3:
                dyChange = 0.3f;
                fuelEmptyRate = -2.8f;
                break;
            case 4:
                dyChange = 0.35f;
                fuelEmptyRate = -3.3f;
                break;
            case 5:
                dyChange = 0.4f;
                fuelEmptyRate = -3.8f;
                break;
            case 6:
                dyChange = 0.45f;
                fuelEmptyRate = -4.3f;
                break;
            case 7:
                dyChange = 0.5f;
                fuelEmptyRate = -4.8f;
                break;
            default:
                dyChange = 0.55f;
                fuelEmptyRate = -5.3f;
                break;
        }

        //update the animation of bitmap -- helicopter
        animation.update();

        if(up) {
            dy -= (dyChange+0.05f);
        }
        else {
            dy += dyChange;
        }

        /*if(score%100 == 0){
            dyChange += 0.05f;
        }*/

        if(dy>14) dy=14;
        if(dy<-14) dy=-14;
        y += dy;

        if(fuelEmptyRate<-7) {
            fuelEmptyRate = -7;
        }

        //game over
        /*if(y<=0 || y>=(GamePanel.mHeight - 25)) {
            playing = false;
            gameOver = true;
        }*/
    }

    private int levelUpShowTimerCount = 0;
    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(),x,y,null);

        //fuel gauge
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        canvas.drawRect(FUEL_X-2, FUEL_Y-2, FUEL_X + MAX_FUEL + 2, FUEL_Y + 17, paint);
        if(fuelGauge>=120)
            paint.setColor(Color.parseColor("#06b723"));
        else if(fuelGauge>=60)
            paint.setColor(Color.parseColor("#FFD2C813"));
        else
            paint.setColor(Color.RED);
        paint.setStrokeWidth(0);
        canvas.drawRect(FUEL_X, FUEL_Y, FUEL_X + fuelGauge, FUEL_Y + 15, paint);

        //canvas.drawBitmap(levelUpBitmap, GamePanel.mWidth/2 - 50, GamePanel.mHeight/2, null);
        //show level up whem level up happens
        if(levelUp) {
            levelUpShowTimerCount ++;
            if(levelUpShowTimerCount < 50) {    //then level up msg will be showing only for 50 iteration of draw method
                canvas.drawBitmap(levelUpBitmap, GamePanel.getmWidth() /2 - 50, GamePanel.getmHeight() /2, null);
            }
            else {
                levelUp = false;
                levelUpShowTimerCount = 0;
            }
        }
    }

    public void setUp(boolean b) {
        up =b;
    }

    public int getScore() {
        return score;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void resetDy() {
        dy = 0;
        dyChange = 0.2f;
    }
    
    public void resetAll() {
        this.resetDy();
        this.resetY();
        this.resetScore();
        this.resetFuelGauge();
        this.resetFuelGauge();
    }

    public void resetY() {
        y = GamePanel.getmHeight() - (GamePanel.getmHeight() *3)/4;
    }

    public void resetScore() {
        score = 0;
    }
    
    public void resetFuelEmptyRate() { fuelEmptyRate = -2; }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setHiscore(int hiscore) {
        this.hiscore = hiscore;
    }

    public int getHiscore() {
        return hiscore;
    }

    public void setFuelGauge(int fuelGauge) {
        this.fuelGauge = fuelGauge > MAX_FUEL ? MAX_FUEL : fuelGauge;
    }

    public int getFuelGauge() {
        return fuelGauge;
    }

    public void resetFuelGauge() {
        fuelGauge = MAX_FUEL;
    }

    public boolean isLevelUp() {
        return levelUp;
    }
}
