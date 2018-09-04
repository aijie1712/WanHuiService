package com.cmeplaza.basemodule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cmeplaza.basemodule.R;

/**
 * 作者：Android_AJ on 2017/4/12.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 */
public class InputItemView extends RelativeLayout {
    private RelativeLayout mRootView;
    // 图标
    private ImageView iv_tag;
    // 输入框
    private EditText et_input;
    // 隐藏密码框
    private ImageView iv_hide;
    // 清空
    private ImageView iv_delete;
    private View view_divide;

    // 图标
    private int imageTag;
    // EditText最多可输入的长度
    private int maxEditTextLength;
    // EditText输入格式
    private int inputType;
    // 提示文本
    private String inputHint;
    // 是否显示隐藏图标
    private boolean isShowHideImage;

    private boolean isHide = true;

    private boolean showDelImage = false;

    public InputItemView(Context context) {
        this(context, null);
    }

    public InputItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_common_item_input, this);
        // 初始化控件
        initView();
        // 获取定义的属性
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputItemView);
        imageTag = a.getResourceId(R.styleable.InputItemView_iv_tag, 0);
        inputType = a.getInt(R.styleable.InputItemView_input_editText_Type, 0);

        inputHint = a.getString(R.styleable.InputItemView_input_hint);
        maxEditTextLength = a.getInteger(R.styleable.InputItemView_input_max_length, 30);
        isShowHideImage = a.getBoolean(R.styleable.InputItemView_input_show_hide_image, false);
        showDelImage = a.getBoolean(R.styleable.InputItemView_input_show_del_image, false);
        a.recycle();

        if (imageTag > 0) {
            iv_tag.setVisibility(View.VISIBLE);
            view_divide.setVisibility(GONE);
            iv_tag.setImageResource(imageTag);
        }else{
            iv_tag.setVisibility(View.GONE);
            view_divide.setVisibility(GONE);
        }
        if (et_input != null) {
            InputFilter[] filters = {new InputFilter.LengthFilter(maxEditTextLength)};
            et_input.setFilters(filters);
            et_input.setHint(inputHint);
            if (inputType > 0) {
                // 设置输入类型
                switch (inputType) {
                    case 101:
                        et_input.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        break;
                    case 102:  // number
                        et_input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                        break;
                    case 103:  // phone
                        et_input.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                        break;
                    case 104:  // pwd
                        et_input.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
            }
        }

        if (iv_hide != null && isShowHideImage) {
            iv_hide.setVisibility(View.VISIBLE);
            iv_hide.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isHide = !isHide;
                    if (isHide) {
                        iv_hide.setImageResource(R.drawable.icon_eye_close);
                        et_input.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                    } else {
                        iv_hide.setImageResource(R.drawable.icon_eye_open);
                        et_input.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    et_input.setSelection(et_input.getText().length());
                }
            });
        }
    }

    private void initView() {
        iv_tag = (ImageView) mRootView.findViewById(R.id.iv_tag);
        et_input = (EditText) mRootView.findViewById(R.id.et_input);
        iv_hide = (ImageView) mRootView.findViewById(R.id.iv_hide);
        iv_delete = (ImageView) mRootView.findViewById(R.id.iv_delete);
        view_divide = mRootView.findViewById(R.id.view_divide);

        iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("");
            }
        });

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!showDelImage) {
                    return;
                }
                if (TextUtils.isEmpty(s)) {
                    iv_delete.setVisibility(View.GONE);
                } else {
                    iv_delete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 获取输入文本
     *
     * @return
     */
    public String getEditTextString() {
        return et_input.getText().toString().trim();
    }

    public EditText getEditInput(){
        return et_input;
    }

    public void setEditTextString(String string) {
        et_input.setText(string);
    }

    public void setEditTextString(int text) {
        setEditTextString(getContext().getString(text));
    }

    public void setInputHint(String hint) {
        et_input.setHint(hint);
    }

    public void setInputHint(int hintResid) {
        setInputHint(getContext().getString(hintResid));
    }
}
