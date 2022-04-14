package com.zlh.he_ma_master.api.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.mall.param.SaveCartParam;
import com.zlh.he_ma_master.api.mall.vo.MallShoppingCartItemVO;
import com.zlh.he_ma_master.config.annotation.TokenToMallUser;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.service.ShoppingCartItemService;
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
public class MallShoppingCartController {

    @Resource
    private ShoppingCartItemService shoppingCartItemService;

    private static final Logger logger = LoggerFactory.getLogger(MallShoppingCartController.class);

    @GetMapping("/shop-cart")
    public Result<Page<MallShoppingCartItemVO>> getCartItems(@TokenToMallUser MallUser mallUser){
        logger.info("get shopping cart item api,user={}", mallUser.getUserId());
        // todo get cart items
        return null;
    }

    @PostMapping("/shop-cart")
    public Result<String> saveCartItem(@TokenToMallUser MallUser mallUser, @RequestBody @Valid SaveCartParam saveCartParam){
        logger.info("save shopping cart item api,user={}", mallUser.getUserId());
        if (shoppingCartItemService.saveCartItem(saveCartParam, mallUser.getUserId())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("保存商品失败");
        }
    }
}
