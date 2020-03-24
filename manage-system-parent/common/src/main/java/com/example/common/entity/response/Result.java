package com.example.common.entity.response;

/**
 * 返回结果实体类
 */
public class Result<T> {

    private boolean flag;//是否成功
    private Integer code;//返回码
    private String message;//返回消息
    private T data;//返回数据

    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = (T)data;
    }

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result() {
        this.flag = true;
        this.code = StatusCode.OK;
        this.message = "执行成功";
    }

    public static Result ok(String message) {
        Result result = new Result();
        result.message = message;
        return result;
    }

    public static Result ok(String message,Object data) {
        Result result = new Result();
        result.message = message;
        result.data = data;
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.flag = false;
        result.message = message;
        return result;
    }

    public static Result error(Integer code,String message) {
        Result result = new Result();
        result.flag = false;
        result.code = code;
        result.message = message;
        return result;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
}
