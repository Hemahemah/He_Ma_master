package com.zlh.he_ma_master.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlh.he_ma_master.api.admin.param.ConfigAddParam;
import com.zlh.he_ma_master.api.admin.param.ConfigEditParam;
import com.zlh.he_ma_master.api.mall.vo.MallIndexCarouselVO;
import com.zlh.he_ma_master.api.mall.vo.MallIndexConfigGoodsVO;
import com.zlh.he_ma_master.api.mall.vo.MallIndexInfoVO;
import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.IndexConfigTypeEnum;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.entity.GoodsInfo;
import com.zlh.he_ma_master.entity.IndexConfig;
import com.zlh.he_ma_master.service.CarouselService;
import com.zlh.he_ma_master.service.GoodsInfoService;
import com.zlh.he_ma_master.service.IndexConfigService;
import com.zlh.he_ma_master.dao.IndexConfigMapper;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.RedisConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author lh
* @createDate 2022-04-02 21:03:31
*/
@Service
public class IndexConfigServiceImpl extends ServiceImpl<IndexConfigMapper, IndexConfig>
    implements IndexConfigService{

    @Resource
    private GoodsInfoService goodsInfoService;

    @Resource
    private CarouselService carouselService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    @Override
    public MallIndexInfoVO getConfigGoodsForIndex() {
        // 1.查询缓存
        String indexConfig = stringRedisTemplate.opsForValue().get(RedisConstants.MALL_INDEX_KEY);
        // 2.缓存没有数据，查询数据库并写入缓存
        if (!StringUtils.hasText(indexConfig)){
            MallIndexInfoVO index = getIndex();
            stringRedisTemplate.opsForValue().set(RedisConstants.MALL_INDEX_KEY, JSONUtil.toJsonStr(index), RedisConstants.MALL_INDEX_TTL, TimeUnit.HOURS);
            return index;
        }
        return JSONUtil.toBean(indexConfig, MallIndexInfoVO.class);
    }

    @Override
    public MallIndexInfoVO getIndex(){
        MallIndexInfoVO mallIndexInfoVO = new MallIndexInfoVO();
        List<MallIndexCarouselVO> carousel = carouselService.getCarouselForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<MallIndexConfigGoodsVO> hotGoodies = getIndexByType(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<MallIndexConfigGoodsVO> newGoodies = getIndexByType(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<MallIndexConfigGoodsVO> recommendGoodies = getIndexByType(IndexConfigTypeEnum.INDEX_GOODS_RECOMMEND.getType(), Constants.INDEX_GOODS_RECOMMEND_NUMBER);
        mallIndexInfoVO.setCarousels(carousel);
        mallIndexInfoVO.setHotGoodses(hotGoodies);
        mallIndexInfoVO.setNewGoodses(newGoodies);
        mallIndexInfoVO.setRecommendGoodses(recommendGoodies);
        return mallIndexInfoVO;
    }

    private List<MallIndexConfigGoodsVO> getIndexByType(int type, int indexGoodsHotNumber) {
        List<MallIndexConfigGoodsVO> mallIndexConfigGoodsVos = new ArrayList<>(indexGoodsHotNumber);
        Page<IndexConfig> pages = getPages(1, indexGoodsHotNumber, type);
        // 1. 获取对应配置类型的商品id
        List<Long> goodsId = pages.getRecords().stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
        // 2. 获取商品信息
        List<GoodsInfo> goodsInfos = goodsInfoService.listByIds(goodsId);
        goodsInfos.forEach(goodsInfo -> {
            MallIndexConfigGoodsVO indexConfigGoodsVO = new MallIndexConfigGoodsVO();
            BeanUtils.copyProperties(goodsInfo, indexConfigGoodsVO);
            // 商品名称过长
            if (indexConfigGoodsVO.getGoodName().length() > Constants.NAME_MAX_LENGTH){
                indexConfigGoodsVO.setGoodName(indexConfigGoodsVO.getGoodName().substring(0,30)+"...");
            }
            if (indexConfigGoodsVO.getGoodIntro().length() > Constants.NAME_MAX_LENGTH){
                indexConfigGoodsVO.setGoodIntro(indexConfigGoodsVO.getGoodIntro().substring(0,30)+"...");
            }
            mallIndexConfigGoodsVos.add(indexConfigGoodsVO);
        });
        return mallIndexConfigGoodsVos;
    }

    private IndexConfig copyProperties(Object obj){
        IndexConfig indexConfig = new IndexConfig();
        BeanUtils.copyProperties(obj, indexConfig);
        indexConfig.setCreateTime(new Date());
        return indexConfig;
    }
}




