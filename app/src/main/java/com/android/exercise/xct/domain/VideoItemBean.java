package com.android.exercise.xct.domain;

import java.io.Serializable;

/**
 * Created by xct on 2018/5/17.
 */
// 使用bundle传递对象要序列化
public class VideoItemBean implements Serializable {

    //标题
    private String title;
    //时长
    private String duration;
    //大小
    private long size;
    //绝对路径
    private String data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VideoItemBean{" +
                "title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                ", size=" + size +
                ", data='" + data + '\'' +
                '}';
    }
}
