package com.bfsty.disruptor.qs;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        // 参数准备工作
        OrderEventFactory eventFactory = new OrderEventFactory();
        int ringBufferSize = 1024 * 1024; // 有界的容器

        // 建议生产中使用时不要使用这种方式，而是应该自己 new ThreadPoolExecutor  ，使用有界的容器，避免处理不过来时内存被打满
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


        /**
         * 1 eventFactory: 消息（event）工厂对象
         * 2 ringBufferSize: 容器长度
         * 3 executor： 线程池（建议使用自定义线程池，需要重写RejectedExecutionHandler）
         * 4 ProductType ：但生产者还是多生产者
         * 5 waitStrategy：等待策略
         */

        //1 .实例化disruptor对象
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(eventFactory,
                ringBufferSize,
                executor,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()); // 等待策略

        // 2.添加消费者的监听(构建disruptor与一个消费者的关联关系)
        disruptor.handleEventsWith(new OrderEventHandler());

        // 3. 启动disruptor
        disruptor.start();

        //4. 获取实际存储数据的容器：RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        OrderEventProducer eventProducer = new OrderEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);

        for (long i = 0; i < 100; i++) {
            bb.putLong(0,i);
            eventProducer.sendData(bb);
        }

        disruptor.shutdown();
        executor.shutdown();


    }

}
