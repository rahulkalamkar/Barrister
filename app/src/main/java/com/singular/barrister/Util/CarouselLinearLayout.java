package com.singular.barrister.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.singular.barrister.Fragment.CarouselPagerAdapter;

public class CarouselLinearLayout extends LinearLayout {
    private float scale = CarouselPagerAdapter.BIG_SCALE;

    public CarouselLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarouselLinearLayout(Context context) {
        super(context);
    }

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;

    public void setScaleBoth(float scale) {
        this.scale = scale;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // The main mechanism to display scale animation, you can customize it as your needs
        int w = this.getWidth();
        int h = this.getHeight();
        if (SMALL_SCALE == scale) {
            float scaleVertical = 0.2f;
            canvas.scale(scaleVertical, scale, w / 2, h / 2);
        } else {
            canvas.scale(scale, scale, w / 2, h / 2);
        }
    }
}