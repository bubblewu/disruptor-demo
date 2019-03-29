package com.bubble.disruptor.service;

import com.bubble.disruptor.bean.DataBean;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消费者。实现为disruptor框架的WorkHandler接口。
 * 读取生产的数据并进行处理。
 *
 * @author wugang
 * date: 2019-03-29 14:41
 **/
public class Consumer implements WorkHandler<DataBean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    /**
     * 数据的读取已经由disruptor进行了封装，onEvent方法是框架的回调方法。
     * 所以，这里只需要简单的进行数据处理即可。
     *
     * @param event 数据
     * @throws Exception 异常
     */
    @Override
    public void onEvent(DataBean event) throws Exception {
        long result = event.getValue() * event.getValue(); // 求整数的平方
        LOGGER.info("thread name: {}, thread id: {}, data: {}, result: {}",
                Thread.currentThread().getName(),
                Thread.currentThread().getId(),
                event.getValue(),
                result);
    }

}
