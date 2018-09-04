package com.cmeplaza.basemodule.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmeplaza.basemodule.R;
import com.cmeplaza.basemodule.utils.LogUtils;
import com.cmeplaza.basemodule.utils.SizeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by klx on 2018/8/18.
 * ViewPager指示器
 */

public class TabsView extends LinearLayout {
    private static final int TAB_MODE_FIXED = 101;
    private static final int TAB_MODE_SCROLLABLE = 102;

    private int tabMode;  // tab模式，fixed-固定的 101   scrollable-可滑动的 102
    private float minWidth;

    private int viewWidth;

    private int mSelectedColor = Color.parseColor("#333333");// 选中的字体颜色
    private int mNotSelectedColor = mSelectedColor;// 未选中的字体颜色

    private LinearLayout mRootView;
    private LinearLayout mTabsContainer;// 放置tab的容器

    private String[] titles;
    private ViewPager mViewPager;

    private Map<Integer, Integer> unReadMap = new HashMap<>();
    private OnTabsItemClickListener onTabsItemClickListener;

    public TabsView(Context context) {
        this(context, null);
    }

    public TabsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabsView);
        tabMode = a.getInt(R.styleable.TabsView_tabMode, TAB_MODE_FIXED);
        minWidth = a.getDimension(R.styleable.TabsView_minWidth, 60);
        a.recycle();
        mRootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_tabs_view, this);
        // 初始化容器
        mTabsContainer = (LinearLayout) mRootView.findViewById(R.id.ll_container);
        this.post(new Runnable() {
            @Override
            public void run() {
                viewWidth = getWidth();
                initTabChildView();
            }
        });
    }

    private void initTabChildView() {
        mTabsContainer.removeAllViews();
        if (titles != null && titles.length > 0) {
            for (int i = 0; i < titles.length; i++) {
                View textLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_tabs_textview, null);
                if (TAB_MODE_FIXED == tabMode) {
                    int textLayoutWidth = viewWidth / titles.length;
                    textLayout.setLayoutParams(new LayoutParams(textLayoutWidth, LayoutParams.MATCH_PARENT));
                } else if (TAB_MODE_SCROLLABLE == tabMode) {
                    int textLayoutWidth = Float.valueOf(minWidth).intValue();
                    textLayout.setLayoutParams(new LayoutParams(SizeUtils.dp2px(getContext(), textLayoutWidth), LayoutParams.MATCH_PARENT));
                }
                final TextView textView = (TextView) textLayout.findViewById(R.id.tv_text);
                textView.setText(titles[i]);
                textLayout.setTag(i);
                final int fixI = i;
                textLayout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        setCurrentTab(position);
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(fixI, false);
                        } else {
                            if (onTabsItemClickListener != null) {
                                onTabsItemClickListener.onTabsItemClick(textView, fixI);
                            }
                        }
                    }
                });
                mTabsContainer.addView(textLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setCurrentTab(0);
                    }
                }, 200);
                if (unReadMap.size() > 0) {
                    for (Integer integer : unReadMap.keySet()) {
                        setUnReadCount(integer, unReadMap.get(integer));
                    }
                }
            }
        }
    }

    /**
     * 设置当前的tab
     *
     * @param position
     */
    public void setCurrentTab(int position) {
        int childCount = mTabsContainer.getChildCount();
        if (position < 0 || position >= childCount) {
            return;
        }
        // 设置每个tab的状态
        for (int i = 0; i < childCount; i++) {
            RelativeLayout childView = (RelativeLayout) mTabsContainer.getChildAt(i);
            View view_indicator = childView.findViewById(R.id.view_indicator);
            TextView childViewText = (TextView) childView.findViewById(R.id.tv_text);
            if (i == position) {
                childViewText.setTextColor(mSelectedColor);
                view_indicator.setVisibility(View.VISIBLE);
            } else {
                childViewText.setTextColor(mNotSelectedColor);
                view_indicator.setVisibility(View.GONE);
            }
        }
    }

    public void setUnReadCount(int position, int unReadCount) {
        unReadMap.put(position, unReadCount);
        int childCount = mTabsContainer.getChildCount();
        if (position < 0 || position >= childCount) {
            return;
        }
        View childView = mTabsContainer.getChildAt(position);
        TextView tv_un_read = (TextView) childView.findViewById(R.id.tv_un_read);
        if (tv_un_read != null) {
            if (unReadCount > 0) {
                tv_un_read.setVisibility(VISIBLE);
                tv_un_read.setText(String.valueOf(unReadCount));
            } else {
                tv_un_read.setText("");
                tv_un_read.setVisibility(GONE);
            }
        }
    }

    /**
     * 设置选项卡
     *
     * @param titles
     */
    public void setTabs(String... titles) {
        this.titles = titles;
    }

    public void setUpWithViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setCurrentTab(position);
                }
            });
        }
    }

    public void setOnTabsItemClickListener(OnTabsItemClickListener onTabsItemClickListener) {
        this.onTabsItemClickListener = onTabsItemClickListener;
    }

    public interface OnTabsItemClickListener {
        void onTabsItemClick(View view, int position);
    }
}
