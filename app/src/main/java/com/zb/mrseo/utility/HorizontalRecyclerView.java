package com.zb.mrseo.utility;


import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalRecyclerView extends RecyclerView {

    public HorizontalRecyclerView(Context context) {
        super(context);
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();

        int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
        int firstVisibleView = linearLayoutManager.findFirstVisibleItemPosition();
        View firstView = linearLayoutManager.findViewByPosition(firstVisibleView);
        View lastView = linearLayoutManager.findViewByPosition(lastVisibleView);


        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        Log.v("width123:-",width+"");
//        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        int leftMargin = (width - lastView.getWidth()) / 2;
        int rightMargin = (width - firstView.getWidth()) / 2 + firstView.getWidth();
        int leftEdge = lastView.getLeft();
        int rightEdge = firstView.getRight();
        int scrollDistanceLeft = leftEdge - leftMargin;
        int scrollDistanceRight = rightMargin - rightEdge;

        if (velocityX > 0)
            smoothScrollBy(scrollDistanceLeft, 0);
        else
            smoothScrollBy(-scrollDistanceRight, 0);

        return true;
    }
}