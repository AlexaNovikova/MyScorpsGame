package ru.sibguti.alexanovikova.myscorpsgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

public class SpiderEmitter extends InsectEmitter<SpiderPool>{

    public SpiderEmitter(SpiderPool insectsPool, View view, Context context, Bitmap bloodBitmap) {
        super(insectsPool, view, context, bloodBitmap);
    }
}
