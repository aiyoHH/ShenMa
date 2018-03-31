package cn.javava.shenma.bean;

import java.util.List;

/**
 * Created by aiyoRui on 2018/3/31.
 */

public class NoneDataBean {


    /**
     * status : success
     * message : 退出游戏成功
     * data : []
     */

    private String status;
    private String message;
    private List<?> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
