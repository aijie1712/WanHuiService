package com.cmeplaza.basemodule.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmeplaza.basemodule.R;

/**
 * Created by zhen on 2016/8/23.
 * uri:https://github.com/SolveBugs/CommonItem
 * 通用item
 */
public class CommonItem extends RelativeLayout {

    //left icon
    private Drawable mImg;

    //text
    private String mText;

    //textcolor
    private int mTextColor;

    //textsize
    private float mTextSize;

    //text
    private String mRightText;

    //textcolor
    private int mRightTextColor;

    //textsize
    private float mRightTextSize;

    //itme background
    private int mBackgroud;

    //right
    private Drawable mArrowRes;
    //root
    private RelativeLayout mRootView;

    //img -icon
    private ImageView mImageView;

    //arrow
    private ImageView mArrow;

    //text-img
    private TextView mTextView;

    private TextView mRightTextView;

    public CommonItem(Context context) {
        super(context);
    }

    public CommonItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.common_item_layout, this);
        initView(context);
        initAttrs(context, attrs);
    }

    /**
     * init views
     *
     * @param context
     */
    private void initView(Context context) {
        mImageView = (ImageView) mRootView.findViewById(R.id.image);
        mArrow = (ImageView) mRootView.findViewById(R.id.icon);
        mTextView = (TextView) mRootView.findViewById(R.id.text);
        mRightTextView = (TextView) mRootView.findViewById(R.id.tv_right);
    }

    /**
     * get Attrs
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonItem1);

        int mImgResid = a.getResourceId(R.styleable.CommonItem1_img_item, 0);
        int mArrowResid = a.getResourceId(R.styleable.CommonItem1_arrow_item, 0);
        int mTextResid = a.getResourceId(R.styleable.CommonItem1_text_item, 0);
        int mTextColorResid = a.getResourceId(R.styleable.CommonItem1_textcolor_item, 0);
        int mTextSizeResid = a.getResourceId(R.styleable.CommonItem1_textsize_item, 0);
        int mRightTextResid = a.getResourceId(R.styleable.CommonItem1_right_text_item, 0);
        int mRightTextColorResid = a.getResourceId(R.styleable.CommonItem1_right_textcolor_item, 0);
        int mRightTextSizeResid = a.getResourceId(R.styleable.CommonItem1_right_textsize_item, 0);
        int mBackgroundResid = a.getResourceId(R.styleable.CommonItem1_background_item, 0);

        if (mImgResid != 0) {
            mImg = context.getResources().getDrawable(mImgResid);
        }

        if (mTextResid != 0) {
            mText = context.getResources().getString(mTextResid);
        }

        if (mTextColorResid != 0) {
            mTextColor = context.getResources().getColor(mTextColorResid);
        }

        if (mTextSizeResid != 0) {
            mTextSize = context.getResources().getDimension(mTextSizeResid);
        }

        if (mRightTextResid != 0) {
            mRightText = context.getResources().getString(mTextResid);
        }

        if (mRightTextColorResid != 0) {
            mRightTextColor = context.getResources().getColor(mTextColorResid);
        }

        if (mRightTextSizeResid != 0) {
            mRightTextSize = context.getResources().getDimension(mTextSizeResid);
        }

        if (mArrowResid != 0) {
            mArrowRes = context.getResources().getDrawable(mArrowResid);
        }

        if (mBackgroundResid != 0) {
            mBackgroud = context.getResources().getColor(mBackgroundResid);
        }


        a.recycle();

        mTextView.setText(mText);
        mTextView.setTextColor(mTextColor);
        DisplayMetrics dm = getResources().getDisplayMetrics();
//        mTextView.setTextSize(mTextSize/dm.density);

        if (!TextUtils.isEmpty(mRightText)) {
            mRightTextView.setVisibility(View.VISIBLE);
            mRightTextView.setText(mRightText);
            mRightTextView.setTextColor(mRightTextColor);
            //mRightTextView.setTextSize(mTextSize);
        }

        if (mImg != null) {
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setBackground(mImg);
        } else {
            mImageView.setVisibility(View.GONE);
        }

        mArrow.setBackground(mArrowRes);

        setBackgroundColor(mBackgroud);
    }

    public String getRightText() {
        return mRightTextView.getText().toString().trim();
    }

    public void setRightText(String text) {
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText(text);
    }

    public int getUnReadNum() {
        if (mRightTextView.getVisibility() == VISIBLE) {
            return Integer.parseInt(mRightTextView.getText().toString());
        } else {
            return 0;
        }
    }

    public void setUnReadNum(int num) {
        if (num == 0) {
            mRightTextView.setVisibility(View.GONE);
        } else {
            mRightTextView.setVisibility(View.VISIBLE);
            mRightTextView.setText(String.valueOf(num));
            mRightTextView.setTextColor(Color.WHITE);
            mRightTextView.setBackgroundResource(R.drawable.bg_red_text);
        }
    }
}
