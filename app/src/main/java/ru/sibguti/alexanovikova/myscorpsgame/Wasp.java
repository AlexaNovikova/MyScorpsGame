package ru.sibguti.alexanovikova.myscorpsgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class Wasp extends Insect {

    public Wasp(Context context) {
        super();
        insectBitmap = new Bitmap[6];
        insectBitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bee1);
        insectBitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bee2);
        insectBitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bee3);
        insectBitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bee4);
        insectBitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bee5);
        insectBitmap[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bee6);
    }

}
