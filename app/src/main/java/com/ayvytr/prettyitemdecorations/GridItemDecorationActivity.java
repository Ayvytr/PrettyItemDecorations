package com.ayvytr.prettyitemdecorations;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ayvytr.baseadapter.CommonAdapter;
import com.ayvytr.baseadapter.ViewHolder;
import com.ayvytr.easyandroid.view.activity.BaseActivity;
import com.ayvytr.prettyitemdecoration.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridItemDecorationActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    Random random = new Random();

    @Override
    protected int getContentLayoutRes() {
        return R.layout.activity_linear_item_decoration;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch((position + 1) % 3) {
                    case 0:
                        return 3;
                    case 1:
                        return 2;
                    case 2:
                        return 1;
                    default:
                        return 1;
                }

            }
        });
        mRv.setLayoutManager(gridLayoutManager);
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            list.add(i + "");
        }
        mRv.setAdapter(new CommonAdapter<String>(getContext(), R.layout.item_test_item_decoration_g, list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv, s);
            }
        });
//        mRv.addItemDecoration(new LinearItemDecoration(LinearItemDecoration.VERTICAL, Color.GRAY, 2,
//                50, 100));
        mRv.addItemDecoration(new GridItemDecoration(Color.GRAY, 30));
    }

}
