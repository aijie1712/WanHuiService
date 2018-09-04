package com.cmeplaza.basemodule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.cmeplaza.basemodule.R;

/**
 * 作者：Android_AJ on 2017/4/12.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 * 可以控制Drawable属性值大小的TextView
 */
public class DrawableTextView extends AppCompatTextView {
    private int drawableWidth = 0;
    private int drawableHeight = 0;

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        drawableWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_drawableWidth, 0);
        drawableHeight = a.getDimensionPixelSize(R.styleable.DrawableTextView_drawableHeight, 0);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 设置drawable大小
        Drawable[] drawables = getCompoundDrawables();
        for (Drawable drawable : drawables) {
            setDrawableSize(drawable);
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        super.onDraw(canvas);
    }

    public int getDrawableWidth() {
        return drawableWidth;
    }

    public int getDrawableHeight() {
        return drawableHeight;
    }

    private void setDrawableSize(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        /// 这一步必须要做,否则不会显示.
        if (drawableWidth > 0 && drawableHeight > 0) {
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        } else {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
    }
}
