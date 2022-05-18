package ru.sibguti.alexanovikova.myscorpsgame;


import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public abstract class InsectsPool<T extends Insect>{

    protected final List<T> activeInsects = new ArrayList<>();
    protected final List<T> freeInsects = new ArrayList<>();
    protected Panel panel;

    public InsectsPool(Panel panel) {
        this.panel = panel;
    }

    protected abstract T newInsect();

    public Insect obtain() {
        T insect;
        if (freeInsects.isEmpty()) {
            insect = newInsect();
        } else {
            insect = freeInsects.remove(freeInsects.size() - 1);
        }
        activeInsects.add(insect);
        System.out.println(getClass().getName() + " active/free : " + activeInsects.size() + "/" + freeInsects.size());
        return insect;
    }

    public void drawActiveInsects(Canvas canvas) {
        for (T insect : activeInsects) {
       if (!insect.isDestroyed()) {
                insect.draw(canvas);
          }
        }
    }

    public void freeAllActiveInsects() {
        freeInsects.addAll(activeInsects);
        activeInsects.clear();
    }


    public void freeAllDestroyedActiveInsects() {
        for (int i = 0; i < activeInsects.size(); i++) {
           T insect = activeInsects.get(i);
            if (insect.isDestroyed()) {
                free(insect);
                i--;
                insect.flushDestroy();
            }
        }
    }

    private void free(T insect) {
        if (activeInsects.remove(insect)) {
            freeInsects.add(insect);
            System.out.println(getClass().getName() + " active/free : " + activeInsects.size() + "/" + freeInsects.size());
        }
    }

    public List<T> getActiveInsects() {
        return activeInsects;
    }

    public boolean checkCollisions(float x, float y) {
        for (T insect : activeInsects) {
            if (insect.isTouched(x, y)) {
                panel.addScore(insect.getScore());
                insect.destroy();
                panel.playBoomSound();
                return true;
            }
        }
        return false;
    }
}
