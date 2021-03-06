package com.cmeplaza.basemodule.widget.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.cmeplaza.basemodule.CoreLib;
import com.cmeplaza.basemodule.utils.SizeUtils;


/**
 * Created by David on 17.06.2015.
 * RecyclerView分割线
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {

    private final Paint mPaint;
    private int mHeightDp;

    private int linePadding = SizeUtils.dp2px(CoreLib.getContext(), 10);

    public DividerDecoration(Context context) {
        this(context, Color.argb((int) (255 * 0.2), 0, 0, 0), 0.5f);
    }

    public DividerDecoration(Context context, int color, float heightDp) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        mHeightDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp, context.getResources().getDisplayMetrics());
    }

    /**
     * 分割线padding
     *
     * @param padding 单位是dp
     */
    public void setLinePadding(int padding) {
        this.linePadding = SizeUtils.dp2px(CoreLib.getContext(), padding);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (hasDividerOnBottom(view, parent, state)) {
            outRect.set(0, 0, 0, mHeightDp);
        } else {
            outRect.setEmpty();
        }
    }

    private boolean hasDividerOnBottom(View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        return position < state.getItemCount() - 1 && parent.getAdapter().getItemViewType(position) != 1;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (hasDividerOnBottom(view, parent, state)) {
                c.drawRect(view.getLeft() + linePadding, view.getBottom(), view.getRight() - linePadding, view.getBottom() + mHeightDp, mPaint);
            }
        }
    }
}
