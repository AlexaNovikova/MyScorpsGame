package ru.sibguti.alexanovikova.myscorpsgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.view.View;

import java.util.Random;


class Panel extends View {

    private static final int GENERATE_INTERVAL = 40;
    private static int insectsMaxAmount;
    private static final String TIME_PLAY = "00:02:00";
    private Bitmap bloodBitmap;
    private StartCount startCount;
    private WaspPool waspPool;
    private SpiderPool spiderPool;
    private SpiderEmitter spiderEmitter;
    private WaspEmitter waspEmitter;
    private int timeCounter = 0;
    private SoundPool sounds;
    private int sCollision;
    private Bitmap menuButton;
    private Matrix menuMatrix;
    private boolean isStarting;
    private Random random;
    private int r;
    private Paint paint;
    private int score;
    private String time ="";
    private int minutes;
    private int secs;
    private int sec;
    private boolean isFinished;
    private String nick;

    public Panel(Context context) {// Конструктор
        super(context);
        setBackgroundResource(R.drawable.bg);
        isStarting = true;
        startCount = new StartCount(context);
        menuButton = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
        menuMatrix = new Matrix();
        menuMatrix.postScale(0.2f, 0.2f);
        menuMatrix.postTranslate(400, 1000);
        bloodBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blood);
        spiderPool = new SpiderPool(this);
        waspPool = new WaspPool(this);
        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        sCollision = sounds.load(context, R.raw.boom, 1);
        spiderEmitter = new SpiderEmitter(spiderPool, this, context, bloodBitmap);
        waspEmitter = new WaspEmitter(waspPool, this, context, bloodBitmap);
        random = new Random();
        paint = new Paint();
        paint.setColor(Color.rgb(100, 255, 10));
        paint.setTextSize(60);

    }

    private void createTimer() {
        new CountDownTimer(120000, 1000) {
            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                minutes = (int) (millisUntilFinished / 60000);
                secs = (int) (millisUntilFinished % 60000) /1000;
                if(secs<10){
                    time = "0"
                            + minutes +":0"+ secs;
                }
                else {
                    time = "0"
                            + minutes +":"+ secs;
                }
            }

            public void onFinish() {
                time = "Game over!";
                isFinished = true;

            }
        }
                .start();
    }



    void drawView(Canvas c) {
        waspPool.drawActiveInsects(c);
        spiderPool.drawActiveInsects(c);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isFinished) {
            if (isStarting) {
                if (startCount.isShown()) {
                    startCount.draw(canvas);
                } else {
                    isStarting = false;
                    createTimer();
                }
            }
        if (!isStarting) {
            canvas.drawText("SRORE: " + score,
                    80, 100, paint);
            canvas.drawText(time, 900, 100, paint);
            drawView(canvas);
            timeCounter += 1;
            waspPool.freeAllDestroyedActiveInsects();
            spiderPool.freeAllDestroyedActiveInsects();

            if (timeCounter > GENERATE_INTERVAL) {
                timeCounter = 0;
                if (waspPool.activeInsects.size() + spiderPool.activeInsects.size() < insectsMaxAmount) {
                    r = random.nextInt(2);
                    switch (r) {
                        case 0:
                            spiderEmitter.generate(canvas);
                            break;
                        case 1:
                            waspEmitter.generate(canvas);
                            break;
                    }
                }
            }
        }
        }
        else{
            paint.setTextSize(120);
            canvas.drawText("GAME OVER!",
                    200, 600, paint);
            canvas.drawText(nick+ " score: " + score, 50, 800, paint);
            canvas.drawBitmap(menuButton, menuMatrix, null);
        }
        invalidate(); //перерисовать весь холст заново
    }

    public WaspPool getWaspPool() {
        return waspPool;
    }

    public SpiderPool getSpiderPool() {
        return spiderPool;
    }

    public void playBoomSound() {
        sounds.play(sCollision, 1.0f, 1.0f, 0, 0, 1.5f);
    }

    public boolean checkButtonClick(float x, float y) {
        return  x > 400 && x < 400 + menuButton.getWidth() * 0.2 && y > 1000 && y < 1000 + menuButton.getHeight() * 0.2;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setScorpsAmount(String scorpsAmount) {
        insectsMaxAmount = Integer.parseInt(scorpsAmount);
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public String getNick() {
        return nick;
    }
}
