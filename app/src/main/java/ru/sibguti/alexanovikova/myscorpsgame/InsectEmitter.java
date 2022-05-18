package ru.sibguti.alexanovikova.myscorpsgame;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.util.Random;


public abstract class InsectEmitter <T extends InsectsPool>{

    private final Bitmap BLOOD_BITMAP;

    private static final float INSECT_BIG_HEIGHT = 1.5f;
    private static final float INSECT_SMALL_HEIGHT = 1f;
    private static final float INSECT_MEDIUM_HEIGHT = 1.3f;

    private static final int INSECT_BIG_SCORE = 3;
    private static final int INSECT_SMALL_SCORE = 5;
    private static final int INSECT_MEDIUM_SCORE = 4;

    private final float INSECT_MAX_SPEED = 9f;
    private final float INSECT_MIN_SPEED = 3f;
    private final float INSECT_MEDIUM_SPEED = 5f;

    private final T insectsPool;
    private final View view;

    private Context context;
    private Random random;

    public InsectEmitter(T insectsPool, View view, Context context,
                         Bitmap bloodBitmap) {
        this.insectsPool = insectsPool;
        this.view = view;
        BLOOD_BITMAP = bloodBitmap;
        this.context = context;
        this.random = new Random(2);
    }

    public void generate(Canvas canvas) {
        Insect insect = insectsPool.obtain();
        float type = (float) Math.random();
        if (type < 0.4f) {
            insect.set(
                    context,
                    view,
                    INSECT_MAX_SPEED,
                    INSECT_SMALL_HEIGHT,
                    INSECT_SMALL_SCORE
            );
        } else if (type < 0.7f) {
            insect.set(
                    context,
                    view,
                    INSECT_MEDIUM_SPEED,
                    INSECT_MEDIUM_HEIGHT,
                    INSECT_MEDIUM_SCORE
            );
        } else  {
            insect.set(
                    context,
                    view,
                    INSECT_MIN_SPEED,
                    INSECT_BIG_HEIGHT,
                    INSECT_BIG_SCORE
            );

        }
        float posX;
        float posY;
        float dx;
        float dy;
        float r = (float) Math.random();
        if(r<0.25f){
            posX =  Rnd.nextFloat(0, canvas.getWidth());
            posY = canvas.getHeight();
            if(r<0.12f){
                dx = 1;
            }
            else dx = -1;
            dy = -1;
        }
        else if (r<0.5f){
            posX =  canvas.getWidth();
            posY = Rnd.nextFloat(0, canvas.getHeight());
            dx = -1;
            if(r<0.38f){
                dy=1;
            }
            else dy = -1;
        }
        else if (r<0.75f){
            posY =  0;
            posX = Rnd.nextFloat(0, canvas.getWidth());
            dy = 1;
            if(r<0.63f){
                dx=1;
            }
            else dx = -1;
        }
        else {
            posY = Rnd.nextFloat(0, canvas.getHeight());
            posX = 0;
            dx = 1;
            if(r<0.88f){
                dy=1;
            }
            else dy = -1;
        }

        insect.setDirection(dx, dy);
        insect.setPos(posX, posY);

    }

}
