package com.ayvytr.prettyitemdecorations;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ayvytr.baseadapter.CommonAdapter;
import com.ayvytr.baseadapter.ViewHolder;
import com.ayvytr.easyandroid.view.activity.BaseActivity;
import com.ayvytr.prettyitemdecoration.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearItemDecorationActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.activity_linear_item_decoration;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            list.add(i + "");
        }
        mRv.setAdapter(new CommonAdapter<String>(getContext(), R.layout.item_test_item_decoration_v, list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv, s);
            }
        });
        mRv.addItemDecoration(new LinearItemDecoration(LinearItemDecoration.VERTICAL, Color.GRAY, 20,
                50, 100));
//        mRv.addItemDecoration(new LinearItemDecoration(LinearItemDecoration.HORIZONTAL, Color.GRAY, 2,
//                50, 100));
//        mRv.addItemDecoration(new PrettyItemDecoration(PrettyItemDecoration.VERTICAL, Color.GRAY));
//        mRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

}
