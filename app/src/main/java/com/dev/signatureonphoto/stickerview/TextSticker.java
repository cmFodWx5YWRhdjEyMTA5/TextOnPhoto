package com.dev.signatureonphoto.stickerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;


public class TextSticker extends Sticker {

    /**
     * Our ellipsis string.
     */
    private static final String mEllipsis = "\u2026";

    private final Context context;
    private final Rect realBounds;
    private final RectF textRect;
    private final TextPaint textPaint;
    TextPaint textBorder;
    private Paint borderPaint;
    private Drawable drawable;
    private StaticLayout staticLayout, staticLayoutBorder;
    private int alignmentType;
    private Layout.Alignment alignment;
    private String text;

    /**
     * Upper bounds for text size.
     * This acts as a starting point for resizing.
     */
    private float maxTextSizePixels;

    /**
     * Lower bounds for text size.
     */
    private float minTextSizePixels;

    /**
     * Line spacing multiplier.
     */
    private float lineSpacingMultiplier = 1.0f;

    /**
     * Additional line spacing.
     */
    private float lineSpacingExtra = 0.0f;

    private float radius;
    private float dx;
    private float dy;
    private int shadowColor;
    private Typeface typeface;
    private int typefaceType = 11;
    private float skewX;
    private int color, shadowType, neonType;

    private int neonColor;

    public TextSticker(@NonNull Context context) {
        this(context, null);
    }

