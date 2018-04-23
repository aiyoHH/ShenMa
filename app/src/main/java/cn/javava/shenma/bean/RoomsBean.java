package cn.javava.shenma.bean;

import java.util.List;

/**
 * Created by aiyoRui on 2018/1/24.
 */

public class RoomsBean {


    /**
     * status : success
     * message : 返回数据成功
     * data : [{"id":3,"title":"主导航联系华智","balance":10,"kucun":1,"thumb":"http://weixin.javava.cn//jiayi/public/uploads/20180403\\6b971861d1341bb2517513d949e56b7f.png","is_zhong":0,"goods_id":3},{"id":4,"title":"主导航联系华智","balance":0,"kucun":1,"thumb":"http://weixin.javava.cn//jiayi/public/uploads/20180403\\7bc9b82fead45a641fa73bc4c1394b34.png","is_zhong":0,"goods_id":4}]
     */

    private String status;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * title : 主导航联系华智
         * balance : 10
         * kucun : 1
         * thumb : http://weixin.javava.cn//jiayi/public/uploads/20180403\6b971861d1341bb2517513d949e56b7f.png
         * is_zhong : 0
         * goods_id : 3
         */

        private int id;
        private String title;
        private int balance;
        private int kucun;
        private String thumb;
        private int is_zhong;
        private int goods_id;
        private String isdata;
        private int room_id;

        public int getRoom_id() {
            return room_id;
        }

        public void setRoom_id(int room_id) {
            this.room_id = room_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public int getKucun() {
            return kucun;
        }

        public void setKucun(int kucun) {
            this.kucun = kucun;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getIs_zhong() {
            return is_zhong;
        }

        public void setIs_zhong(int is_zhong) {
            this.is_zhong = is_zhong;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }
        public String getIsdata() {
            return isdata;
        }

        public void setIsdata(String isdata) {
            this.isdata = isdata;
        }
    }
}
