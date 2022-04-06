package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.ConfigAddParam;
import com.zlh.he_ma_master.api.admin.param.ConfigEditParam;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.IndexConfig;
import com.zlh.he_ma_master.service.GoodsInfoService;
import com.zlh.he_ma_master.service.IndexConfigService;
import com.zlh.he_ma_master.dao.IndexConfigMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author lh
* @createDate 2022-04-02 21:03:31
*/
@Service
public class IndexConfigServiceImpl extends ServiceImpl<IndexConfigMapper, IndexConfig>
    implements IndexConfigService{

    @Resource
    private GoodsInfoService goodsInfoService;

    @Override
    public Page<IndexConfig> getPages(Integer pageNumber, Integer pageSize, Integer configType) {
        Page<IndexConfig> page = new Page<>(pageNumber,pageSize);
        QueryWrapper<IndexConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_type",configType).orderByDesc("config_rank");
        return page(page,queryWrapper);
    }

    @Override
    public boolean updateConfig(ConfigEditParam editParam) {
        IndexConfig indexConfig = copyProperties(editParam);
        // 1. 是否存在此商品
        if (goodsInfoService.getById(indexConfig.getGoodsId()) == null){
            throw new HeMaException(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        // 2. 是否存在此配置
        if (getById(indexConfig.getConfigId()) == null){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        // 3. 查询是否有存在编号不同但商品信息相同的配置
        QueryWrapper<IndexConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id",indexConfig.getGoodsId()).
                eq("config_type",indexConfig.getConfigType()).
                ne("config_id",indexConfig.getConfigId());
        IndexConfig config = getOne(queryWrapper);
        if (config != null){
            throw new HeMaException(ServiceResultEnum.SAME_INDEX_CONFIG_EXIST.getResult());
        }
        // 4. 修改
        return updateById(indexConfig);
    }

    @Override
    public boolean addConfig(ConfigAddParam addParam) {
        IndexConfig indexConfig = copyProperties(addParam);
        // 1. 是否存在此商品
        if (goodsInfoService.getById(indexConfig.getGoodsId()) == null){
            throw new HeMaException(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        // 2. 查询是否有存在编号不同但商品信息相同的配置
        QueryWrapper<IndexConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id",indexConfig.getGoodsId()).
                eq("config_type",indexConfig.getConfigType()).
                ne("config_id",indexConfig.getConfigId());
        IndexConfig config = getOne(queryWrapper);
        if (config != null){
            throw new HeMaException(ServiceResultEnum.SAME_INDEX_CONFIG_EXIST.getResult());
        }
        return save(indexConfig);
    }

    @Override
    public boolean removeConfig(Long[] ids) {
        // 1. 查询是否存在商品
        List<IndexConfig> indexConfigs = listByIds(Arrays.asList(ids));
        if (indexConfigs.size() < 1){
            throw new HeMaException(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return removeBatchByIds(Arrays.asList(ids));
    }

    private IndexConfig copyProperties(Object obj){
        IndexConfig indexConfig = new IndexConfig();
        BeanUtils.copyProperties(obj, indexConfig);
        indexConfig.setCreateTime(new Date());
        return indexConfig;
    }
}




