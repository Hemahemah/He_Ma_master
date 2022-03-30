package com.zlh.he_ma_master.api.admin;

import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.config.annotation.TokenToAdminUser;
import com.zlh.he_ma_master.entity.AdminUserToken;
import com.zlh.he_ma_master.utils.Constants;
import com.zlh.he_ma_master.utils.Result;
import com.zlh.he_ma_master.utils.ResultGenerator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/he_ma")
public class AdminUserUploadController {


    @PostMapping("/upload/file/{meg}")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @TokenToAdminUser AdminUserToken adminUserToken,
                             @PathVariable String meg,
                             HttpServletRequest request){
        String filename = file.getOriginalFilename();
        String suffixName = Objects.requireNonNull(filename).substring(filename.lastIndexOf("."));
        //生成文件名称
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        String newFileName = sdf.format(new Date()) + r.nextInt(100) + suffixName;
        File fileDirectory = new File(meg.equals("car")?Constants.FILE_UPLOAD_DIC:Constants.GOOD_IMG_DIC);
        File destFile = new File(meg.equals("car")?(Constants.FILE_UPLOAD_DIC + newFileName):(Constants.GOOD_IMG_DIC + newFileName));
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            String url = request.getRequestURL().substring(0,request.getRequestURL().lastIndexOf("/he_ma"));
            URI uri = new URI(meg.equals("car")?(url+"/images/"+newFileName):(url+"/he_ma/goods-img/"+newFileName));
            return ResultGenerator.getSuccessResult(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            Result failResult = ResultGenerator.getFailResult();
            failResult.setMessage(ServiceResultEnum.FILE_UPLOAD_ERROR.getResult());
            return failResult;
        }
    }
}
