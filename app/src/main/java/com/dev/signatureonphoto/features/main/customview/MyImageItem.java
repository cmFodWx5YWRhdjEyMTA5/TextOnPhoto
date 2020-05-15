package com.dev.signatureonphoto.features.main.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class MyImageItem extends ImageView {

    public MyImageItem(Context context) {
        super(context);
    }

    public MyImageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=getMeasuredWidth();
        setMeasuredDimension(width,width);
    }
}
