package com.zlh.he_ma_master.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.ConfigEditParam;
import com.zlh.he_ma_master.entity.IndexConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 首页配置管理模块
* @author lh
* @createDate 2022-04-02 21:03:31
*/
public interface IndexConfigService extends IService<IndexConfig> {

    /**
     * 获取配置列表
     * @param pageNumber 页码
     * @param pageSize 分页大小
     * @param configType 配置类型
     * @return 分页参数
     */
    Page<IndexConfig> getPages(Integer pageNumber, Integer pageSize, Integer configType);

    /**
     * 修改配置信息
     * @param editParam 配置参数
     * @return true修改成功
     */
    boolean updateConfig(ConfigEditParam editParam);
}
