package com.zlh.he_ma_master.api.admin.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchIdParam implements Serializable {

    @NotNull(message = "参数异常")
    private Long[] ids;


}