    public TextSticker(@NonNull Context context, @Nullable Drawable drawable) {
        this.context = context;
        this.drawable = drawable;

        if (drawable == null) {
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_background);
        }

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.parseColor(Constants.BODER_COLOR));
        borderPaint.setAlpha(128);
        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textBorder = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        realBounds = new Rect(0, 0, getWidth(), getHeight());
        textRect = new RectF(0, 0, getWidth(), getHeight());
        minTextSizePixels = convertSpToPx(0);
        maxTextSizePixels = convertSpToPx(32);

        switch (getAlignmentType()) {
            case 1:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case 2:
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
            case 3:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
        }

        textBorder.setTextSize(maxTextSizePixels);
        textBorder.setShadowLayer(getRadius(), getDx(), getDy(), getShadowColor());
        //Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        textBorder.setTypeface(getTypeface());
        textBorder.setStyle(Paint.Style.STROKE);
        textBorder.setStrokeWidth(1);
        textBorder.setColor(Color.RED);

        textPaint.setTextSize(maxTextSizePixels);
        textPaint.setShadowLayer(getRadius(), getDx(), getDy(), getShadowColor());
        //Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        textPaint.setTypeface(getTypeface());

    }

    public int getNeonType() {
        return neonType;
    }

    public int getShadowType() {
        return shadowType;
    }

    public int getCurrentTypeface() {
        return typefaceType;
    }

    public int getCurrentTextColor() {
        return color;
    }

    boolean isStroke;

    @Override
    public void draw(@NonNull Canvas canvas) {
        Matrix matrix = getMatrix();

        if (isBackgroundEnable() && !isSelected()) {
            canvas.save();
            canvas.concat(matrix);
            canvas.drawRect(textRect, borderPaint);
            canvas.restore();
        }

        canvas.save();
        canvas.concat(matrix);
        if (drawable != null) {
            drawable.setBounds(realBounds);
            drawable.draw(canvas);
        }
        canvas.restore();

        canvas.save();
        canvas.concat(matrix);
        if (staticLayout != null) {
            if (textRect.width() == getWidth()) {
                int dy = getHeight() / 2 - staticLayout.getHeight() / 2;
                // center vertical
                canvas.translate(0, dy);
            } else {
                float dx = textRect.left;
                float dy = textRect.top + textRect.height() / 2 - staticLayout.getHeight() / 2;
                canvas.translate(dx, dy);
            }
            if (isStroke) {
                staticLayoutBorder.draw(canvas);
            }
            staticLayout.draw(canvas);

        }
        canvas.restore();

    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override
    public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }

    @NonNull
    @Override
    public TextSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        textPaint.setAlpha(alpha);
        textBorder.setAlpha(alpha);
        return this;
    }

    @NonNull
    public TextSticker setTextSize(float textSize) {
        textPaint.setTextSize(convertSpToPx(textSize));
        textBorder.setTextSize(convertSpToPx(textSize));
        return this;
    }

    @NonNull
    @Override
    public TextSticker setShadowLayer(float radius, float dx, float dy, int shadowColor, int shadowType) {
        this.shadowType = shadowType;
        textPaint.setShadowLayer(radius, dx, dy, shadowColor);
        textBorder.setShadowLayer(radius, dx, dy, shadowColor);

        return this;
    }

    @NonNull
    @Override
    public TextSticker setTextSkewX(float skewX) {
        textPaint.setTextSkewX(skewX);
        textBorder.setTextSkewX(skewX);
        return this;
    }


    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public TextSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        realBounds.set(0, 0, getWidth(), getHeight());
        textRect.set(0, 0, getWidth(), getHeight());
        return this;
    }

    @NonNull
    public TextSticker setDrawable(@NonNull Drawable drawable, @Nullable Rect region) {
        this.drawable = drawable;
        realBounds.set(0, 0, getWidth(), getHeight());
        if (region == null) {
            textRect.set(0, 0, getWidth(), getHeight());
        } else {
            textRect.set(region.left, region.top, region.right, region.bottom);
        }
        return this;
    }

    @NonNull
    public TextSticker setTypeface(@Nullable Typeface typeface, int typefaceType) {
        this.typefaceType = typefaceType;
        textPaint.setTypeface(typeface);
        textBorder.setTypeface(typeface);
        return this;
    }

    @NonNull
    public TextSticker setTextStyle(int style) {
        if (textPaint.getTypeface() != null) {
            textPaint.setTypeface(Typeface.create(textPaint.getTypeface(), style));
            textBorder.setTypeface(Typeface.create(textPaint.getTypeface(), style));
        } else {
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, style));
            textBorder.setTypeface(Typeface.create(Typeface.DEFAULT, style));
        }
        return this;
    }

    @NonNull
    public TextSticker setTextColor(@ColorInt int color, int position) {
        this.color = position;
        textPaint.setColor(color);
        return this;
    }

    @NonNull
    public TextSticker setTextAlign(@NonNull Layout.Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @NonNull
    public TextSticker setMaxTextSize(@Dimension(unit = Dimension.SP) float size) {
        textPaint.setTextSize(convertSpToPx(size));
        textBorder.setTextSize(convertSpToPx(size));
        maxTextSizePixels = textPaint.getTextSize();
        return this;
    }

    /**
     * Sets the lower text size limit
     *
     * @param minTextSizeScaledPixels the minimum size to use for text in this view,
     *                                in scaled pixels.
     */
    @NonNull
    public TextSticker setMinTextSize(float minTextSizeScaledPixels) {
        minTextSizePixels = convertSpToPx(minTextSizeScaledPixels);
        return this;
    }

    @NonNull
    public TextSticker setLineSpacing(float add, float multiplier) {
        lineSpacingMultiplier = multiplier;
        lineSpacingExtra = add;
        return this;
    }

    @NonNull
    public TextSticker setStrokeWidth(float width) {
        textBorder.setStrokeWidth(width);
        return this;
    }


    @NonNull
    @Override
    public TextSticker setTextNeon(float radius, float dx, float dy, int shadowColor, int neonType) {
        this.neonType = neonType;
        textPaint.setShadowLayer(radius, dx, dy, shadowColor);
        textBorder.setShadowLayer(radius, dx, dy, shadowColor);
        return this;
    }

    public TextSticker setTextNeonDefault(float radius, float dx, float dy, int neonType) {
        this.neonType = neonType;
        textPaint.setShadowLayer(radius, dx, dy, context.getResources().getColor(R.color.colordefault));
        textBorder.setShadowLayer(radius, dx, dy, context.getResources().getColor(R.color.colordefault));
        return this;
    }
    @NonNull
    public TextSticker setBorderTextColor(int color) {
        textBorder.setColor(color);
        isStroke = true;
        return this;
    }

    @NonNull
    public TextSticker setText(@Nullable String text) {
        this.text = text;
        return this;
    }

    @Nullable
    public String getText() {
        return text;
    }

    /**
     * Resize this view's text size with respect to its width and height
     * (minus padding). You should always call this method after the initialization.
     */
    @NonNull
    public TextSticker resizeText() {
        final float availableHeightPixels = textRect.height();

        final float availableWidthPixels = textRect.width();

        final CharSequence text = getText();

        // Safety check
        // (Do not resize if the view does not have dimensions or if there is no text)
        if (text == null
                || text.length() <= 0
                || availableHeightPixels <= 0
                || availableWidthPixels <= 0
                || maxTextSizePixels <= 0) {
            return this;
        }

        float targetTextSizePixels = maxTextSizePixels;
        int targetTextHeightPixels =
                getTextHeightPixels(text, (int) availableWidthPixels, targetTextSizePixels);

        // Until we either fit within our TextView
        // or we have reached our minimum text size,
        // incrementally try smaller sizes
        while (targetTextHeightPixels > availableHeightPixels
                && targetTextSizePixels > minTextSizePixels) {
            targetTextSizePixels = Math.max(targetTextSizePixels - 2, minTextSizePixels);

            targetTextHeightPixels =
                    getTextHeightPixels(text, (int) availableWidthPixels, targetTextSizePixels);
        }

        // If we have reached our minimum text size and the text still doesn't fit,
        // append an ellipsis
        // (NOTE: Auto-ellipsize doesn't work hence why we have to do it here)
        if (targetTextSizePixels == minTextSizePixels
                && targetTextHeightPixels > availableHeightPixels) {
            // Make a copy of the original TextPaint object for measuring
            TextPaint textPaintCopy = new TextPaint(textPaint);
            textPaintCopy.setTextSize(targetTextSizePixels);

            // Measure using a StaticLayout instance
            StaticLayout staticLayout =
                    new StaticLayout(text, textPaintCopy, (int) availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                            lineSpacingMultiplier, lineSpacingExtra, false);

            // Check that we have a least one line of rendered text
            if (staticLayout.getLineCount() > 0) {
                // Since the line at the specific vertical position would be cut off,
                // we must trim up to the previous line and add an ellipsis
                int lastLine = staticLayout.getLineForVertical((int) availableHeightPixels) - 1;

                if (lastLine >= 0) {
                    int startOffset = staticLayout.getLineStart(lastLine);
                    int endOffset = staticLayout.getLineEnd(lastLine);
                    float lineWidthPixels = staticLayout.getLineWidth(lastLine);
                    float ellipseWidth = textPaintCopy.measureText(mEllipsis);

                    // Trim characters off until we have enough room to draw the ellipsis
                    while (availableWidthPixels < lineWidthPixels + ellipseWidth) {
                        endOffset--;
                        lineWidthPixels =
                                textPaintCopy.measureText(text.subSequence(startOffset, endOffset + 1).toString());
                    }

                    setText(text.subSequence(0, endOffset) + mEllipsis);
                }
            }
        }
        textPaint.setTextSize(targetTextSizePixels);
        textBorder.setTextSize(targetTextSizePixels);
        staticLayout =
                new StaticLayout(this.text, textPaint, (int) textRect.width(), alignment, lineSpacingMultiplier,
                        lineSpacingExtra, true);
        staticLayoutBorder =
                new StaticLayout(this.text, textBorder, (int) textRect.width(), alignment, lineSpacingMultiplier,
                        lineSpacingExtra, true);

        return this;
    }

    @NonNull
    public TextSticker resizeText(float size) {
        final float availableHeightPixels = textRect.height();

        final float availableWidthPixels = textRect.width();

        final CharSequence text = getText();

        // Safety check
        // (Do not resize if the view does not have dimensions or if there is no text)
        if (text == null
                || text.length() <= 0
                || availableHeightPixels <= 0
                || availableWidthPixels <= 0
                || maxTextSizePixels <= 0) {
            return this;
        }

        float targetTextSizePixels = maxTextSizePixels;
        int targetTextHeightPixels =
                getTextHeightPixels(text, (int) availableWidthPixels, targetTextSizePixels);


        if (targetTextSizePixels == minTextSizePixels
                && targetTextHeightPixels > availableHeightPixels) {
            // Make a copy of the original TextPaint object for measuring
            TextPaint textPaintCopy = new TextPaint(textPaint);
            textPaintCopy.setTextSize(targetTextSizePixels);

            // Measure using a StaticLayout instance
            StaticLayout staticLayout =
                    new StaticLayout(text, textPaintCopy, (int) availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                            lineSpacingMultiplier, lineSpacingExtra, false);

            // Check that we have a least one line of rendered text
            if (staticLayout.getLineCount() > 0) {
                // Since the line at the specific vertical position would be cut off,
                // we must trim up to the previous line and add an ellipsis
                int lastLine = staticLayout.getLineForVertical((int) availableHeightPixels) - 1;

                if (lastLine >= 0) {
                    int startOffset = staticLayout.getLineStart(lastLine);
                    int endOffset = staticLayout.getLineEnd(lastLine);
                    float lineWidthPixels = staticLayout.getLineWidth(lastLine);
                    float ellipseWidth = textPaintCopy.measureText(mEllipsis);

                    // Trim characters off until we have enough room to draw the ellipsis
                    while (availableWidthPixels < lineWidthPixels + ellipseWidth) {
                        endOffset--;
                        lineWidthPixels =
                                textPaintCopy.measureText(text.subSequence(startOffset, endOffset + 1).toString());
                    }

                    setText(text.subSequence(0, endOffset) + mEllipsis);
                }
            }
        }
        textPaint.setTextSize(size);
        staticLayout =
                new StaticLayout(this.text, textPaint, (int) textRect.width(), alignment, lineSpacingMultiplier,
                        lineSpacingExtra, true);

        return this;
    }

    /**
     * @return lower text size limit, in pixels.
     */
    public float getMinTextSizePixels() {
        return minTextSizePixels;
    }

    /**
     * Sets the text size of a clone of the view's {@link TextPaint} object
     * and uses a {@link StaticLayout} instance to measure the height of the text.
     *
     * @return the height of the text when placed in a view
     * with the specified width
     * and when the text has the specified size.
     */
    protected int getTextHeightPixels(@NonNull CharSequence source, int availableWidthPixels,
                                      float textSizePixels) {
        textPaint.setTextSize(textSizePixels);
        // It's not efficient to create a StaticLayout instance
        // every time when measuring, we can use StaticLayout.Builder
        // since api 23.
        StaticLayout staticLayout =
                new StaticLayout(source, textPaint, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                        lineSpacingMultiplier, lineSpacingExtra, true);
        return staticLayout.getHeight();
    }

    /**
     * @return the number of pixels which scaledPixels corresponds to on the device.
     */
    private float convertSpToPx(float scaledPixels) {
        return scaledPixels * context.getResources().getDisplayMetrics().scaledDensity;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public float getSkewX() {
        return skewX;
    }

    public void setSkewX(float skewX) {
        this.skewX = skewX;
    }

    public int getNeonColor() {
        return neonColor;
    }

    public void setNeonColor(int neonColor) {
        this.neonColor = neonColor;
    }

    public int getAlignmentType() {
        return alignmentType;
    }

    public void setAlignmentType(int alignmentType) {
        this.alignmentType = alignmentType;
    }
}
