package com.bfsty.disruptor.qs;

import com.lmax.disruptor.EventFactory;

public class OrderEventFactory implements EventFactory<OrderEvent>{

    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();   // 该方法就是为了返回空的数据对象（event）
    }
}
