package ru.sibguti.alexanovikova.myscorpsgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    float x;
    float y;
    private Panel panel;
    public SharedPreferences sharedPreferences;
    private static final String fileName = "score.txt";

    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(MainActivity.this, SoundService.class));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        panel = new Panel(this);
        panel.setScorpsAmount(this.getIntent().getStringExtra("scorpsAmount"));
        panel.setNick(this.getIntent().getStringExtra("nick"));
        panel.setOnTouchListener(this);
        setContentView(panel);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                if (!panel.isFinished()) {
                    if (!panel.getSpiderPool().checkCollisions(x, y) &&
                            !panel.getWaspPool().checkCollisions(x, y)) {
                        panel.addScore(-1);
                    }
                } else {
                    if (panel.checkButtonClick(x, y)) {
                        Intent intent = new Intent(MainActivity.this, Menu.class);
                        intent.putExtra("nick", panel.getNick());
                        intent.putExtra("score", panel.getScore());
                        saveScore();
                        stopService(new Intent(MainActivity.this, SoundService.class));
                        startActivity(intent);
                    }
                }
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveScore() {
        writeFile();
    }

    protected void onDestroy() {
        //stop service and stop music
        stopService(new Intent(MainActivity.this, SoundService.class));
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeFile() {
        try {
            String filePath = Environment.getExternalStorageDirectory() + "/" + fileName;
            File myFile = new File(filePath);// Создается файл, если он не был создан
            String name = panel.getNick() != null ? panel.getNick() : "Unknown user";
            String result = name + ": " + panel.getScore();
            FileWriter writer = new FileWriter(filePath, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(result);
            bufferWriter.write(System.lineSeparator());
            bufferWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}