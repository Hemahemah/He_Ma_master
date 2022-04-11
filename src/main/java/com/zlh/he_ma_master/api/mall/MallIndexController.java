package com.zlh.he_ma_master.api.mall;

import com.zlh.he_ma_master.api.mall.vo.MallIndexCarouselVO;
import com.zlh.he_ma_master.api.mall.vo.MallIndexConfigGoodsVO;
import com.zlh.he_ma_master.api.mall.vo.MallIndexInfoVO;
import com.zlh.he_ma_master.common.IndexConfigTypeEnum;
import com.zlh.he_ma_master.service.CarouselService;
import com.zlh.he_ma_master.service.IndexConfigService;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma_api")
public class MallIndexController {

    @Resource
    private IndexConfigService indexConfigService;

    @Resource
    private CarouselService carouselService;

    @GetMapping("/index-infos")
    public Result<MallIndexInfoVO> getIndexInfos(){
        MallIndexInfoVO mallIndexInfoVO = new MallIndexInfoVO();
        List<MallIndexCarouselVO> carousel = carouselService.getCarouselForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<MallIndexConfigGoodsVO> hotGoodies = indexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<MallIndexConfigGoodsVO> newGoodies = indexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<MallIndexConfigGoodsVO> recommendGoodies = indexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMEND.getType(), Constants.INDEX_GOODS_RECOMMEND_NUMBER);
        mallIndexInfoVO.setCarousels(carousel);
        mallIndexInfoVO.setHotGoodses(hotGoodies);
        mallIndexInfoVO.setNewGoodses(newGoodies);
        mallIndexInfoVO.setRecommendGoodses(recommendGoodies);
        return ResultGenerator.getSuccessResult(mallIndexInfoVO);
    }

}
