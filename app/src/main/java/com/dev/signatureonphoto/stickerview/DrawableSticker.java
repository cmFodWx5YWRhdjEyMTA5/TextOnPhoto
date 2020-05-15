package com.dev.signatureonphoto.stickerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;


public class DrawableSticker extends Sticker {

    private Drawable drawable;
    private Rect realBounds;
    public static final int TYPE_LINE=2;
    public static final int TYPE_BORDER=3;
    public static final int TYPE_TYPO=4;
    public static final int TYPE_NORMAL=1;
//    private Paint borderPaint;

    private int typeSticker;
    public DrawableSticker(Drawable drawable,int typeSticker) {
//        borderPaint=new Paint();
//        borderPaint.setAntiAlias(true);
//        borderPaint.setColor(Color.parseColor("#BBFFFFFF"));
//        borderPaint.setAlpha(128);
        this.drawable = drawable;
        this.typeSticker = typeSticker;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    @NonNull
    @Override public Drawable getDrawable() {
        return drawable;
    }

    @Override public DrawableSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    @Override public void draw(@NonNull Canvas canvas) {
//
//        if (isBackgroundEnable() && !isSelected()) {
//            canvas.save();
//            canvas.concat(getMatrix());
//            canvas.drawRect(realBounds, borderPaint);
//            canvas.restore();
//        }

        canvas.save();
        canvas.concat(getMatrix());
        drawable.setBounds(realBounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    @NonNull
    @Override public DrawableSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    @NonNull
    @Override
    public Sticker setShadowLayer(float radius, float dx, float dy, int shadowColor, int shadowType) {
        return null;
    }

    @NonNull
    @Override
    public Sticker setTextSkewX(float skewX) {
        return null;
    }

    @NonNull
    @Override
    public TextSticker setTextNeon(float radius, float dx, float dy, int shadowColor, int neonType) {
        return null;
    }

    @Override public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }

    public int getTypeSticker() {
        return typeSticker;
    }

    public void setTypeSticker(int typeSticker) {
        this.typeSticker = typeSticker;
    }
}

