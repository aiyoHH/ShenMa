package cn.javava.shenma.bean;

/**
 * Created by aiyoRui on 2018/3/31.
 */

public class NoneDataBean {


    /**
     * status : success
     * message : 扣费成功
     * data : {"is_zhua":0,"balance":2}
     */

    private String status;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * is_zhua : 0
         * balance : 2
         */

        private int is_zhua;
        private int balance;
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }


        public int getIs_zhua() {
            return is_zhua;
        }

        public void setIs_zhua(int is_zhua) {
            this.is_zhua = is_zhua;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }
    }

    @Override
    public String toString() {
        return "NoneDataBean{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
