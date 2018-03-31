package cn.javava.shenma.bean;

/**
 * Created by aiyoRui on 2018/1/24.
 */

public class UserInfoBean {


    /**
     * status : success
     * message : 游戏开始
     * data : {"open_id":"oHqvmw_aKcumJ7KkzMQYmg5ZC6I8","member_id":10,"token":"deb514303972768ca6572ad9a116ce5ef439faaf","balance":215,"profile_photo":"http://thirdwx.qlogo.cn/mmopen/Q3auHgzwzM7tBG0y1OgMMAaaOlU3nOBVCWvMxZ9InpbEtz0r2D28wlZC09CEHYwpgnGH6ibcPRk3uJPibaadqBAXC5CyGNSgSjicqqXpQzdcGU/132","sex":1,"login_time":"18-03-29 22:34:55"}
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
         * open_id : oHqvmw_aKcumJ7KkzMQYmg5ZC6I8
         * member_id : 10
         * name:xxx
         * token : deb514303972768ca6572ad9a116ce5ef439faaf
         * balance : 215
         * profile_photo : http://thirdwx.qlogo.cn/mmopen/Q3auHgzwzM7tBG0y1OgMMAaaOlU3nOBVCWvMxZ9InpbEtz0r2D28wlZC09CEHYwpgnGH6ibcPRk3uJPibaadqBAXC5CyGNSgSjicqqXpQzdcGU/132
         * sex : 1
         * login_time : 18-03-29 22:34:55
         */

        private String open_id;
        private int member_id;
        private String token;
        private int balance;
        private String profile_photo;
        private int sex;
        private String login_time;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }



        public String getOpen_id() {
            return open_id;
        }

        public void setOpen_id(String open_id) {
            this.open_id = open_id;
        }

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public String getProfile_photo() {
            return profile_photo;
        }

        public void setProfile_photo(String profile_photo) {
            this.profile_photo = profile_photo;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }
    }
}
