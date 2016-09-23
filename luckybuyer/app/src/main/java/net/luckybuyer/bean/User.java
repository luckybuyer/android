package net.luckybuyer.bean;

/**
 * Created by admin on 2016/9/23.
 */
public class User {

    /**
     * _resource : User
     * auth0_user_id : facebook|118093991983735
     * balance : 0
     * id : 3
     * profile : {"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F","picture_large":"https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}
     */

    private String _resource;
    private String auth0_user_id;
    private int balance;
    private int id;
    /**
     * locale : zh_CN
     * name : 杨树宇
     * picture : https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F
     * picture_large : https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg
     * social_link : https://www.facebook.com/app_scoped_user_id/118093991983735/
     * timezone : +08:00
     */

    private ProfileBean profile;

    public String get_resource() {
        return _resource;
    }

    public void set_resource(String _resource) {
        this._resource = _resource;
    }

    public String getAuth0_user_id() {
        return auth0_user_id;
    }

    public void setAuth0_user_id(String auth0_user_id) {
        this.auth0_user_id = auth0_user_id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProfileBean getProfile() {
        return profile;
    }

    public void setProfile(ProfileBean profile) {
        this.profile = profile;
    }

    public static class ProfileBean {
        private String locale;
        private String name;
        private String picture;
        private String picture_large;
        private String social_link;
        private String timezone;

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getPicture_large() {
            return picture_large;
        }

        public void setPicture_large(String picture_large) {
            this.picture_large = picture_large;
        }

        public String getSocial_link() {
            return social_link;
        }

        public void setSocial_link(String social_link) {
            this.social_link = social_link;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }
}
