package com.test.commentdialog.util;

import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author ganhuanhui
 * @date 2020/4/28 0028
 * @desc
 */
public class RecyclerViewUtil {

    private RecyclerView recyclerView;

    public RecyclerViewUtil() {
    }

    public void initScrollListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (mScrollListener != null) {
            this.recyclerView.addOnScrollListener(mScrollListener);
        }
    }

    private int currentState = -1;
    private Handler cancelScrollHandler = new Handler();
    private Runnable cancelScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (recyclerView != null) {
                currentState = -1;
                // When the user stops sliding, actively touch the Recyclerview, so as to be able to consume the stop scrolling event immediately, and prevent the click event from being triggered when the item is clicked twice.
                recyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0f, 0f, 0));
            }
        }
    };

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            currentState = newState;
            Log.e("bottomSheetAdapter", "newState:: "+ newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //If in continuous scrolling, cancel the callback
            if (cancelScrollHandler == null || cancelScrollRunnable == null) return;
            cancelScrollHandler.removeCallbacks(cancelScrollRunnable);
            //The current animation state of RecyclerView when scrolling to a certain position, this state is when the code is called or when inertial scrolling
            if (currentState == RecyclerView.SCROLL_STATE_SETTLING) {
                cancelScrollHandler.postDelayed(cancelScrollRunnable, 20);
            }
        }
    };

    public void destroy() {
        if (cancelScrollHandler != null && cancelScrollRunnable != null) {
            cancelScrollHandler.removeCallbacks(cancelScrollRunnable);
            cancelScrollHandler = null;
        }
        if (recyclerView != null && mScrollListener != null) {
            recyclerView.removeOnScrollListener(mScrollListener);
            recyclerView = null;
            mScrollListener = null;
        }
    }

}
