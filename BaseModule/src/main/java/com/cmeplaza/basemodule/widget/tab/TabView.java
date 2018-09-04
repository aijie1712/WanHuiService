package com.cmeplaza.basemodule.widget.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmeplaza.basemodule.R;
import com.cmeplaza.basemodule.utils.ScreenUtils;
import com.cmeplaza.basemodule.utils.SizeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：yaochangliang on 2016/8/13 17:36
 * 邮箱：yaochangliang159@sina.com
 */
public class TabView extends RelativeLayout {
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int LMP = LinearLayout.LayoutParams.MATCH_PARENT;
    /**
     * the TextView selected color,default is orange
     */
    private int mTextViewSelColor;
    /**
     * the TextView unselected color,default is black
     */
    private int mTextViewUnSelColor;
    /**
     * the TabView background color,default is white
     */
    private int mTabViewBackgroundColor;
    /**
     * the TabView`s height,default is 52dip
     */
    private int mTabViewHeight;
    /**
     * Spacing between the icon and textview,default is 2dip
     */
    private int mImageViewTextViewMargin;
    /**
     * the textview`s size,default is 14sp
     */
    private int mTextViewSize;
    /**
     * the imageview`s width,default width  is 30dip
     */
    private int mImageViewWidth;
    /**
     * the imageview`s height,default height is 30dip
     */
    private int mImageViewHeight;
    /**
     * the child inside tabview
     */
    private List<TabViewChild> mTabViewChildList;
    /**
     * the tabview`s location,default is bottom
     */
    private int mTabViewGravity = Gravity.BOTTOM;
    /**
     * which tabchild to show,default is 0
     */
    private int mTabViewDefaultPosition = 0;
    private LinearLayout tabview;
    private List<Integer> unselectedIconList;
    private FrameLayout mFragmentContainer;
    private int index = 0;
    private int currentTabIndex;
    private int unReadCount;

    private int leftMargin = 15;

    private ViewPager mViewPager;

