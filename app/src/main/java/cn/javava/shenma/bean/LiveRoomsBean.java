package cn.javava.shenma.bean;

import java.util.List;

/**
 * Created by aiyoRui on 2018/1/24.
 */

public class LiveRoomsBean {

    /**
     * content : [{"channelId":"WWJ_ZEGO_3275f295eab4","name":"dfdf","state":"DeviceOnline"}]
     * last : true
     * totalElements : 1
     * totalPages : 1
     * sort : dfdf
     * first : true
     * numberOfElements : 1
     * size : 20
     * number : 0
     */

    private boolean last;
    private int totalElements;
    private int totalPages;
    private String sort;
    private boolean first;
    private int numberOfElements;
    private int size;
    private int number;
    private List<ContentBean> content;

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * channelId : WWJ_ZEGO_3275f295eab4
         * name : dfdf
         * state : DeviceOnline
         */

        private String channelId;
        private String name;
        private String state;

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
    }
}
