package com.zlh.he_ma_master.utils;

/**
 * 响应工具类
 */
public class ResultGenerator {

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    /**
     * 生成响应成功结果
     * @return result
     */
    public static<T> Result<T> getSuccessResult(){
        Result<T> result = new Result<>();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }

    public static<T> Result<T> getSuccessResult(T data){
        Result<T> result = new Result<>();
        result.setResultCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    /**
     * 生成响应失败结果
     * @return result
     */
    public static<T> Result<T> getFailResult(){
        Result<T> result = new Result<>();
        result.setResultCode(RESULT_CODE_SERVER_ERROR);
        result.setMessage(DEFAULT_FAIL_MESSAGE);
        return result;
    }

    public static<T> Result<T> getFailResult(String message){
        Result<T> result = new Result<>();
        result.setResultCode(RESULT_CODE_SERVER_ERROR);
        result.setMessage(message);
        return result;
    }
}
