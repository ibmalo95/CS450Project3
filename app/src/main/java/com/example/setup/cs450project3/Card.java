package com.example.setup.cs450project3;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Ina on 9/27/16.
 */

public class Card {

    private ImageButton ib;
    private int level;
    private int state;

    public Card(ImageButton ib, int level, int state) {
        this.ib = ib;
        this.level = level;
        this.state = state;
    }

    public void flip() {
        this.state = 1;
    }

    public void unflip() {
        this.state = 0;
    }

    public Integer getLevel() {
        return this.level;
    }
    public ImageButton getIb() {
        return this.ib;
    }
}

