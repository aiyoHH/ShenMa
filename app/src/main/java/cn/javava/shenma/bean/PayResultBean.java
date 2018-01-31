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
     * codeUrl : weixin://wxpay/bizpayurl?pr=Qr7OY7y
     * tradeNo : 20180131103390236
     */

    private String codeUrl;
    private String tradeNo;

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
