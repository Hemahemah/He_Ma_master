package com.zlh.he_ma_master.api.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.mall.param.SaveOrderParam;
import com.zlh.he_ma_master.api.mall.vo.MallOrderListVO;
import com.zlh.he_ma_master.config.annotation.TokenToMallUser;
import com.zlh.he_ma_master.entity.MallOrder;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.service.MallOrderService;
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
    private MallOrderService mallOrderService;

    private static final Logger logger = LoggerFactory.getLogger(MallOrderController.class);

    @PostMapping("/saveOrder")
    public Result<String> saveOrder(@TokenToMallUser MallUser mallUser, @RequestBody @Valid SaveOrderParam saveOrderParam){
        logger.info("save order api,user={}", mallUser.getUserId());
        if (saveOrderParam.getCartItemIds().length <= 0){
            return ResultGenerator.getFailResult("参数异常");
        }
        String orderResult = mallOrderService.saveOrder(saveOrderParam, mallUser);
        if (orderResult == null){
            return ResultGenerator.getFailResult("生成订单异常！请联系客服");
        }else {
            return ResultGenerator.getSuccessResult(orderResult);
        }
    }

    @GetMapping("/paySuccess")
    public Result<String> paySuccess(@RequestParam String orderNo, @RequestParam int payType, @TokenToMallUser MallUser mallUser){
        logger.info("pay order api,user={}", mallUser.getUserId());
        if (!mallOrderService.payOrder(orderNo, payType, mallUser.getUserId())){
            return ResultGenerator.getFailResult("支付异常！请联系客服");
        }else {
            return ResultGenerator.getSuccessResult();
        }
    }

    @GetMapping("/order")
    public Result getOrderList(@RequestParam(required = false) Integer pageNumber,
                               @RequestParam(required = false) int status,
                               @TokenToMallUser MallUser mallUser){
        if (pageNumber == null || pageNumber < 1){
            pageNumber = 1;
        }
        Page<MallOrderListVO> mallOrderListVoPage =  mallOrderService.getOrderList(pageNumber, status, mallUser.getUserId());
        return null;
    }

}
