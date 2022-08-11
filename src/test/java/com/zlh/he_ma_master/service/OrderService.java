package com.zlh.he_ma_master.service;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class OrderService implements Delayed {
    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
