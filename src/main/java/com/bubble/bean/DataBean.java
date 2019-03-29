package com.bubble.bean;

import java.io.Serializable;

/**
 * 数据类
 *
 * @author wugang
 * date: 2019-03-29 14:28
 **/
public class DataBean implements Serializable {
    private static final long serialVersionUID = 7527731914943826380L;

    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "value=" + value +
                '}';
    }
}
