package com.bubble.disruptor.service;

import com.bubble.disruptor.bean.CommonData;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生产者
 *
 * @author wugang
 * date: 2019-03-29 15:18
 **/
public class CommonProducer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonProducer.class);

    private final RingBuffer<CommonData> ringBuffer;

    public CommonProducer(RingBuffer<CommonData> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @SuppressWarnings("unchecked")
    public void pushData(long uid, T list) {
        long sequence = ringBuffer.next();
        try {
            CommonData data = ringBuffer.get(sequence);
            data.setUid(uid);
            data.setT(list);
            LOGGER.info("{} has add user {}", Thread.currentThread().getName(), uid);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

}
