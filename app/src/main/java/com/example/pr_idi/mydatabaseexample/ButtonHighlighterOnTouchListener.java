package com.example.pr_idi.mydatabaseexample;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Andreu on 08/12/2016.
 */
public class ButtonHighlighterOnTouchListener implements View.OnTouchListener {
    final Button button;

    public ButtonHighlighterOnTouchListener(final Button button) {
        super();
        this.button = button;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        //grey color filter, you can change the color as you like
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            button.setHighlightColor(Color.argb(155, 185, 185, 185));
        }else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setHighlightColor(Color.argb(0, 185, 185, 185));
        }
        return false;
    }
}
