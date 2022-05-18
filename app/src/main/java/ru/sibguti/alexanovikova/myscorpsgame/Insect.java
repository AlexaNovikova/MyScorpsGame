package ru.sibguti.alexanovikova.myscorpsgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

public abstract class Insect {

    private final int FRAME_TIME = 6;
    private int directionTime;
    private float speed;
    private float x, y;
    private float dx, dy;
    private float scale;
    protected Bitmap[] insectBitmap;
    private Matrix matrix;
    private boolean isDestroyed;
    private boolean isBlood;
    private Bitmap [] bloodBitmap;
    private Context context;
    private int currentFrame = 0;
    private int timeToChangeFrame = 0;
    private int bloodFrame;
    private int score;
    private int dirTime;


    public void set(Context context, View view, float speed, float scale, int score) {
        this.speed = speed;
        this.directionTime = (int) (300/speed);
        this.scale = scale;
        this.matrix = new Matrix();
        this.context = context;
        this.bloodBitmap = new Bitmap[10];
        bloodBitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood1);
        bloodBitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood2);
        bloodBitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood3);
        bloodBitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood4);
        bloodBitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood5);
        bloodBitmap[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood6);
        bloodBitmap[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood7);
        bloodBitmap[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood8);
        bloodBitmap[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood9);
        bloodBitmap[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood10);
        isDestroyed = false;
        this.score = score;
        matrix.postScale(scale, scale);
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
        matrix.postTranslate(x, y);
    }

    private void move(Canvas canvas) {
        timeToChangeFrame++;
        dirTime++;
        if(timeToChangeFrame>FRAME_TIME){
            timeToChangeFrame=0;
            currentFrame++;
            if(currentFrame==5){
                currentFrame=0;
            }
        }
        x += dx;
        y += dy;
        if ((x + insectBitmap[currentFrame].getWidth() * scale) > canvas.getWidth()) {
            dx = -speed;
        }
        if (x < 0) {
            dx = speed;
        }
        if (y + insectBitmap[currentFrame].getHeight() * scale > canvas.getHeight()) {
            dy = -speed;
        }
        if (y < 0) {
            dy = speed;
        }
        else {
            if(dirTime>directionTime){
                float r = (float)Math.random();
                if(r<0.5f) {
                    dx = speed;
                }
                else dx = -speed;
                float t = (float)Math.random();
                if(t<0.5f) {
                    dy = speed;
                }
                else dy = -speed;
                dirTime = 0;
            }
        }
        matrix.postTranslate(dx, dy); // перемещаем по координатам X и Y
    }

    public void draw(Canvas canvas) {
                if(!isBlood){
                    move(canvas);
                    canvas.drawBitmap(insectBitmap[currentFrame], matrix, null);
                }
                if(isBlood){
                    canvas.drawBitmap(bloodBitmap[bloodFrame++], matrix, null);
                    if(bloodFrame==10){
                        bloodFrame = 0;
                        isBlood = false;
                        isDestroyed = true;
                    }
                }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void flushDestroy() {
        isDestroyed = false;
    }

    public void destroy() {
        isBlood = true;
    }

    public float getWidth() {
        return insectBitmap[currentFrame].getWidth();
    }

    public float getHeight() {
        return insectBitmap[currentFrame].getHeight();
    }

    public boolean isTouched(float x, float y) {
        return x > this.x && x < this.x + insectBitmap[currentFrame].getWidth() * scale
                && y > this.y && y < this.y + insectBitmap[currentFrame].getHeight() * scale;
    }

    public int getScore(){
        return score;
    }

    public  float getScale(){
        return scale;
    };

    public void setDirection(float dx, float dy){
        this.dx = dx*speed;
        this.dy = dy*speed;
    };
}
