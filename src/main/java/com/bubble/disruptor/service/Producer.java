package com.bubble.disruptor.service;

import com.bubble.disruptor.bean.DataBean;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * 生产者
 *
 * @author wugang
 * date: 2019-03-29 14:48
 **/
public class Producer {
    // 定义环形队列，用来作为共享内存缓冲区。
    private final RingBuffer<DataBean> ringBuffer;

    public Producer(RingBuffer<DataBean> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 发布事件:
     * Disruptor 的事件发布过程是一个两阶段提交的过程：
     * 　　第一步：先从 RingBuffer 获取下一个可以写入的事件的序号；
     * 　　第二步：获取对应的事件对象，将数据写入事件对象；
     * 　　第三部：将事件提交到 RingBuffer;
     * 事件只有在提交之后才会通知 EventProcessor 进行处理；
     * <p>
     * 将传入的ByteBuffer中的数据提取出，将产生的数据推入缓冲区。
     *
     * @param byteBuffer 传入的ByteBuffer数据（ByteBuffer可以用来包装任何数据类型）
     */
    public void pushData(ByteBuffer byteBuffer) {
        // 序列自增并返回循环缓冲区的下一个序列。调用这个方法之后应该确保要发布序列。
        // 序列sequence对应数组的实际位置，有元素入队列，序列就加1
        long sequence = ringBuffer.next();
        try {
            // 通过序列号获取下一个空闲的PCData
            DataBean data = ringBuffer.get(sequence);
            // 将PCData的数据设置为期望值
            data.setValue(byteBuffer.getLong(0));
        } finally {
            // 最后的ringBuffer.publish方法必须包含在finally中以确保必须得到调用；
            // 如果某个请求的sequence未被提交，将会堵塞后续的发布操作或者其它的producer。
            // 数据发布， 只有发布后的数据才可以被消费者看到
            ringBuffer.publish(sequence);
        }
    }


}
