package org.wrh.cloud.common.enums;

/**
 * @author wuruohong
 * @date 2022-06-17 9:48
 * 返回信息枚举类
 */
public enum ResultEnum {

    SUCCESS(0,"success"),
    FAIL(1000,"fail"),
    FILTER_ERROR(1001,"filter error"),
    SERVER_ERROR(1002,"server error"),
    FILTER_ERROR_RESP(1003,"filter-error-response");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
