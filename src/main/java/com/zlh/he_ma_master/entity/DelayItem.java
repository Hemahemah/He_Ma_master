package com.zlh.he_ma_master.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zlh.he_ma_master.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


/**
 * @author lh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelayItem implements Delayed {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(createTime.getTime() + Constants.ORDER_EXPIRE_TIME - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }
}
