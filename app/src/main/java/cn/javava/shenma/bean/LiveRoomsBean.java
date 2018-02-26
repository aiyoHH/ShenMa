package cn.javava.shenma.bean;

import java.util.List;

/**
 * Created by aiyoRui on 2018/1/24.
 */

public class LiveRoomsBean {


    /**
     * code : 0
     * message : 成功
     * data : {"content":[],"last":true,"totalElements":0,"totalPages":0,"sort":null,"first":true,"numberOfElements":0,"size":20,"number":0}
     */

    private int code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content : []
         * last : true
         * totalElements : 0
         * totalPages : 0
         * sort : null
         * first : true
         * numberOfElements : 0
         * size : 20
         * number : 0
         */

        private boolean last;
        private String totalElements;
        private String totalPages;
        private Object sort;
        private boolean first;
        private String numberOfElements;
        private String size;
        private String number;
        private List<ContentBean> content;

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public String getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(String totalElements) {
            this.totalElements = totalElements;
        }

        public String getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(String totalPages) {
            this.totalPages = totalPages;
        }

        public Object getSort() {
            return sort;
        }

        public void setSort(Object sort) {
            this.sort = sort;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public String getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(String numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean{
            public String getChannelId() {
                return channelId;
            }

            public void setChannelId(String channelId) {
                this.channelId = channelId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            private String channelId;
            private String name;
            private String   state;
        }
    }
}
