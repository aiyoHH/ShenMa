package cn.javava.shenma.bean;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/31
 * Todo {TODO}.
 */

public class PayResultBean {


    /**
     * code : 0
     * code_url : weixin://wxpay/bizpayurl?pr=5RafV6F
     * trade_no : 20180365060455256
     * message : 成功
     */

    private int code;
    private String code_url;
    private String trade_no;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
