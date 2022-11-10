package com.example.fengcommon.enums;

public enum EnumStatus {

    SUCCESS("0000","成功"),
    FAIL("9999","失败");

    private String code;

    private String message;

    EnumStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
