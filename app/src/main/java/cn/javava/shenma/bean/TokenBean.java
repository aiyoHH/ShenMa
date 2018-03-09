package cn.javava.shenma.bean;

/**
 * Created by aiyoRui on 2018/3/6.
 */

public class TokenBean {

    /**
     * code : 0
     * message : 成功
     * expires_in : 7200
     * access_token : c
     */

    private int code;
    private String message;
    private int expires_in;
    private String access_token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
