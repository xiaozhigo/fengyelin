package com.example.fengcommon.enums;

/**
 * <p>
 * 状态码封装
 * </p>
 *
 * @author hyj
 */
public enum Status {
    /**
     * 操作成功
     */
    OK(0, "操作成功"),

    /**
     * 未知异常
     */
    ERROR(500, "服务器出错啦"),

    REPEAT_SUBMIT(00001, "重复请求");
    /**
     * 状态码
     */
    private int code;
    /**
     * 内容
     */
    private String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
