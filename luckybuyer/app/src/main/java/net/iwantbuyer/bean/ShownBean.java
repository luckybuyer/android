package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/12/12.
 */
public class ShownBean {


    private List<ShowBean> show;

    public List<ShowBean> getShow() {
        return show;
    }

    public void setShow(List<ShowBean> show) {
        this.show = show;
    }

    public static class ShowBean {
        /**
         * _resource : Post
         * content : hdhsh
         * created_at : 2017-03-31T10:50:52.584980+08:00
         * game_issue_id : 199
         * id : 82
         * images : ["//static-staging.luckybuyer.net/social/images/655452b8acd1411891e0151a550ccbb0.jpeg"]
         * product_title : 杨树宇来来来
         * user : {"_resource":"PublicUser","id":648,"profile":{"locale":null,"name":"杨树宇","picture":"https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png","social_link":"https://twitter.com/intent/user?user_id=818725636785119234","timezone":null}}
         */

        private String _resource;
        private String content;
        private String created_at;
        private int game_issue_id;
        private int id;
        private String product_title;
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

        public int getGame_issue_id() {
            return game_issue_id;
        }

        public void setGame_issue_id(int game_issue_id) {
            this.game_issue_id = game_issue_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProduct_title() {
            return product_title;
        }

        public void setProduct_title(String product_title) {
            this.product_title = product_title;
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
            /**
             * _resource : PublicUser
             * id : 648
             * profile : {"locale":null,"name":"杨树宇","picture":"https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png","social_link":"https://twitter.com/intent/user?user_id=818725636785119234","timezone":null}
             */

            private String _resource;
            private int id;
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
                /**
                 * locale : null
                 * name : 杨树宇
                 * picture : https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png
                 * social_link : https://twitter.com/intent/user?user_id=818725636785119234
                 * timezone : null
                 */

                private Object locale;
                private String name;
                private String picture;
                private String social_link;
                private Object timezone;

                public Object getLocale() {
                    return locale;
                }

                public void setLocale(Object locale) {
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

                public Object getTimezone() {
                    return timezone;
                }

                public void setTimezone(Object timezone) {
                    this.timezone = timezone;
                }
            }
        }
    }
}
