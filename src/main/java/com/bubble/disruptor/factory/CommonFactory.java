package com.bubble.disruptor.factory;

import com.bubble.disruptor.bean.CommonData;
import com.lmax.disruptor.EventFactory;

/**
 * 一个产生CommonData的工厂类。
 * 它会在Disruptor（无锁的缓存框架，核心就是环形队列RingBuffer）系统初始化时，构造所有的缓冲区中的实例对象。
 * 也就是说Disruptor会预先分配空间。
 *
 * @author wugang
 * date: 2019-03-29 14:48
 **/
public class CommonFactory implements EventFactory<CommonData> {

    @Override
    public CommonData newInstance() {
        return new CommonData();
    }

}
