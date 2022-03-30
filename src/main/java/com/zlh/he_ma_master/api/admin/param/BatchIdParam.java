package com.zlh.he_ma_master.api.admin.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BatchIdParam implements Serializable {

    @NotNull(message = "参数异常")
    private Integer[] ids;
}
