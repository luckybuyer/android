package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/12/12.
 */
public class ShownBean {

    /**
     * _resource : Post
     * content : Shahs
     * created_at : 2016-12-09T10:15:53.583386+00:00
     * id : 12
     * images : ["//static-staging.luckybuyer.net/social/images/6f07a94df0af4dfab289deebaefdbd8d","//static-staging.luckybuyer.net/social/images/91acad33b8584ede9a9f57082e3852a0","//static-staging.luckybuyer.net/social/images/65cd8f4983374a13af179cf6e7a04fac.png"]
     * user : {"_resource":"PublicUser","id":10,"profile":{"locale":"en_US","name":"Carol","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/14517544_1831029820460827_2354002370786964344_n.jpg?oh=131e1afcecf169923111aa16634792c7&oe=58BAD4BA","social_link":"https://www.facebook.com/app_scoped_user_id/1830467243850418/","timezone":"+08:00"}}
     */

    private List<ShowBean> show;

    public List<ShowBean> getShow() {
        return show;
    }

    public void setShow(List<ShowBean> show) {
        this.show = show;
    }

    public static class ShowBean {
        private String _resource;
        private String content;
        private String created_at;
        private int id;
        /**
         * _resource : PublicUser
         * id : 10
         * profile : {"locale":"en_US","name":"Carol","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/14517544_1831029820460827_2354002370786964344_n.jpg?oh=131e1afcecf169923111aa16634792c7&oe=58BAD4BA","social_link":"https://www.facebook.com/app_scoped_user_id/1830467243850418/","timezone":"+08:00"}
         */

        private UserBean user;
        private List<String> images;

        public String get_resource() {
            return _resource;
        }

        public void set_resource(String _resource) {
            this._resource = _resource;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public static class UserBean {
            private String _resource;
            private int id;
            /**
             * locale : en_US
             * name : Carol
             * picture : https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/14517544_1831029820460827_2354002370786964344_n.jpg?oh=131e1afcecf169923111aa16634792c7&oe=58BAD4BA
             * social_link : https://www.facebook.com/app_scoped_user_id/1830467243850418/
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
    }
}
