package com.zlh.he_ma_master.api.mall;

import com.zlh.he_ma_master.api.mall.vo.MallIndexInfoVO;
import com.zlh.he_ma_master.service.IndexConfigService;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma_api")
public class MallIndexController {

    @Resource
    private IndexConfigService indexConfigService;


    @GetMapping("/index-infos")
    public Result<MallIndexInfoVO> getIndexInfos(){
        return ResultGenerator.getSuccessResult(indexConfigService.getConfigGoodsForIndex());
    }

}
