package com.service.wanhui.wanhuiservice.bean;

/**
 * Created by klx on 2018/9/4.
 */

public class ShareBean {

    /**
     * href : link
     * title : title
     * desc : desc
     * thumb : imgUrl
     */

    private String href;
    private String title;
    private String desc;
    private String thumb;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