    private Map<Integer, Integer> unReadCountMap = new HashMap<>();

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttrs(context);
        initCustomAttrs(context, attrs);
        initView(context);
    }


    private void initDefaultAttrs(Context context) {
        mTextViewSelColor = Color.rgb(252, 88, 17);
        mTextViewUnSelColor = Color.rgb(129, 130, 149);
        mTabViewBackgroundColor = Color.rgb(255, 255, 255);
        mTabViewHeight = TabViewUtil.dp2px(context, 52);
        mImageViewTextViewMargin = TabViewUtil.dp2px(context, 2);
        // 设置点击事件
        mTextViewSize = TabViewUtil.sp2px(context, 14);
        mImageViewWidth = TabViewUtil.dp2px(context, 30);
        mImageViewHeight = TabViewUtil.dp2px(context, 30);
    }


    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.TabView_tab_textViewSelColor) {
            mTextViewSelColor = typedArray.getColor(attr, mTextViewSelColor);
        } else if (attr == R.styleable.TabView_tab_textViewUnSelColor) {
            mTextViewUnSelColor = typedArray.getColor(attr, mTextViewUnSelColor);
        } else if (attr == R.styleable.TabView_tab_tabViewBackgroundColor) {
            mTabViewBackgroundColor = typedArray.getColor(attr, mTabViewBackgroundColor);
        } else if (attr == R.styleable.TabView_tab_tabViewHeight) {
            mTabViewHeight = typedArray.getDimensionPixelSize(attr, mTabViewHeight);
        } else if (attr == R.styleable.TabView_imageViewTextViewMargin) {
            mImageViewTextViewMargin = typedArray.getDimensionPixelSize(attr, mImageViewTextViewMargin);
        } else if (attr == R.styleable.TabView_tab_textViewSize) {
            mTextViewSize = typedArray.getDimensionPixelSize(attr, mTextViewSize);
        } else if (attr == R.styleable.TabView_tab_imageViewWidth) {
            mImageViewWidth = typedArray.getDimensionPixelSize(attr, mImageViewWidth);
        } else if (attr == R.styleable.TabView_tab_imageViewHeight) {
            mImageViewHeight = typedArray.getDimensionPixelSize(attr, mImageViewHeight);
        } else if (attr == R.styleable.TabView_tab_tabViewGravity) {
            mTabViewGravity = typedArray.getInt(attr, mTabViewGravity);
        } else if (attr == R.styleable.TabView_tab_tabViewDefaultPosition) {
            mTabViewDefaultPosition = typedArray.getInteger(attr, mTabViewDefaultPosition);
        }
    }

    private void initView(Context context) {
        tabview = new LinearLayout(context);
        tabview.setId(R.id.tabview_id);


        mFragmentContainer = new FrameLayout(context);
        mFragmentContainer.setId(R.id.tabview_fragment_container);
        LayoutParams fragmentContainerParams = new LayoutParams(RMP, RMP);
        LayoutParams tabviewParams = null;
        if (mTabViewGravity == Gravity.BOTTOM) {
            tabviewParams = new LayoutParams(RMP, mTabViewHeight);
            tabview.setOrientation(LinearLayout.HORIZONTAL);
            tabviewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            fragmentContainerParams.addRule(RelativeLayout.ABOVE, R.id.tabview_id);
        } else if (mTabViewGravity == Gravity.LEFT) {
            tabviewParams = new LayoutParams(mTabViewHeight, RMP);
            tabview.setOrientation(LinearLayout.VERTICAL);
            tabviewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            fragmentContainerParams.addRule(RelativeLayout.RIGHT_OF, R.id.tabview_id);
        } else if (mTabViewGravity == Gravity.TOP) {
            tabviewParams = new LayoutParams(RMP, mTabViewHeight);
            tabview.setOrientation(LinearLayout.HORIZONTAL);
            tabviewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            fragmentContainerParams.addRule(RelativeLayout.BELOW, R.id.tabview_id);
        } else if (mTabViewGravity == Gravity.RIGHT) {
            tabviewParams = new LayoutParams(mTabViewHeight, RMP);
            tabview.setOrientation(LinearLayout.VERTICAL);
            tabviewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            fragmentContainerParams.addRule(RelativeLayout.LEFT_OF, R.id.tabview_id);
        }
        tabview.setLayoutParams(tabviewParams);
        tabview.setBackgroundColor(mTabViewBackgroundColor);

        mFragmentContainer.setLayoutParams(fragmentContainerParams);
        this.addView(tabview);
        this.addView(mFragmentContainer);

    }

    public void setUpWithViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTabViewDefaultPosition(position);
                initTabChildView();
            }
        });
    }


    private void initTabChildView() {
        tabview.removeAllViews();
        unselectedIconList = new ArrayList<>();
        for (int i = 0; i < mTabViewChildList.size(); i++) {
            final TabViewChild t = mTabViewChildList.get(i);
            FrameLayout child = new FrameLayout(getContext());

            LinearLayout tabChild = new LinearLayout(getContext());
            LinearLayout.LayoutParams tabChildParams = null;
            if (mTabViewGravity == Gravity.BOTTOM) {
                tabChildParams = new LinearLayout.LayoutParams(0, LMP, 1.0f);
                tabChildParams.gravity = Gravity.CENTER;
            } else if (mTabViewGravity == Gravity.LEFT) {
                tabChildParams = new LinearLayout.LayoutParams(LMP, 0, 1.0f);
                tabChildParams.gravity = Gravity.CENTER_VERTICAL;
            } else if (mTabViewGravity == Gravity.TOP) {
                tabChildParams = new LinearLayout.LayoutParams(0, LMP, 1.0f);
                tabChildParams.gravity = Gravity.CENTER_HORIZONTAL;
            } else if (mTabViewGravity == Gravity.RIGHT) {
                tabChildParams = new LinearLayout.LayoutParams(LMP, 0, 1.0f);
                tabChildParams.gravity = Gravity.CENTER_VERTICAL;
            }
            child.setLayoutParams(tabChildParams);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LMP, LMP);
            tabChild.setGravity(Gravity.CENTER);
            tabChild.setOrientation(LinearLayout.VERTICAL);
            tabChild.setLayoutParams(layoutParams);

            // 图标
            final ImageView imageview = new ImageView(getContext());
            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(
                    mImageViewWidth, mImageViewHeight
            );
            ivParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;

            imageview.setLayoutParams(ivParams);
            imageview.setImageResource(t.getImageViewUnSelIcon());
            unselectedIconList.add(t.getImageViewUnSelIcon());
            tabChild.addView(imageview);

            // 图标下的文字
            final TextView textview = new TextView(getContext());
            textview.setText(t.getTextViewText());
            textview.setTextColor(mTextViewUnSelColor);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextViewSize);

            LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textviewParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            textviewParams.topMargin = mImageViewTextViewMargin;
            textview.setLayoutParams(textviewParams);
            tabChild.addView(textview);
            child.addView(tabChild);

            // 未读显示的红点
            final TextView msgTextView = new TextView(getContext());
            msgTextView.setTextColor(Color.parseColor("#FFFFFF"));
            msgTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            msgTextView.setBackgroundResource(R.drawable.bg_red_text);
            msgTextView.setMaxLines(1);
            msgTextView.setEllipsize(TextUtils.TruncateAt.END);
            FrameLayout.LayoutParams msgTextLayoutParams = new FrameLayout.LayoutParams(SizeUtils.dp2px(getContext(), 18),
                    SizeUtils.dp2px(getContext(), 18));
            msgTextLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            msgTextView.setGravity(Gravity.CENTER);
            msgTextLayoutParams.leftMargin = SizeUtils.dp2px(getContext(), leftMargin);
            msgTextLayoutParams.topMargin = 10;
            msgTextView.setLayoutParams(msgTextLayoutParams);
            msgTextView.setVisibility(GONE);
            child.addView(msgTextView);
            tabview.addView(child);

            final int currentPosition = i;
            if (mTabViewDefaultPosition >= mTabViewChildList.size()) {
                if (i == 0) {
                    imageview.setImageResource(t.getImageViewSelIcon());
                    textview.setText(t.getTextViewText());
                    textview.setTextColor(mTextViewSelColor);
                }
            } else {
                if (mTabViewDefaultPosition == i) {
                    imageview.setImageResource(t.getImageViewSelIcon());
                    textview.setText(t.getTextViewText());
                    textview.setTextColor(mTextViewSelColor);
                }
            }
            tabChild.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ScreenUtils.hideSoftInput(view);
                    reSetAll();
                    imageview.setImageResource(t.getImageViewSelIcon());
                    textview.setText(t.getTextViewText());
                    textview.setTextColor(mTextViewSelColor);
                    index = currentPosition;
                    showOrHide();
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(currentPosition, false);
                    }
                    if (listener != null) {
                        listener.onTabChildClick(currentPosition, imageview, textview);
                    }
                    if (!unReadCountMap.isEmpty()) {
                        Set<Integer> keySet = unReadCountMap.keySet();
                        Object[] arrays = keySet.toArray();
                        for (Object array : arrays) {
                            int position = (Integer) array;
                            if (unReadCountMap.get(array) > 0) {
                                setUnReadCount(position, unReadCountMap.get(array), true);
                            } else if (unReadCountMap.get(array) == -1) {
                                setUnReadCount(position, 1, false);
                            } else {
                                setUnReadCount(position, 0, true);
                            }
                        }
                    }
                }
            });
