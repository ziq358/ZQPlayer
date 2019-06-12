package com.isoftstone.geelyauto.app.view;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FloatingActionButtonUtil {

    public static void connectFabToRecycleView(FloatingActionButton floatingActionButton, RecyclerView rv_list){
        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(recyclerView.canScrollVertically(-1)){
                    floatingActionButton.setVisibility(View.VISIBLE);
                }else {
                    //滑动到顶部
                    floatingActionButton.setVisibility(View.GONE);
                }

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.setVisibility(View.GONE);
                rv_list.scrollToPosition(0);
            }
        });
    }
}
