package com.bfsty.disruptor.ability;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

public class DisruptorSingle4Test {

    public static void main(String[] args) {
        int ringBufferSize = 65536;

        final Disruptor<Data> dataDisruptor = new Disruptor<Data>(
                new EventFactory<Data>() {
            @Override
            public Data newInstance() {
                return new Data();
            }
        },ringBufferSize, Executors.newSingleThreadExecutor(),
                ProducerType.SINGLE,new YieldingWaitStrategy());

        DataConsumer dataConsumer = new DataConsumer();
        dataDisruptor.handleEventsWith(dataConsumer);

        dataDisruptor.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RingBuffer<Data> ringBuffer = dataDisruptor.getRingBuffer();
                for (long i=0;i<Constants.EVENT_NUM_OM;i++){
                    long seq = ringBuffer.next();
                    Data data = ringBuffer.get(seq);
                    data.setId(i);
                    data.setName("c"+i);
                    ringBuffer.publish(seq);


                }
            }
        }).start();


    }
}
