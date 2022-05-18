package ru.sibguti.alexanovikova.myscorpsgame;

public class SpiderPool extends InsectsPool<Spider> {

    public SpiderPool(Panel panel) {
        super(panel);
    }

    @Override
    protected Spider newInsect() {
        return new Spider(panel.getContext());
    }
}
