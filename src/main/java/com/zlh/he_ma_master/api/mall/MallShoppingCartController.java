package com.zlh.he_ma_master.api.mall;

import com.zlh.he_ma_master.api.mall.param.SaveCartParam;
import com.zlh.he_ma_master.api.mall.param.UpdateCartItemParam;
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
import java.util.List;

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
    public Result<List<MallShoppingCartItemVO>> getCartItems(@TokenToMallUser MallUser mallUser){
        logger.info("get shopping cart item api,user={}", mallUser.getUserId());
        List<MallShoppingCartItemVO> cartItemVoList = shoppingCartItemService.getCartItems(mallUser.getUserId());
        if (cartItemVoList != null){
            return ResultGenerator.getSuccessResult(cartItemVoList);
        }else {
            return ResultGenerator.getFailResult("获取信息失败");
        }
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

    @PutMapping("/shop-cart")
    public Result<String> updateCartItem(@TokenToMallUser MallUser mallUser, @RequestBody @Valid UpdateCartItemParam updateCartItemParam){
        logger.info("update shopping cart item api,user={}", mallUser.getUserId());
        if (shoppingCartItemService.updateCartItem(updateCartItemParam, mallUser.getUserId())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("修改商品失败");
        }
    }

    @DeleteMapping("/shop-cart/{shoppingCartItemId}")
    public Result<String> deleteCartItem(@TokenToMallUser MallUser mallUser, @PathVariable Long shoppingCartItemId){
        logger.info("delete shopping cart item api,user={}", mallUser.getUserId());
        if (shoppingCartItemService.deleteCartItem(shoppingCartItemId, mallUser.getUserId())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult("删除商品失败");
        }
    }
}
