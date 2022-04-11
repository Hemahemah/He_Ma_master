package com.zlh.he_ma_master.api.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.mall.vo.MallSearchGoodsVO;
import com.zlh.he_ma_master.config.annotation.TokenToMallUser;
import com.zlh.he_ma_master.entity.MallUser;
import com.zlh.he_ma_master.service.GoodsInfoService;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma_api")
public class MallGoodsController {

    @Resource
    private GoodsInfoService goodsInfoService;

    private static final Logger logger = LoggerFactory.getLogger(MallGoodsController.class);

    @GetMapping("/search")
    public Result<Page<MallSearchGoodsVO>> search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Long goodsCategoryId,
                         @RequestParam(required = false) String orderBy,
                         @RequestParam(required = false) Integer pageNumber,
                         @TokenToMallUser MallUser mallUser){
        logger.info("goods search api,keyword={},goodsCategoryId={},orderBy={},pageNumber={},userId={}", keyword, goodsCategoryId, orderBy, pageNumber, mallUser.getUserId());
        // 商品分类与搜索关键字同时为空
        if (goodsCategoryId == null && !StringUtils.hasText(keyword)){
            return ResultGenerator.getFailResult("请输入关键词!");
        }
        if (pageNumber == null){
            pageNumber = 1;
        }
        Page<MallSearchGoodsVO> mallSearchGoodsVoPage = goodsInfoService.searchGoods(keyword, goodsCategoryId, orderBy, pageNumber);
        return ResultGenerator.getSuccessResult(mallSearchGoodsVoPage);
    }


}
