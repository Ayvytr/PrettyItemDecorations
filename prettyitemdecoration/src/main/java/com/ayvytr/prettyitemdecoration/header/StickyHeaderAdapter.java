package com.ayvytr.prettyitemdecoration.header;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 配合 {@link StickyHeaderItemDecoration}, 通过实现 {@link android.support.v7.widget.RecyclerView.Adapter},
 * 可实现 Item 添加头部吸附效果.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 1.0.0
 */
public interface StickyHeaderAdapter<VH extends RecyclerView.ViewHolder> {
    /**
     * 根据 {@link RecyclerView} 当前Item的位置，获取Header id.一般使用英文或者中文拼音首字母作为id返回.
     *
     * @param position Item真实位置，需要用 {@link RecyclerView#getChildAdapterPosition(View)} 获取
     * @return Header id
     */
    int getId(int position);

    /**
     * 根据id获取真实的Position.
     * <p>
     * 请看一下建议，
     * <p>
     * 使用SparseArray，在{@link #getId(int)} 保存真实Position，然后这个方法直接返回保存的Position.
     *
     * @param id id
     * @return 真实position
     */
    int getPosition(int id);

    /**
     * 返回当前 Header 的ViewHolder
     *
     * @param parent {@link RecyclerView}
     * @return {@link android.support.v7.widget.RecyclerView.ViewHolder}
     */
    VH onCreateHeaderViewHolder(RecyclerView parent);

    /**
     * 根据当前位置的 {@link android.support.v7.widget.RecyclerView.ViewHolder} 和 position，创建Header.
     *
     * @param holder   {@link android.support.v7.widget.RecyclerView.ViewHolder}
     * @param position 当前Item的位置
     */
    void onBindHeaderViewHolder(VH holder, int position);
}
