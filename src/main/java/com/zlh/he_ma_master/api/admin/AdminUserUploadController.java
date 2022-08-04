package com.zlh.he_ma_master.api.admin;

import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * @author lh
 */
@RestController
@RequestMapping("/he_ma")
public class AdminUserUploadController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserUploadController.class);

    @Value("${constants.fileUploadDic}")
    private String fileUploadDic;

    @Value("${constants.goodImgDic}")
    private String goodImgDic;

    @PostMapping("/upload/file/{meg}")
    public Result<URI> uploadFile(@RequestParam("file") MultipartFile file,
                             @TokenToAdminUser AdminUserToken adminUserToken,
                             @PathVariable String meg){
        logger.info("upload file api,adminUer={}",adminUserToken.getUserId());
        String filename = file.getOriginalFilename();
        String suffixName = Objects.requireNonNull(filename).substring(filename.lastIndexOf("."));
        //生成文件名称
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        String newFileName = sdf.format(new Date()) + r.nextInt(100) + suffixName;
        File fileDirectory = new File("car".equals(meg)?fileUploadDic:goodImgDic);
        File destFile = new File("car".equals(meg)?(fileUploadDic + newFileName):(goodImgDic + newFileName));
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            URI uri = new URI("car".equals(meg)?("/images/"+newFileName):("/goods-img/"+newFileName));
            return ResultGenerator.getSuccessResult(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            Result<URI> failResult = ResultGenerator.getFailResult();
            failResult.setMessage(ServiceResultEnum.FILE_UPLOAD_ERROR.getResult());
            return failResult;
        }
    }

}
