package com.cmeplaza.basemodule.widget.tab;

/**
 * 作者：yaochangliang on 2016/8/13 18:37
 * 邮箱：yaochangliang159@sina.com
 */
public class TabViewChild {
    private int imageViewSelIcon;
    private int imageViewUnSelIcon;
    private String textViewText;
    private int unReadCount;


    private TabViewChild() {

    }

    public TabViewChild(int imageViewSelIcon, int imageViewUnSelIcon, String textViewText) {
        this.imageViewSelIcon = imageViewSelIcon;
        this.imageViewUnSelIcon = imageViewUnSelIcon;
        this.textViewText = textViewText;
    }

    public int getImageViewSelIcon() {
        return imageViewSelIcon;
    }


    public void setImageViewSelIcon(int imageViewSelIcon) {
        this.imageViewSelIcon = imageViewSelIcon;
    }


    public int getImageViewUnSelIcon() {
        return imageViewUnSelIcon;
    }


    public void setImageViewUnSelIcon(int imageViewUnSelIcon) {
        this.imageViewUnSelIcon = imageViewUnSelIcon;
    }


    public String getTextViewText() {
        return textViewText;
    }


    public void setTextViewText(String textViewText) {
        this.textViewText = textViewText;
    }


    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
