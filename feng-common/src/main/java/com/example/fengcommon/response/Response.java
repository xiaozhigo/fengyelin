package com.example.fengcommon.response;

import com.example.fengcommon.enums.EnumStatus;
import java.io.Serializable;

/**
 * @author administrator
 * @Title:Response
 * @Description:响应
 * @date 2017/01/24
 */

public class Response implements Serializable {

    private static final long serialVersionUID = 3780592159301270531L;

    private String returnCode;

    private String returnMessage;

    private Object object;

    public Response() {
        this.returnCode = EnumStatus.SUCCESS.getCode();
        this.returnMessage = EnumStatus.SUCCESS.getMessage();
    }

    @Override
    public String toString() {
        return "Response{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                ", object=" + object +
                '}';
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isSuccee() {
        if (EnumStatus.SUCCESS.getCode().equals(this.returnCode)) {
            return true;
        }
        return false;
    }


    public void setCodeAndMessage(String returnCode, String returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
    }

    public void setCodeAndMessage(EnumStatus enumStatus) {
        this.returnCode = enumStatus.getCode();
        this.returnMessage = enumStatus.getMessage();
    }

    public static Response error(String returnCode, String returnMessage) {
        Response response = new Response();
        response.returnCode = returnCode;
        response.returnMessage = returnMessage;
        return response;
    }
}
