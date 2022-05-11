package com.zlh.he_ma_master.config.handler;

import com.zlh.he_ma_master.common.HeMaException;
import com.zlh.he_ma_master.common.ServiceResultEnum;
import com.zlh.he_ma_master.utils.Result;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 自定义异常处理器
 * @author lh
 */
@RestControllerAdvice
public class HeMaExceptionHandler {

    /**
     * todo: 异常处理状态码
     * 统一处理异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e){
        Result<String> result = new Result<>();
        result.setMessage(e.getMessage());
        result.setResultCode(500);
        //是否为自定义异常
        if (e instanceof HeMaException){
            //管理员未登录或token过期
            if (ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult().equals(e.getMessage())
                    || ServiceResultEnum.ADMIN_EXPIRE_ERROR.getResult().equals(e.getMessage())
                    || ServiceResultEnum.ADMIN_PASSWORD_ERROR.getResult().equals(e.getMessage())){
                result.setResultCode(419);
            }else if (ServiceResultEnum.MALL_USER_NOT_LOGIN_ERROR.getResult().equals(e.getMessage())
                    ||ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult().equals(e.getMessage())){
                result.setResultCode(416);
            }
            return result;
        }
        result.setMessage("未知异常,请查看控制台日志");
        e.printStackTrace();
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e ){
        Result<String> result = new Result<>();
        result.setResultCode(400);
        StringBuilder sb = new StringBuilder();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        result.setMessage(message);
        return result;
    }



}
