package ru.sibguti.alexanovikova.myscorpsgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class StartCount {

    private final int TIME_INTERVAL = 40;
    private final float X_POS = 350;
    private final float Y_POS = 500;
    private final float SCALE = 0.3f;

    private final Bitmap oneNumberBitmap;
    private final Bitmap twoNumberBitmap;
    private final Bitmap threeNumberBitmap;
    private final Bitmap goBitmap;
    private final Matrix matrix;
    private final Context context;
    private int timeCounter;

    public StartCount(Context context) {
        this.context = context;
        this.oneNumberBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.one);
        this.twoNumberBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.two);
        this.threeNumberBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.three);
        this.goBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.go);
        this.matrix = new Matrix();
        matrix.postScale(SCALE, SCALE);
        matrix.postTranslate(X_POS, Y_POS);
    }

    public void draw(Canvas canvas){
        timeCounter++;
        if (timeCounter<TIME_INTERVAL){
            canvas.drawBitmap(oneNumberBitmap, matrix, null);
        }
        else if (timeCounter< 2*TIME_INTERVAL){
            canvas.drawBitmap(twoNumberBitmap, matrix, null);
        }
        else if(timeCounter<3*TIME_INTERVAL){
            canvas.drawBitmap(threeNumberBitmap, matrix, null);
        }
        else if (timeCounter< 4*TIME_INTERVAL){
            canvas.drawBitmap(goBitmap, matrix, null);
        }
    }

    public boolean isShown(){
        return timeCounter<4*TIME_INTERVAL;
    }
}
