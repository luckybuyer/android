package net.iwantbuyer.bean;

/**
 * Created by admin on 2016/9/23.
 */
public class User {

    /**
     * _resource : type of the resource
     * auth0_user_id : string
     * balance : 0
     * has_given_new_user_gift : true
     * has_topup : true
     * id : 0
     * profile : {"locale":"string","name":"string","picture":"string","social_link":"string","timezone":"string"}
     */

    private String _resource;
    private String auth0_user_id;
    private int balance;
    private boolean has_given_new_user_gift;
    private boolean has_topup;
    private int id;
    /**
     * locale : string
     * name : string
     * picture : string
     * social_link : string
     * timezone : string
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

    public boolean isHas_given_new_user_gift() {
        return has_given_new_user_gift;
    }

    public void setHas_given_new_user_gift(boolean has_given_new_user_gift) {
        this.has_given_new_user_gift = has_given_new_user_gift;
    }

    public boolean isHas_topup() {
        return has_topup;
    }

    public void setHas_topup(boolean has_topup) {
        this.has_topup = has_topup;
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
