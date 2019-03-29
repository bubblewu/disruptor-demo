package com.bubble.disruptor;

import com.bubble.disruptor.bean.CommonData;
import com.bubble.disruptor.bean.DataBean;
import com.bubble.disruptor.factory.CommonFactory;
import com.bubble.disruptor.service.CommonConsumer;
import com.bubble.disruptor.service.CommonProducer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 泛型时处理的demo
 *
 * @author wugang
 * date: 2019-03-29 15:24
 **/
public class CommonMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonMain.class);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Instant begin = Instant.now();
        CommonFactory factory = new CommonFactory();
        int bufferSize = 1024 * 1024; // 缓冲区的大小，必须为2的整数次幂。
        Disruptor<CommonData> disruptor = new Disruptor<>(factory,
                bufferSize,
                DaemonThreadFactory.INSTANCE,
                ProducerType.MULTI,
                new BlockingWaitStrategy()); // 监控缓冲区中信息的默认策略。和使用BlockingQueue类似，使用锁和条件(Condition)进行数据监控和线程的唤醒。
        // 设置四个消费者，系统会把每个消费者映射到一个线程中，下面提供了四个消费者线程。(需小于指定的线程池中线程数量)
        disruptor.handleEventsWithWorkerPool(
                new CommonConsumer("test"),
                new CommonConsumer("test"),
                new CommonConsumer("test"),
                new CommonConsumer("test")
        );
        // 启动并初始化Disruptor系统
        disruptor.start();

        RingBuffer<CommonData> ringBuffer = disruptor.getRingBuffer();
        CommonProducer producer = new CommonProducer(ringBuffer);
        genUid().parallelStream().forEach(uid -> producer.pushData(uid, genData()));
        LOGGER.info("add user total count is {}", genUid().size());

        disruptor.shutdown(); //关闭 disruptor，方法会堵塞，直至所有的事件都得到处理

        LOGGER.info("job done, total costs {} ms", Duration.between(begin, Instant.now()).toMillis());
    }

    private static List<Long> genUid() {
        return Arrays.stream("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20".split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private static List<DataBean> genData() {
        List<DataBean> dataBeanList = new ArrayList<>(1);
        DataBean dataBean = new DataBean();
        dataBean.setValue(100);
        dataBeanList.add(dataBean);
        return dataBeanList;
    }

}
