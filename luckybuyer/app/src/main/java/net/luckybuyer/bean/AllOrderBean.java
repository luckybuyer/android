package net.luckybuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/9/23.
 */
public class AllOrderBean {


    /**
     * _resource : GameOrder
     * amount : 8
     * delivery : {"_resource":"Delivery","created_at":"2016-09-23T08:44:24.546219+00:00","id":4,"status":"pending","status_changes":[],"url":"https://api-staging.luckybuyer.net/v1/deliveries/4"}
     * game : {"_resource":"Game","batch_id":8,"closed_at":"2016-09-23T08:44:24.397675+00:00","finished_at":"2016-09-23T08:45:24.397675+00:00","id":10,"issue_id":1,"left_shares":0,"lucky_number":"1000001","lucky_order":{"_resource":"WinnerGameOrder","created_at":"2016-09-23T08:44:24.390935+00:00","id":31,"numbers":[1000001],"shares":1,"total_shares":1,"user":{"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F","picture_large":"https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}}},"lucky_user":{"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F","picture_large":"https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}},"num_orders":2,"product":{"_resource":"Product","detail":"Japan box type student school bag","detail_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/7-2.jpg","id":8,"title":"Japan box type student school bag","title_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/3-2.jpg"},"result_countdown":60,"share_price":8,"shares":2,"start_at":"2016-09-20T05:33:08+00:00","status":"finished","url":"https://api-staging.luckybuyer.net/v1/games/10"}
     * id : 31
     * numbers : [1000001]
     * shares : 1
     * url : https://api-staging.luckybuyer.net/v1/game-orders/31
     */

    private List<AllorderBean> allorder;

    public List<AllorderBean> getAllorder() {
        return allorder;
    }

    public void setAllorder(List<AllorderBean> allorder) {
        this.allorder = allorder;
    }

    public static class AllorderBean {
        private String _resource;
        private int amount;
        /**
         * _resource : Delivery
         * created_at : 2016-09-23T08:44:24.546219+00:00
         * id : 4
         * status : pending
         * status_changes : []
         * url : https://api-staging.luckybuyer.net/v1/deliveries/4
         */

        private DeliveryBean delivery;
        /**
         * _resource : Game
         * batch_id : 8
         * closed_at : 2016-09-23T08:44:24.397675+00:00
         * finished_at : 2016-09-23T08:45:24.397675+00:00
         * id : 10
         * issue_id : 1
         * left_shares : 0
         * lucky_number : 1000001
         * lucky_order : {"_resource":"WinnerGameOrder","created_at":"2016-09-23T08:44:24.390935+00:00","id":31,"numbers":[1000001],"shares":1,"total_shares":1,"user":{"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F","picture_large":"https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}}}
         * lucky_user : {"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F","picture_large":"https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}}
         * num_orders : 2
         * product : {"_resource":"Product","detail":"Japan box type student school bag","detail_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/7-2.jpg","id":8,"title":"Japan box type student school bag","title_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/3-2.jpg"}
         * result_countdown : 60
         * share_price : 8
         * shares : 2
         * start_at : 2016-09-20T05:33:08+00:00
         * status : finished
         * url : https://api-staging.luckybuyer.net/v1/games/10
         */

        private GameBean game;
        private int id;
        private int shares;
        private String url;
        private List<Integer> numbers;

        public String get_resource() {
            return _resource;
        }

