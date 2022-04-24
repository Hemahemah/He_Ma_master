package com.zlh.he_ma_master.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.mall.vo.MallOrderDetailVO;
import com.zlh.he_ma_master.api.mall.vo.MallOrderListVO;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.service.MallOrderService;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma")
public class AdminOrderController {

    @Resource
    private MallOrderService mallOrderService;

    private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);

    @GetMapping("/orders")
    public Result<Page<MallOrderListVO>> getOrderList(@RequestParam(required = false) Integer pageNumber,
                               @RequestParam(required = false) Integer pageSize,
                               @RequestParam(required = false) String orderNo,
                               @RequestParam(required = false) Integer orderStatus,
                               @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("admin get order api,adminUser={}", adminUserToken.getUserId());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < Constants.PAGE_MIN_SIZE){
            return ResultGenerator.getFailResult("分页参数异常");
        }
        Page<MallOrderListVO> mallOrderListVoPage = mallOrderService.getOrderPage(pageNumber, pageSize, orderNo, orderStatus);
        if (mallOrderListVoPage != null){
            return ResultGenerator.getSuccessResult(mallOrderListVoPage);
        }else {
            return ResultGenerator.getFailResult("获取订单数据失败!");
        }
    }

    @GetMapping("/orders/{orderId}")
    public Result<MallOrderDetailVO> getOrderDetail(@PathVariable Integer orderId, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("admin get order detail api,adminUser={}", adminUserToken.getUserId());
        MallOrderDetailVO mallOrderDetailVo = mallOrderService.getOrderDetailByOrderId(orderId);
        if (mallOrderDetailVo != null){
            return ResultGenerator.getSuccessResult(mallOrderDetailVo);
        }else {
            return ResultGenerator.getFailResult("获取订单详情失败!");
        }
    }

    @PutMapping("/orders/checkDone")
    public Result<String> checkDone(@RequestBody BatchIdParam idParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("admin check done order api,adminUser={}", adminUserToken.getUserId());
        if (idParam.getIds().length <= 0){
            return ResultGenerator.getFailResult("参数异常!");
        }
        if (mallOrderService.checkDone(idParam)){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("配货失败!");
        }
    }

    @PutMapping("/orders/checkOut")
    public Result<String> checkOut(@RequestBody BatchIdParam idParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("admin check out order api,adminUser={}", adminUserToken.getUserId());
        if (idParam.getIds().length <= 0){
            return ResultGenerator.getFailResult("参数异常!");
        }
        if (mallOrderService.checkOut(idParam)){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("配货失败!");
        }
    }

    @PutMapping("/orders/close")
    public Result<String> handleClose(@RequestBody BatchIdParam idParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("admin close order api,adminUser={}", adminUserToken.getUserId());
        if (idParam.getIds().length <= 0){
            return ResultGenerator.getFailResult("参数异常!");
        }
        if (mallOrderService.handleClose(idParam)){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("配货失败!");
        }
    }
}
