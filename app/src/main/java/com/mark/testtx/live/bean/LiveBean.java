package com.mark.testtx.live.bean;

/**
 *
 * Created by Mark.Han on 2017/4/24.
 */
public class LiveBean {


    private String title;//标题
    private int id;//id
    private int type;//分类
    private String imageUrl;//图片地址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
