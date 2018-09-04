package com.cmeplaza.basemodule.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmeplaza.basemodule.R;
import com.cmeplaza.basemodule.event.UIEvent;
import com.cmeplaza.basemodule.utils.CommonDialogUtils;
import com.cmeplaza.basemodule.widget.MyLoadMoreWrapper;
import com.cmeplaza.basemodule.widget.stateview.StateView;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：Android_AJ on 2017/4/6.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 */
public abstract class BaseFragment extends RxFragment implements
        MyLoadMoreWrapper.OnLoadMoreListener {
    public AlertDialog dialog;
    protected View rootView;
    // 下拉刷新
    protected SwipeRefreshLayout swipe_refresh;
    // 加载更多
    protected MyLoadMoreWrapper loadMoreWrapper;
    protected boolean canLoadMore = false;
    protected StateView stateView;
    private Unbinder unbinder;
    private boolean isRefresh = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        initView();
        initBack();
        initRefreshLayout();
        return rootView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    private void initBack() {
        TextView tv_back = ButterKnife.findById(rootView, R.id.tv_back);
        if (tv_back != null) {
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    protected void initRefreshLayout() {
        swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        if (swipe_refresh == null) {
            return;
        }
        swipe_refresh.setColorSchemeResources(R.color.pink_dark, R.color.pink_light,
                R.color.colorAccentDark);
        swipe_refresh.setProgressViewOffset(false, -100, getResources().getDisplayMetrics().heightPixels / 10);
        swipe_refresh.setRefreshing(false);
        swipe_refresh.setEnabled(false);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onLayoutRefresh();
            }
        });
    }

    /**
     * 下拉刷新后调用的方法
     */
    protected void onLayoutRefresh() {

    }

    protected void disableSwipe_refresh() {
        if (swipe_refresh != null) {
            swipe_refresh.setEnabled(false);
        }
    }

    protected void setCanRefresh() {
        if (swipe_refresh != null) {
            swipe_refresh.setEnabled(true);
        }
    }

    /**
     * 设置支持StateView
     *
     * @param view 要包裹的View
     */
    protected void supportStateView(View view) {
        stateView = StateView.inject(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    protected void initData() {
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    protected void initLoadMoreWrapper(RecyclerView.Adapter commonAdapter) {
        loadMoreWrapper = new MyLoadMoreWrapper(commonAdapter);
        loadMoreWrapper.setLoadMoreView(R.layout.layout_load_more_recycler);
        loadMoreWrapper.setOnLoadMoreListener(this);
    }

    protected void hasMore(boolean flag) {
        if (canLoadMore == flag) {
            return;
        }
        loadMoreWrapper.setLoadOver(flag);
        canLoadMore = flag;
        loadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreRequested() {
        if (!canLoadMore) {
            return;
        }
    }

    protected void commonStartActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        commonStartActivity(intent);
    }

    protected void commonStartActivity(Intent intent) {
        startActivity(intent);
    }

    protected void showBack() {
        TextView tv_back = ButterKnife.findById(rootView, R.id.tv_back);
        if (tv_back != null) {
            visible(tv_back);
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    protected void hideBack() {

    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void setTitleCenter(int res) {
        setTitleCenter(getActivity().getResources().getString(res));
    }

    protected void setTitleCenter(String title) {
        if (rootView == null) {
            return;
        }
        TextView tv_title_center = (TextView) rootView.findViewById(R.id.tv_title_center);
        if (tv_title_center != null) {
            tv_title_center.setText(title);
        }
    }

    protected boolean isEmpty(String content) {
        return TextUtils.isEmpty(content);
    }

    protected void inVisible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * 默认加载框
     */
    public void showProgress() {
        showProgress(getString(R.string.loading));
    }

    /**
     * 显示加载框
     *
     * @param message
     */
    public void showProgress(String message) {
        dialog = CommonDialogUtils.getProgressDialog(getActivity(), message);
        dialog.show();
    }

    /**
     * 默认加载框
     */
    protected void showProgress(int id) {
        String message = getString(id);
        showProgress(message);
    }

    /**
     * 隐藏加载框
     */
    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (swipe_refresh != null) {
            swipe_refresh.setRefreshing(false);
        }
    }

    protected void hideRefresh() {
        if (swipe_refresh != null) {
            swipe_refresh.setRefreshing(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIEvent(UIEvent uiEvent) {

    }

    protected String getText(TextView textView) {
        if (textView == null) {
            return "";
        }
        return textView.getText().toString().trim();
    }
}
