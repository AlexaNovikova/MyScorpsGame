package ru.sibguti.alexanovikova.myscorpsgame;

public class WaspPool extends InsectsPool<Wasp> {

    public WaspPool(Panel panel) {
        super(panel);
    }

    @Override
    protected Wasp newInsect() {
        return new Wasp(panel.getContext());
    }
}
