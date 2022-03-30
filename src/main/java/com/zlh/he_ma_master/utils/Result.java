package com.zlh.he_ma_master.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 响应结果
 * @param <T>
 */
public class Result<T> implements Serializable {

    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    /**
     * 状态码
     */
    private int resultCode;

    public Result() {
    }

    public Result(String message, T data, int resultCode) {
        this.message = message;
        this.data = data;
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return Objects.equals(message, result.message) && Objects.equals(data, result.data) && Objects.equals(resultCode, result.resultCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, data, resultCode);
    }

    @Override
    public String toString() {
        return "Result{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", resultCode='" + resultCode + '\'' +
                '}';
    }
}