        public void set_resource(String _resource) {
            this._resource = _resource;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public DeliveryBean getDelivery() {
            return delivery;
        }

        public void setDelivery(DeliveryBean delivery) {
            this.delivery = delivery;
        }

        public GameBean getGame() {
            return game;
        }

        public void setGame(GameBean game) {
            this.game = game;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<Integer> numbers) {
            this.numbers = numbers;
        }

        public static class DeliveryBean {
            private String _resource;
            private String created_at;
            private int id;
            private String status;
            private String url;
            private List<?> status_changes;

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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public List<?> getStatus_changes() {
                return status_changes;
            }

            public void setStatus_changes(List<?> status_changes) {
                this.status_changes = status_changes;
            }
        }

        public static class GameBean {
            private String _resource;
            private int batch_id;
            private String closed_at;
            private String finished_at;
            private int id;
            private int issue_id;
            private int left_shares;
            private String lucky_number;
            /**
             * _resource : WinnerGameOrder
             * created_at : 2016-09-23T08:44:24.390935+00:00
             * id : 31
             * numbers : [1000001]
             * shares : 1
             * total_shares : 1
             * user : {"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F","picture_large":"https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}}
             */

            private LuckyOrderBean lucky_order;
            /**
             * _resource : PublicUser
             * id : 3
             * profile : {"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F","picture_large":"https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}
             */

            private LuckyUserBean lucky_user;
            private int num_orders;
            /**
             * _resource : Product
             * detail : Japan box type student school bag
             * detail_image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/7-2.jpg
             * id : 8
             * title : Japan box type student school bag
             * title_image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/3-2.jpg
             */

            private ProductBean product;
            private int result_countdown;
            private int share_price;
            private int shares;
            private String start_at;
            private String status;
            private String url;

            public String get_resource() {
                return _resource;
            }

            public void set_resource(String _resource) {
                this._resource = _resource;
            }

            public int getBatch_id() {
                return batch_id;
            }

            public void setBatch_id(int batch_id) {
                this.batch_id = batch_id;
            }

            public String getClosed_at() {
                return closed_at;
            }

            public void setClosed_at(String closed_at) {
                this.closed_at = closed_at;
            }

            public String getFinished_at() {
                return finished_at;
            }

            public void setFinished_at(String finished_at) {
                this.finished_at = finished_at;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIssue_id() {
                return issue_id;
            }

            public void setIssue_id(int issue_id) {
                this.issue_id = issue_id;
            }

            public int getLeft_shares() {
                return left_shares;
            }

            public void setLeft_shares(int left_shares) {
                this.left_shares = left_shares;
            }

            public String getLucky_number() {
                return lucky_number;
            }

            public void setLucky_number(String lucky_number) {
                this.lucky_number = lucky_number;
            }

            public LuckyOrderBean getLucky_order() {
                return lucky_order;
            }

            public void setLucky_order(LuckyOrderBean lucky_order) {
                this.lucky_order = lucky_order;
            }

            public LuckyUserBean getLucky_user() {
                return lucky_user;
            }

            public void setLucky_user(LuckyUserBean lucky_user) {
                this.lucky_user = lucky_user;
            }

            public int getNum_orders() {
                return num_orders;
            }

            public void setNum_orders(int num_orders) {
                this.num_orders = num_orders;
            }

            public ProductBean getProduct() {
                return product;
            }

            public void setProduct(ProductBean product) {
                this.product = product;
            }

            public int getResult_countdown() {
                return result_countdown;
            }

            public void setResult_countdown(int result_countdown) {
                this.result_countdown = result_countdown;
            }

            public int getShare_price() {
                return share_price;
            }

            public void setShare_price(int share_price) {
                this.share_price = share_price;
            }

            public int getShares() {
                return shares;
            }

            public void setShares(int shares) {
                this.shares = shares;
            }

            public String getStart_at() {
                return start_at;
            }

            public void setStart_at(String start_at) {
                this.start_at = start_at;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public static class LuckyOrderBean {
                private String _resource;
                private String created_at;
                private int id;
                private int shares;
                private int total_shares;
                /**
                 * _resource : PublicUser
                 * id : 3
                 * profile : {"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=b1d33c3a4d6d57cbfe57dbee85ceef4b&oe=5873202F","picture_large":"https://scontent.xx.fbcdn.net/t31.0-1/10506738_10150004552801856_220367501106153455_o.jpg","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}
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

                public int getTotal_shares() {
                    return total_shares;
                }

                public void setTotal_shares(int total_shares) {
                    this.total_shares = total_shares;
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

            public static class LuckyUserBean {
                private String _resource;
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

            public static class ProductBean {
                private String _resource;
                private String detail;
                private String detail_image;
                private int id;
                private String title;
                private String title_image;

                public String get_resource() {
                    return _resource;
                }

                public void set_resource(String _resource) {
                    this._resource = _resource;
                }

                public String getDetail() {
                    return detail;
                }

                public void setDetail(String detail) {
                    this.detail = detail;
                }

                public String getDetail_image() {
                    return detail_image;
                }

                public void setDetail_image(String detail_image) {
                    this.detail_image = detail_image;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getTitle_image() {
                    return title_image;
                }

                public void setTitle_image(String title_image) {
                    this.title_image = title_image;
                }
            }
        }
    }
}
