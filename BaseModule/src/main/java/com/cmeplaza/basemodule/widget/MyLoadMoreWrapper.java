package com.cmeplaza.basemodule.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.utils.WrapperUtils;

/**
 * Created by Administrator on 2017/5/8.
 * 加载更多的Adapter的Wrapper
 */

public class MyLoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;

    // 这个是定义的是否加载完成的boolean变量
    private boolean mIsLoadOver = true;

    // 给一个set方法，用于在外部控制是否隐藏‘加载更多’
    public void setLoadOver(boolean loadOver) {
        mIsLoadOver = loadOver;
    }

    private boolean lastItemVisible = true;

    public void setLastItemVisible(boolean flag) {
        lastItemVisible = flag;
    }

    public MyLoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore() {
        return (mLoadMoreView != null || mLoadMoreLayoutId != 0);
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 0) {
            position = 0;
        }
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
            }

            if (lastItemVisible) {
                holder.getConvertView().setVisibility(View.VISIBLE);
            } else {
                holder.getConvertView().setVisibility(View.GONE);
            }

            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        // 这里将mIsLoadOver 和 hasLoadMore()一起判断
//        LogUtils.i("newsFragment getItemCount" + mInnerAdapter.getItemCount() + ((hasLoadMore() && mIsLoadOver) ? 1 : 0));
        return mInnerAdapter.getItemCount() + ((hasLoadMore() && mIsLoadOver) ? 1 : 0);
//        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }


    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public MyLoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public MyLoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public MyLoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }
}
