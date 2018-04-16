package cn.javava.shenma.bean;

import java.util.List;

/**
 * Created by aiyoRui on 2018/4/4.
 */

public class BannerBean {


    /**
     * status : success
     * message : 返回数据成功
     * data : [{"title":"主导航联系华智","url":"http://","thumb":"http://weixin.javava.cn//jiayi/public/uploads/20180403\\eb00bb43d2ae902262bbfb00a72c3ab9.png"},{"title":"主导航联系华智","url":"http://","thumb":"http://weixin.javava.cn//jiayi/public/uploads/20180403\\d628f05473f483c9e406568239cb2723.png"}]
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
         * title : 主导航联系华智
         * url : http://
         * thumb : http://weixin.javava.cn//jiayi/public/uploads/20180403\eb00bb43d2ae902262bbfb00a72c3ab9.png
         */

        private String title;
        private String url;
        private String thumb;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }
}
