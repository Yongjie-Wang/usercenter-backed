package com.wang.common;

/**
 * 全局错误码
 */
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    NO_AUTH(40100, "无权限", ""),
    NO_LOGIN(40101, "未登入", ""),
    NULL_ERROR(40100, "请求数据为空", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    SYSTEM_EXCEPTION(50000,"系统内部异常","");

    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述
     */
    private final String description;


    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
