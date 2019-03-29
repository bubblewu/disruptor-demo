package com.bubble.bean;

/**
 * 支持泛型的基本类
 *
 * @author wugang
 * date: 2019-03-29 14:48
 **/
public class CommonData<T> {

    private long uid;
    private T t;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
