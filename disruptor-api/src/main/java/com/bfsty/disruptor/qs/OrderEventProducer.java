package com.bfsty.disruptor.qs;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class OrderEventProducer {

    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(ByteBuffer data){
        // 1 在生产者发送消息的时候，首先需要从ringBuffer中获取一个可用的序列号
        long sequence = ringBuffer.next();
        try {
            // 2 根据这个序号找到具体的orderEvent元素，注意：此时获取的orderEvent是没有赋值的空值对象
            OrderEvent orderEvent = ringBuffer.get(sequence);
            // 3 进行实际的赋值
            orderEvent.setValue(data.getLong(0));
        }finally {
            // 4 提交发布操作
            ringBuffer.publish(sequence);
        }

    }
}
