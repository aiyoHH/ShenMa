package cn.javava.shenma.bean;

/**
 * Created by aiyoRui on 2018/3/6.
 */

public class TokenBean {


    /**
     * access_token : fSHgYeeGvBNvYGbszOOa7eswo_8dlHStJgol0x7Q4__LCKnvaFLGOhYWJ4K_w20QmdGpCaOZnZUcYIUEROi-CWAA8JPv_40jy66_FnjJ338MM9KAUl4KZIdZvVfZioHM
     * token_type : bearer
     * expires_in : 42970
     * scope : all
     * code : 0
     * message : 成功
     */

    private String access_token;
    private String token_type;
    private int expires_in;
    private String scope;
    private int code;
    private String message;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

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
}
