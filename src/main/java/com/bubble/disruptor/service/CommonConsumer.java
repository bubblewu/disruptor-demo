package com.bubble.disruptor.service;

import com.bubble.disruptor.bean.CommonData;
import com.bubble.disruptor.bean.DataBean;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 消费者。实现为disruptor框架的WorkHandler接口。
 * 读取生产的数据并进行处理。
 *
 * @author wugang
 * date: 2019-03-29 15:18
 **/
public class CommonConsumer implements WorkHandler<CommonData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConsumer.class);
    private final String type;

    public CommonConsumer(String type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(CommonData event) {
        long uid = event.getUid();
        LOGGER.info("{} start rec for user {}", Thread.currentThread().getName(), uid);

        List<DataBean> dataList = (List<DataBean>) event.getT();
        try {
            doSomething(uid, dataList);
        } catch (Exception re) {
            LOGGER.error("now thread {} rec exception: {}", Thread.currentThread().getName(), re);
        }
    }

    private void doSomething(long uid, List<DataBean> dataList) {
        LOGGER.info("{} end rec for user: {}, type: {}, data count: {}", Thread.currentThread().getName(), uid, type, dataList.size());
    }

}
