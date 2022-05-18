package ru.sibguti.alexanovikova.myscorpsgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class Spider extends Insect{

    public Spider(Context context){
        insectBitmap = new Bitmap[6];
        insectBitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spider1);
        insectBitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spider2);
        insectBitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spider3);
        insectBitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spider4);
        insectBitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spider5);
        insectBitmap[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spider6);
    }
}
