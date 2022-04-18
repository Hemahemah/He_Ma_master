package com.zlh.he_ma_master.api.mall;

import com.zlh.he_ma_master.api.mall.param.SaveOrderParam;
import com.zlh.he_ma_master.config.annotation.TokenToMallUser;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.service.OrderService;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma_api")
public class MallOrderController {

    @Resource
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(MallOrderController.class);

    @PostMapping("/saveOrder")
    public Result<String> saveOrder(@TokenToMallUser MallUser mallUser, @RequestBody @Valid SaveOrderParam saveOrderParam){
        logger.info("save order api,user={}", mallUser.getUserId());
        if (saveOrderParam.getCartItemIds().length <= 0){
            return ResultGenerator.getFailResult("参数异常");
        }
        String orderResult = orderService.saveOrder(saveOrderParam, mallUser);
        if (orderResult == null){
            return ResultGenerator.getFailResult("test");
        }else {
            return ResultGenerator.getSuccessResult(orderResult);
        }
    }

}
