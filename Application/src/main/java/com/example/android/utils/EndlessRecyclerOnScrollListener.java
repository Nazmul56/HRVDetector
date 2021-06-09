package com.example.android.utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int current_page = 1;
    private boolean scrollFromEnd;

    private RecyclerView.LayoutManager  mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager  linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.scrollFromEnd = true;
    }

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager  linearLayoutManager, boolean scrollFromEnd) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.scrollFromEnd = scrollFromEnd;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(scrollFromEnd) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = ((LinearLayoutManager) mLinearLayoutManager).findFirstVisibleItemPosition();
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                // Do something
                current_page++;
                onLoadMore(current_page);
                loading = true;
            }
        }else {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = ((LinearLayoutManager) mLinearLayoutManager).findFirstVisibleItemPosition();
            if (loading) {
                if (firstVisibleItem > 0) {
                    loading = false;
                   // previousTotal = totalItemCount;
                }
            }
            if (!loading && firstVisibleItem == 0) {
                // End has been reached
                // Do something
                current_page++;
                onLoadMore(current_page);
                loading = true;
                //Lg.d(TAG, "First Visible Item: "+ firstVisibleItem);
            }
           // Lg.d(TAG, "First Visible Item: "+ firstVisibleItem);
           // Lg.d(TAG, "Total Item: "+ totalItemCount);
           // Lg.d(TAG, "Visible Item: "+ visibleItemCount);
        }

    }

    public abstract void onLoadMore(int current_page);
}