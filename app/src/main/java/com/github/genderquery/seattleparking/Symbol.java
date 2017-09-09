package com.github.genderquery.seattleparking;

import android.graphics.PathEffect;
import android.support.annotation.ColorInt;

public class Symbol {
    @ColorInt public int color;
    public PathEffect pathEffect;

    public Symbol(@ColorInt int color) {
        this.color = color;
    }

    public Symbol(@ColorInt int color, PathEffect pathEffect) {
        this.color = color;
        this.pathEffect = pathEffect;
    }
}
