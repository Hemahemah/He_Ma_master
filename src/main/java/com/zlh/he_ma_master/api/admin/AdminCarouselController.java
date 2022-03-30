package com.zlh.he_ma_master.api.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlh.he_ma_master.api.admin.param.CarouselAddParam;
import com.zlh.he_ma_master.api.admin.param.BatchIdParam;
import com.zlh.he_ma_master.api.admin.param.CarouselEditParam;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.entity.Carousel;
import com.zlh.he_ma_master.service.CarouselService;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/he_ma")
public class AdminCarouselController {

    @Resource
    private CarouselService carouselService;

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @GetMapping("/carousels")
    public Result<Page<Carousel>> getCarouselPage(@RequestParam @Valid Integer pageNumber,
                                                  @RequestParam @Valid Integer pageSize,
                                                  @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser Carousel api, adminUserId={}",adminUserToken.getUserId());
        Page<Carousel> carouselPage = carouselService.getCarouselPage(pageNumber, pageSize);
        if (carouselPage != null){
           return ResultGenerator.getSuccessResult(carouselPage);
        }
        return ResultGenerator.getFailResult();
    }

    @PostMapping("/carousels")
    public Result addCarousel(@RequestBody @Valid CarouselAddParam carouselAddParam,
                              @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser Carousel api, adminUserId={}",adminUserToken.getUserId());
        if (carouselService.saveCarousels(carouselAddParam)){
            return ResultGenerator.getSuccessResult();
        }
        return ResultGenerator.getFailResult();
    }

    @GetMapping("/carousels/{id}")
    public Result<Carousel> getDetail(@PathVariable Integer id,
                                      @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser Carousel api, adminUserId={}",adminUserToken.getUserId());
        Carousel carousel = carouselService.getCarousel(id);
        if (carousel != null){
          return ResultGenerator.getSuccessResult(carousel);
        }
        return ResultGenerator.getFailResult();
    }

    @PutMapping("/carousels")
    public Result updateCarousel(@RequestBody CarouselEditParam carouselEditParam,
                                 @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser Carousel api, adminUserId={}",adminUserToken.getUserId());
        if (carouselService.updateCarousel(carouselEditParam)){
            return ResultGenerator.getSuccessResult();
        }
        return ResultGenerator.getFailResult();
    }

    @DeleteMapping("/carousels")
    public Result deleteCarousel(@RequestBody BatchIdParam delParam,
                                 @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser Carousel api, adminUserId={}",adminUserToken.getUserId());
        //判断id合法性
        if (delParam.getIds() == null || delParam.getIds().length <1){
            return ResultGenerator.getFailResult(ServiceResultEnum.PARAM_EXCEPTION.getResult());
        }
        if (carouselService.deleteCarousel(delParam.getIds())){
            return ResultGenerator.getSuccessResult();
        }else {
            return ResultGenerator.getFailResult();
        }
    }

}
