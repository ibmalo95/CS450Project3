package com.example.setup.cs450project3;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

/**
 * Keeps track of ids of imageButtons, maxLevel, and state
 * Created by Ina on 9/27/16.
 */

public class Card {

    private int id;
    private int level;
    private int state;

    public Card(int id, int level, int state) {
        this.id = id;
        this.level = level;
        this.state = state;
    }

    public void flip() {
        this.state = 1;
    }

    public void unflip() {
        this.state = 0;
    }

    public void setId(int imgButton) {
        this.id = imgButton;
    }

    public Integer getLevel() {
        return this.level;
    }
    public Integer getId() {
        return this.id;
    }
    public Integer getState() {return this.state;}

    public void updateState(ImageButton ib) {
        if (this.getState() == 1) {
            Drawable drawable = (ib.getDrawable());
            drawable.setLevel(this.getLevel());
        }
    }
}

