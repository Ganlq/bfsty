package com.bfsty.disruptor.ability;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockQueue4Test {

    public static void main(String[] args) {
        final ArrayBlockingQueue<Data> queue = new ArrayBlockingQueue<>(100000000);
        final long start = System.currentTimeMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long i=0;
                while (i<Constants.EVENT_NUM_OM){
                    Data data = new Data(i, "c" + i);
                    try {
                        queue.put(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int k = 0;
                while (k<Constants.EVENT_NUM_OM){
                    try {
                        queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    k++;

                }
                long end = System.currentTimeMillis();
                System.out.printf("ArrayBlockQueue Cost: "+(end-start)+"ms");
            }
        }).start();

    }
}
