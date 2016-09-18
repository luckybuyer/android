package net.luckybuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/9/18.
 */
public class ProductOrderBean {

    /**
     * _resource : PublicGameOrder
     * created_at : 2016-09-04T12:52:18.821398+00:00
     * id : 16
     * numbers : [1000002]
     * shares : 1
     * user : {"_resource":"PublicUser","id":2,"profile":{"locale":"zh_CN","name":"Li Lang","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/10636222_717981571625434_7047602580994003042_n.jpg?oh=df449281f75a49345af0618dd9a4c638&oe=584C57EE","picture_large":"https://scontent.xx.fbcdn.net/v/t1.0-1/10636222_717981571625434_7047602580994003042_n.jpg?oh=0861b2f2c37df2c1d123d7ed650e4757&oe=58445ED4","social_link":"https://www.facebook.com/app_scoped_user_id/1068893079867613/","timezone":"+08:00"}}
     */

    private List<ProductorderBean> productorder;

    public List<ProductorderBean> getProductorder() {
        return productorder;
    }

    public void setProductorder(List<ProductorderBean> productorder) {
        this.productorder = productorder;
    }

    public static class ProductorderBean {
        private String _resource;
        private String created_at;
        private int id;
        private int shares;
        /**
         * _resource : PublicUser
         * id : 2
         * profile : {"locale":"zh_CN","name":"Li Lang","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/10636222_717981571625434_7047602580994003042_n.jpg?oh=df449281f75a49345af0618dd9a4c638&oe=584C57EE","picture_large":"https://scontent.xx.fbcdn.net/v/t1.0-1/10636222_717981571625434_7047602580994003042_n.jpg?oh=0861b2f2c37df2c1d123d7ed650e4757&oe=58445ED4","social_link":"https://www.facebook.com/app_scoped_user_id/1068893079867613/","timezone":"+08:00"}
         */

        private UserBean user;
        private List<Integer> numbers;

        public String get_resource() {
            return _resource;
        }

        public void set_resource(String _resource) {
            this._resource = _resource;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getShares() {
            return shares;
        }

        public void setShares(int shares) {
            this.shares = shares;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<Integer> numbers) {
            this.numbers = numbers;
        }

        public static class UserBean {
            private String _resource;
            private int id;
            /**
             * locale : zh_CN
             * name : Li Lang
             * picture : https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/10636222_717981571625434_7047602580994003042_n.jpg?oh=df449281f75a49345af0618dd9a4c638&oe=584C57EE
             * picture_large : https://scontent.xx.fbcdn.net/v/t1.0-1/10636222_717981571625434_7047602580994003042_n.jpg?oh=0861b2f2c37df2c1d123d7ed650e4757&oe=58445ED4
             * social_link : https://www.facebook.com/app_scoped_user_id/1068893079867613/
             * timezone : +08:00
             */

            private ProfileBean profile;

            public String get_resource() {
                return _resource;
            }

            public void set_resource(String _resource) {
                this._resource = _resource;
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
    }
}
