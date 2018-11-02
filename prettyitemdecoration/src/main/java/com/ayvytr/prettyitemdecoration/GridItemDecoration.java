package com.ayvytr.prettyitemdecoration;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 专用于 {@link android.support.v7.widget.LinearLayoutManager} 使用的 ItemDecoration, 其他LayoutManager也可以使用，但是，
 * 比如GridLayoutManager，并不适用它的所有场景，比如 paddingStart和paddingEnd不为0的时候，Item不满一屏的时候，会有多出的分割线,
 * 显示效果不好.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 2.0.0
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_WIDTH = 1;
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = OrientationHelper.VERTICAL;

    private Paint mPaint;
    //Divider宽度，像素
    private int mDividerWidth;
    @ColorInt
    private int mColor;
    private int mOrientation;

    private int mPaddingStart;
    private int mPaddingEnd;

    @ColorInt
    public static final int DEFAULT_COLOR = Color.GRAY;

    private final Rect mBounds = new Rect();

    public GridItemDecoration() {
        this(DEFAULT_COLOR);
    }

    public GridItemDecoration(@ColorInt int color) {
        this(color, DEFAULT_WIDTH);
    }

    public GridItemDecoration(@ColorInt int color, int dividerWidth) {
        this(color, dividerWidth, 0);
    }

    public GridItemDecoration(@ColorInt int color, int dividerWidth, int padding) {
        this(color, dividerWidth, padding, VERTICAL);
    }

    public GridItemDecoration(@ColorInt int color, int dividerWidth, int padding, int orientation) {
        mColor = color;
        mDividerWidth = dividerWidth;
        mPaddingStart = padding;
        mOrientation = orientation;
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

        drawVertical(c, parent);
        drawHorizontal(c, parent);
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
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            final int top = bottom - mDividerWidth;
            canvas.drawRect(left, top, right, bottom, mPaint);
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
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
            final int left = right - mDividerWidth;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int right = mDividerWidth;
        int bottom = mDividerWidth;
        if (isLastSpan(itemPosition, parent)) {
            right = 0;
        }

        if (isLastRow(itemPosition, parent)) {
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    public boolean isLastRow(int itemPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            int itemCount = parent.getAdapter().getItemCount();
            if ((itemCount - itemPosition - 1) < spanCount) {
                return true;
            }
        }
        return false;
    }

    public boolean isLastSpan(int itemPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            if ((itemPosition + 1) % spanCount == 0) {
                return true;
            }
        }
        return false;
    }

}
