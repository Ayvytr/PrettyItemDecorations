package com.ayvytr.prettyitemdecoration;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 专用于 {@link android.support.v7.widget.LinearLayoutManager} 使用的 ItemDecoration, 其他LayoutManager也可以使用，没有
 * 第一个Item顶部的分割线和最后一个Item底部的分割线(分割线数量为ItemCount - 1).但是，比如GridLayoutManager，并不适用它的所有
 * 场景，比如 paddingStart和paddingEnd不为0的时候，Item不满一屏的时候，会有多出的分割线,显示效果不好.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 1.0.0
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_WIDTH = 1;
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = OrientationHelper.VERTICAL;

    private Paint mPaint;
    //Divider宽度，像素
    private int mDividerWidth;
    //Divider方向
    private int mOrientation;
    @ColorInt
    private int mColor;

    private int mPaddingStart;
    private int mPaddingEnd;

    @ColorInt
    public static final int DEFAULT_COLOR = Color.GRAY;

    private final Rect mBounds = new Rect();

    public LinearItemDecoration() {
        this(OrientationHelper.VERTICAL);
    }

    public LinearItemDecoration(int orientation) {
        this(orientation, DEFAULT_COLOR);
    }

    public LinearItemDecoration(int orientation, @ColorInt int color) {
        this(orientation, color, DEFAULT_WIDTH);
    }

    public LinearItemDecoration(int orientation, @ColorInt int color, int dividerWidth) {
        this(orientation, color, dividerWidth, 0);
    }

    public LinearItemDecoration(int orientation, @ColorInt int color, int dividerWidth, int paddingStart) {
        this(orientation, color, dividerWidth, paddingStart, 0);
    }

    public LinearItemDecoration(int orientation, @ColorInt int color, int dividerWidth, int paddingStart,
                                int paddingEnd) {
        mOrientation = orientation;
        mColor = color;
        mDividerWidth = dividerWidth;
        mPaddingStart = paddingStart;
        mPaddingEnd = paddingEnd;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(this.mDividerWidth);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(parent.getLayoutManager() == null) {
            return;
        }

        if(mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @SuppressLint("NewApi")
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if(parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        left += mPaddingStart;
        right -= mPaddingEnd;

        final int childCount = parent.getChildCount();
        int i;
        for(i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if(parent.getChildAdapterPosition(child) != parent.getAdapter().getItemCount() - 1) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
                final int top = bottom - mDividerWidth;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }

        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top;
        int bottom;
        if(parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        top += mPaddingStart;
        bottom -= mPaddingEnd;

        final int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if(parent.getChildAdapterPosition(child) != parent.getAdapter().getItemCount() - 1) {
                parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
                final int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
                final int left = right - mDividerWidth;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if(itemPosition != parent.getAdapter().getItemCount() - 1) {
            if(mOrientation == VERTICAL) {
                outRect.set(0, 0, 0, mDividerWidth);
            } else {
                outRect.set(0, 0, mDividerWidth, 0);
            }
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }
}
