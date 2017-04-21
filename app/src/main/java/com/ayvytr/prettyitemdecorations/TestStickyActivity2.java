package com.ayvytr.prettyitemdecorations;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayvytr.easyandroid.tools.Convert;
import com.ayvytr.easyandroid.view.custom.CenterGravityTextView;
import com.ayvytr.prettyitemdecoration.PrettyItemDecoration;
import com.ayvytr.prettyitemdecoration.header.StickyHeaderAdapter;
import com.ayvytr.prettyitemdecoration.header.StickyHeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestStickyActivity2 extends BaseEasyActivity
{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private BasicAdapter basicAdapter;

    protected void initView(Bundle savedInstanceState)
    {
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        basicAdapter = new BasicAdapter();
        recyclerView.setAdapter(basicAdapter);
        recyclerView.addItemDecoration(new PrettyItemDecoration(PrettyItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new PrettyItemDecoration());
        recyclerView.addItemDecoration(new StickyHeaderItemDecoration(basicAdapter));
    }

    @Override
    protected int getContentLayoutRes()
    {
        return R.layout.activity_test_sticky2;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        basicAdapter = null;
    }

    public class BasicAdapter extends RecyclerView.Adapter<BasicAdapter.Vh>
            implements StickyHeaderAdapter<BasicAdapter.HeaderVh>
    {
        List<String> list = new ArrayList<>();

        public BasicAdapter()
        {
            char c = 'a';
            for(int i = 0; i < 26; i++)
            {
                list.add(Convert.toString((char) (c + i)));
            }
        }

        @Override
        public Vh onCreateViewHolder(ViewGroup parent, int viewType)
        {
            return new Vh(LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.item_sticky_test2, parent, false));
        }

        @Override
        public void onBindViewHolder(Vh holder, int position)
        {
            holder.bind(position);
        }

        @Override
        public int getItemCount()
        {
            return list.size();
        }

        @Override
        public int getId(int position)
        {
            if(position < 6)
            {
                return 0;
            }

            if(position < 13)
            {
                return 1;
            }

            if(position < 19)
            {
                return 2;
            }

            return 3;
        }

        @Override
        public HeaderVh onCreateHeaderViewHolder(RecyclerView parent)
        {
            return new HeaderVh(LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_sticky_test2_header, parent,
                                                      false));
        }

        @Override
        public void onBindHeaderViewHolder(HeaderVh holder, int position)
        {
            holder.bind(position);
        }

        public class Vh extends RecyclerView.ViewHolder
        {
            @BindView(R.id.tv)
            CenterGravityTextView tv;

            public Vh(View view)
            {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bind(int position)
            {
                tv.setText(list.get(position));
            }
        }

        public class HeaderVh extends RecyclerView.ViewHolder
        {
            @BindView(R.id.tv)
            CenterGravityTextView tv;

            public HeaderVh(View view)
            {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bind(int position)
            {
                tv.setText(list.get(position));
            }
        }
    }
}
