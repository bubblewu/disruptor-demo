package com.bubble.disruptor;

import com.bubble.disruptor.bean.DataBean;
import com.bubble.disruptor.factory.DataFactory;
import com.bubble.disruptor.service.Consumer;
import com.bubble.disruptor.service.Producer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;

/**
 * 基于Disruptor无锁缓存框架的生产者-消费者实现demo
 * (Disruptor的性能要比BlockingQueue至少高一个数量级以上）
 *
 * @author wugang
 * date: 2019-03-29 14:30
 **/
public class DisruptorMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorMain.class);

    public static void main(String[] args) throws InterruptedException {
        Instant begin = Instant.now();
        DataFactory factory = new DataFactory();
        int bufferSize = 1024 * 1024; // 缓冲区的大小，必须为2的整数次幂。
        Disruptor<DataBean> disruptor = new Disruptor<>(factory,
                bufferSize,
                DaemonThreadFactory.INSTANCE,
                ProducerType.MULTI,
                new BlockingWaitStrategy()); // 监控缓冲区中信息的默认策略。和使用BlockingQueue类似，使用锁和条件(Condition)进行数据监控和线程的唤醒。
        // 设置四个消费者，系统会把每个消费者映射到一个线程中，下面提供了四个消费者线程。(需小于指定的线程池中线程数量)
        disruptor.handleEventsWithWorkerPool(
                new Consumer(),
                new Consumer(),
                new Consumer(),
                new Consumer()
        );
        // 启动并初始化Disruptor系统
        disruptor.start();

        RingBuffer<DataBean> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);
        // ByteBuffer.allocate方法创建并分配一个私有的空间来储存指定容量大小的数据元素。
        // ByteBuffer.allocate(8) 创建一个容量为8字节的ByteBuffer，如果发现创建的缓冲区容量太小，那么只能重新创建。
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (long i = 0; i < 20; i++) {
            byteBuffer.putLong(0, i);
            producer.pushData(byteBuffer);
            Thread.sleep(100);
            LOGGER.info("{} add data: {}", Thread.currentThread().getName(), i);
        }
        disruptor.shutdown(); //关闭 disruptor，方法会堵塞，直至所有的事件都得到处理

        LOGGER.info("job done, total costs {} ms", Duration.between(begin, Instant.now()).toMillis());
    }

}
