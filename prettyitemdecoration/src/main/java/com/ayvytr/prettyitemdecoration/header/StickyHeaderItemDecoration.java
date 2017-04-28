package com.ayvytr.prettyitemdecoration.header;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * {@link RecyclerView } 的ItemDecoration, 通过 {@link android.support.v7.widget.RecyclerView.Adapter}
 * 实现 {@link StickyHeaderAdapter}, 可实现给 Item 添加头部和吸附效果.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 1.0.0
 */

public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration
{
    private StickyHeaderAdapter headerAdapter;
    private HeaderViewCache headerViewCache;
    private boolean isStick = true;

    private static final int NO_POSITION = RecyclerView.NO_POSITION;
    private Rect rect = new Rect();

    /**
     * 设置顶部吸附效果
     *
     * @param isStick {@code true} 吸附 {@code false} 不吸附
     */
    private void setStick(boolean isStick)
    {
        this.isStick = isStick;
    }

    public StickyHeaderItemDecoration(StickyHeaderAdapter headerAdapter)
    {
        this(headerAdapter, true);
    }

    public StickyHeaderItemDecoration(StickyHeaderAdapter headerAdapter, boolean isStick)
    {
        this.headerAdapter = headerAdapter;
        this.isStick = isStick;
        headerViewCache = new HeaderViewCache(headerAdapter);
        ((RecyclerView.Adapter) headerAdapter)
                .registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
                {
                    @Override
                    public void onChanged()
                    {
                        headerViewCache.invalidate();
                    }
                });
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        super.onDrawOver(c, parent, state);
        draw(c, parent);
    }

    @SuppressLint("NewApi")
    private void draw(Canvas c, RecyclerView parent)
    {
        int childCount = parent.getChildCount();

        if(parent.getClipToPadding())
        {
            c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(),
                    parent.getWidth() - parent.getPaddingRight(),
                    parent.getHeight() - parent.getPaddingBottom());
        }

        int top = parent.getPaddingTop();
        int preHeaderId;
        int headerId = NO_POSITION;
        int x = parent.getPaddingLeft();
        for(int i = 0; i < childCount; i++)
        {
            View itemView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(itemView);

            //只有各组第一个 并且 headerId!=-1 才绘制头部view
            preHeaderId = headerId;
            headerId = getHeaderId(position);
            if(headerId <= NO_POSITION || headerId == preHeaderId)
            {
                continue;
            }

            View header = getHeaderView(parent, position);

            int heightWithPadding = header.getHeight() + top;
            int y = Math.max(heightWithPadding, itemView.getTop());
            if(isStick)
            {
                int nextPosition = getNextHeadPosition(i, headerId, childCount, parent);
                if(nextPosition != NO_POSITION)
                {
                    View nextView = parent.getChildAt(nextPosition);
                    //获得真实位置后再进行判断
                    parent.getDecoratedBoundsWithMargins(nextView, rect);
                    //判断下一个头部view是否到了与上一个头部view接触的临界值
                    //如果满足条件则把上一个头部view推上去
                    if(rect.top <= heightWithPadding)
                    {
                        //这里使用nextView.getTop 直接减掉 header.getHeight()，如果减去heightWithPadding,
                        //会出现移动时的间距
                        y = nextView.getTop() - header.getHeight();
                    }
                }
            }

            //这个时候，y是header底部位置，需要减去它的高度，修正定位
            y -= header.getHeight();
            c.translate(x, y);
            header.draw(c);
            //修正回来
            c.translate(-x, -y);
        }
    }

    /**
     * 获取下一个节点，如果没有则返回 {@link #NO_POSITION}
     *
     * @param count Item数量
     * @return Position
     */
    private int getNextHeadPosition(int id, int groupId, int count, RecyclerView parent)
    {
        for(int i = id; i < count; i++)
        {
            if(getHeaderId(parent.getChildAdapterPosition(parent.getChildAt(i))) != groupId)
            {
                return i;
            }
        }
        return NO_POSITION;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        setItemOffsets(outRect, view, parent);
    }

    private void setItemOffsets(Rect outRect, View view, RecyclerView parent)
    {
        int position = parent.getChildAdapterPosition(view);
        if(hasItemOffset(position))
        {
            int height = getHeaderView(parent, position).getHeight();
            outRect.set(0, height, 0, 0);
        }
    }

    private boolean hasItemOffset(int position)
    {
        int id = getHeaderId(position);
        if(position == 0)
        {
            return id > NO_POSITION;
        }

        int preId = getHeaderId(position - 1);
        return id > NO_POSITION && id != preId;
    }

    private int getHeaderId(int position)
    {
        try
        {
            return headerAdapter.getId(position);
        } catch(Exception e)
        {
            return NO_POSITION;
        }
    }

    /**
     * 获取HeaderView
     *
     * @param parent   RecyclerView
     * @param position RecyclerView当前Item的position，需要用 {@link RecyclerView#getChildAdapterPosition(View)}
     *                 获得真实position，不然显示的数据有误
     * @return Header view
     */
    public View getHeaderView(RecyclerView parent, int position)
    {
        return headerViewCache.getHeader(parent, position);
    }

    /**
     * 重绘Header
     */
    public void invalidate()
    {
        headerViewCache.invalidate();
    }
}
