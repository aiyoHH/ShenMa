package cn.javava.shenma.bean;

import java.util.List;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/10
 * Todo {TODO}.
 */

public class RoomO {

    /**
     * code : 0
     * data : {"room_list":[{"room_id":"WWJ_ZEGO_22d665075be0","room_name":"WWJ_ZEGO_22d665075be0","anchor_id_name":"WWJS_22d665075be0","anchor_nick_name":"WWJS_rk7130s_22d665075be0","stream_info":[{"stream_id":"WWJ_ZEGO_STREAM_22d665075be0"},{"stream_id":"WWJ_ZEGO_STREAM_22d665075be0_2"}]},{"room_id":"WWJ_ZEGO_32aafa7aafbd","room_name":"WWJ_ZEGO_32aafa7aafbd","anchor_id_name":"WWJS_32aafa7aafbd","anchor_nick_name":"WWJS_rk7130s_32aafa7aafbd","stream_info":[]},{"room_id":"WWJ_ZEGO_327e163ff5ff","room_name":"WWJ_ZEGO_327e163ff5ff","anchor_id_name":"WWJS_327e163ff5ff","anchor_nick_name":"WWJS_rk7130s_327e163ff5ff","stream_info":[{"stream_id":"WWJ_ZEGO_STREAM_327e163ff5ff"},{"stream_id":"WWJ_ZEGO_STREAM_327e163ff5ff_2"}]}]}
     * message : success
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private List<RoomListBean> room_list;

        public List<RoomListBean> getRoom_list() {
            return room_list;
        }

        public void setRoom_list(List<RoomListBean> room_list) {
            this.room_list = room_list;
        }

        public static class RoomListBean {
            /**
             * room_id : WWJ_ZEGO_22d665075be0
             * room_name : WWJ_ZEGO_22d665075be0
             * anchor_id_name : WWJS_22d665075be0
             * anchor_nick_name : WWJS_rk7130s_22d665075be0
             * stream_info : [{"stream_id":"WWJ_ZEGO_STREAM_22d665075be0"},{"stream_id":"WWJ_ZEGO_STREAM_22d665075be0_2"}]
             */

            private String room_id;
            private String room_name;
            private String anchor_id_name;
            private String anchor_nick_name;
            private List<StreamInfoBean> stream_info;

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getAnchor_id_name() {
                return anchor_id_name;
            }

            public void setAnchor_id_name(String anchor_id_name) {
                this.anchor_id_name = anchor_id_name;
            }

            public String getAnchor_nick_name() {
                return anchor_nick_name;
            }

            public void setAnchor_nick_name(String anchor_nick_name) {
                this.anchor_nick_name = anchor_nick_name;
            }

            public List<StreamInfoBean> getStream_info() {
                return stream_info;
            }

            public void setStream_info(List<StreamInfoBean> stream_info) {
                this.stream_info = stream_info;
            }

            public static class StreamInfoBean {
                /**
                 * stream_id : WWJ_ZEGO_STREAM_22d665075be0
                 */

                private String stream_id;

                public String getStream_id() {
                    return stream_id;
                }

                public void setStream_id(String stream_id) {
                    this.stream_id = stream_id;
                }
            }
        }
    }
}
