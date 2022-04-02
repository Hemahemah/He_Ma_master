package com.zlh.he_ma_master.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.ConfigEditParam;
import com.zlh.he_ma_master.entity.IndexConfig;
import com.zlh.he_ma_master.service.IndexConfigService;
import com.zlh.he_ma_master.dao.IndexConfigMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author lh
* @createDate 2022-04-02 21:03:31
*/
@Service
public class IndexConfigServiceImpl extends ServiceImpl<IndexConfigMapper, IndexConfig>
    implements IndexConfigService{

    @Override
    public Page<IndexConfig> getPages(Integer pageNumber, Integer pageSize, Integer configType) {
        Page<IndexConfig> page = new Page<>(pageNumber,pageSize);
        QueryWrapper<IndexConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_type",configType);
        return page(page,queryWrapper);
    }

    @Override
    public boolean updateConfig(ConfigEditParam editParam) {
        IndexConfig indexConfig = copyProperties(editParam);
        //todo update
        return false;
    }

    private IndexConfig copyProperties(Object obj){
        IndexConfig indexConfig = new IndexConfig();
        BeanUtils.copyProperties(obj, indexConfig);
        indexConfig.setCreateTime(new Date());
        return indexConfig;
    }
}