//            child.setOnTouchListener(new ButtonOnTouchListener(i));
        }
    }

    public void setTabViewChild(List<TabViewChild> tabViewChildList) {
        this.mTabViewChildList = tabViewChildList;
        if (mTabViewDefaultPosition >= tabViewChildList.size()) {
            index = 0;
            currentTabIndex = 0;
            mTabViewDefaultPosition = 0;
        }
        initTabChildView();

    }

    public void setTabViewDefaultPosition(int position) {
        this.mTabViewDefaultPosition = position;
        this.index = position;
        this.currentTabIndex = position;
    }

    private void reSetAll() {
        for (int i = 0; i < tabview.getChildCount(); i++) {
            FrameLayout tabChild = (FrameLayout) tabview.getChildAt(i);
            for (int j = 0; j < tabChild.getChildCount(); j++) {
                LinearLayout linChild = (LinearLayout) tabChild.getChildAt(0);
                ImageView iv = (ImageView) linChild.getChildAt(0);
                TextView tv = (TextView) linChild.getChildAt(1);
                iv.setImageResource(unselectedIconList.get(i));
                tv.setTextColor(mTextViewUnSelColor);
            }
        }
    }

    private OnTabChildClickListener listener = null;

    public void setOnTabChildClickListener(OnTabChildClickListener l) {
        listener = l;
    }

    public interface OnTabChildClickListener {
        void onTabChildClick(int position, ImageView imageView, TextView textView);
    }


    private void showOrHide() {
        currentTabIndex = index;
    }

    public void setTextViewSelectedColor(int color) {
        this.mTextViewSelColor = color;
    }

    public void setTextViewUnSelectedColor(int color) {
        this.mTextViewUnSelColor = color;
    }

    public void setTabViewBackgroundColor(int color) {
        this.mTabViewBackgroundColor = color;
        tabview.setBackgroundColor(color);
    }

    /**
     * @param height px
     */
    public void setTabViewHeight(int height) {
        this.mTabViewHeight = height;
    }


    /**
     * @param margin px
     */
    public void setImageViewTextViewMargin(int margin) {
        this.mImageViewTextViewMargin = margin;
    }

    public void setTextViewSize(int size) {
        this.mTextViewSize = TabViewUtil.sp2px(getContext(), size);
    }

    public void setImageViewWidth(int width) {
        this.mImageViewWidth = width;
    }

    public void setImageViewHeight(int height) {
        this.mImageViewHeight = height;
    }

    public void setTabViewGravity(int gravity) {
        this.mTabViewGravity = gravity;
    }

    public void setUnReadCount(int tabPosition, int unReadCount) {
        setUnReadCount(tabPosition, unReadCount, true);
    }

    /**
     * 设置tab未读数
     *
     * @param tabPosition  tabPosition
     * @param unReadCount  未读数
     * @param isShowNumber true显示未读数 false显示红点
     */
    public void setUnReadCount(int tabPosition, int unReadCount, boolean isShowNumber) {
        if (tabPosition < 0 || tabPosition >= tabview.getChildCount()) {
            return;
        }
        FrameLayout tabChild = (FrameLayout) tabview.getChildAt(tabPosition);
        TextView textView = (TextView) tabChild.getChildAt(1);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setBackgroundResource(R.drawable.bg_red_text);
        textView.setMaxLines(1);
        int unReadTextSize;
        if (isShowNumber) {
            unReadTextSize = 18;
        } else {
            unReadTextSize = 8;
        }
        FrameLayout.LayoutParams msgTextLayoutParams = new FrameLayout.LayoutParams(SizeUtils.dp2px(getContext(), unReadTextSize),
                SizeUtils.dp2px(getContext(), unReadTextSize));
        msgTextLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setGravity(Gravity.CENTER);
        msgTextLayoutParams.leftMargin = SizeUtils.dp2px(getContext(), leftMargin);
        msgTextLayoutParams.topMargin = 10;
        textView.setLayoutParams(msgTextLayoutParams);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        if (unReadCount > 0) {
            textView.setVisibility(VISIBLE);
            if (isShowNumber) {
                int textSize = 12;
                String unReadCountStr = String.valueOf(unReadCount);
                if (unReadCount > 10 && unReadCount <= 99) {
                    textSize = 10;
                } else if (unReadCount > 99) {
                    textSize = 8;
                    unReadCountStr = "99+";
                }
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                textView.setText(unReadCountStr);
            }
        } else {
            textView.setText("");
            textView.setVisibility(GONE);
        }
        unReadCountMap.put(tabPosition, unReadCount);
        if (!isShowNumber && unReadCount > 0) {
            boolean hasKey = false;
            for (Integer in : unReadCountMap.keySet()) {
                Integer value = unReadCountMap.get(in);
                if (value == tabPosition) {
                    hasKey = true;
                    break;
                }
            }
            if (!hasKey) {
                unReadCountMap.put(tabPosition, -1);
            }
        }
    }
}
