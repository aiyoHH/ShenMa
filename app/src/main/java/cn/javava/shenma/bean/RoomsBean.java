package cn.javava.shenma.bean;

import java.util.List;

/**
 * Created by aiyoRui on 2018/1/24.
 */

public class RoomsBean {


    /**
     * code : 0
     * message : 成功
     * content : [{"id":"488b72b91ef111e88dbe00163e0887cf","channel_id":"0xb-0x1","state":"DeviceOnline","create_date":"2018-03-03 22:43:48"},{"id":"8645c15c1ef111e88dbe00163e0887cf","channel_id":"0xa-0x1","state":"DeviceOnline","create_date":"2018-03-03 22:45:32"}]
     * total_pages : 1
     * total_elements : 2
     * size : 10
     * page : 0
     */

    private int code;
    private String message;
    private int total_pages;
    private int total_elements;
    private int size;
    private int page;
    private List<ContentBean> content;

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

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_elements() {
        return total_elements;
    }

    public void setTotal_elements(int total_elements) {
        this.total_elements = total_elements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 488b72b91ef111e88dbe00163e0887cf
         * channel_id : 0xb-0x1
         * state : DeviceOnline
         * create_date : 2018-03-03 22:43:48
         */

        private String id;
        private String channel_id;
        private String state;
        private String create_date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(String channel_id) {
            this.channel_id = channel_id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }
    }
}
