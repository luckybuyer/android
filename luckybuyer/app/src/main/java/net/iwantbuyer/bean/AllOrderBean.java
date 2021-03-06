package net.iwantbuyer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/9/23.
 */
public class AllOrderBean implements Serializable{


    /**
     * _resource : type of the resource
     * amount : 0
     * delivery : {"_resource":"type of the resource","address":{"_resource":"type of the resource","address":"string","created_at":"2016-10-08T01:41:31.255Z","id":0,"is_default":true,"name":"string","phone":"string","url":"string","zipcode":"string"},"created_at":"2016-10-08T01:41:31.255Z","id":0,"status":"processing","status_changes":[{"_resource":"type of the resource","created_at":"2016-10-08T01:41:31.255Z","status":"processing"}],"url":"string"}
     * game : {"_resource":"type of the resource","closed_at":"2016-10-08T01:41:31.255Z","finished_at":"2016-10-08T01:41:31.255Z","id":0,"issue_id":0,"left_shares":0,"lucky_number":"string","lucky_order":{"_resource":"type of the resource","created_at":"2016-10-08T01:41:31.255Z","id":0,"numbers":[0],"shares":0,"total_shares":"string","user":{"_resource":"type of the resource","id":0,"profile":{"locale":"string","name":"string","picture":"string","picture_large":"string","social_link":"string","timezone":"string"}}},"lucky_user":{"_resource":"type of the resource","id":0,"profile":{"locale":"string","name":"string","picture":"string","picture_large":"string","social_link":"string","timezone":"string"}},"num_orders":0,"product":{"_resource":"type of the resource","detail":"string","detail_image":"string","id":0,"title":"string","title_image":"string"},"result_countdown":0,"share_price":0,"shares":0,"start_at":"2016-10-08T01:41:31.256Z","status":"running","url":"string"}
     * id : 0
     * numbers : [0]
     * shares : 0
     * url : string
     */

    private List<AllorderBean> allorder;

    public List<AllorderBean> getAllorder() {
        return allorder;
    }

    public void setAllorder(List<AllorderBean> allorder) {
        this.allorder = allorder;
    }

    public static class AllorderBean implements Serializable{
        private String _resource;
        private int amount;
        /**
         * _resource : type of the resource
         * address : {"_resource":"type of the resource","address":"string","created_at":"2016-10-08T01:41:31.255Z","id":0,"is_default":true,"name":"string","phone":"string","url":"string","zipcode":"string"}
         * created_at : 2016-10-08T01:41:31.255Z
         * id : 0
         * status : processing
         * status_changes : [{"_resource":"type of the resource","created_at":"2016-10-08T01:41:31.255Z","status":"processing"}]
         * url : string
         */

        private DeliveryBean delivery;
        /**
         * _resource : type of the resource
         * closed_at : 2016-10-08T01:41:31.255Z
         * finished_at : 2016-10-08T01:41:31.255Z
         * id : 0
         * issue_id : 0
         * left_shares : 0
         * lucky_number : string
         * lucky_order : {"_resource":"type of the resource","created_at":"2016-10-08T01:41:31.255Z","id":0,"numbers":[0],"shares":0,"total_shares":"string","user":{"_resource":"type of the resource","id":0,"profile":{"locale":"string","name":"string","picture":"string","picture_large":"string","social_link":"string","timezone":"string"}}}
         * lucky_user : {"_resource":"type of the resource","id":0,"profile":{"locale":"string","name":"string","picture":"string","picture_large":"string","social_link":"string","timezone":"string"}}
         * num_orders : 0
         * product : {"_resource":"type of the resource","detail":"string","detail_image":"string","id":0,"title":"string","title_image":"string"}
         * result_countdown : 0
         * share_price : 0
         * shares : 0
         * start_at : 2016-10-08T01:41:31.256Z
         * status : running
         * url : string
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

        public static class DeliveryBean implements Serializable{
            private String _resource;
            /**
             * _resource : type of the resource
             * address : string
             * created_at : 2016-10-08T01:41:31.255Z
             * id : 0
             * is_default : true
             * name : string
             * phone : string
             * url : string
             * zipcode : string
             */

            private AddressBean address;
            private String created_at;
            private int id;
            private String status;
            private String url;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            private String type;
            /**
             * _resource : type of the resource
             * created_at : 2016-10-08T01:41:31.255Z
             * status : processing
             */

            private List<StatusChangesBean> status_changes;

            public String get_resource() {
                return _resource;
            }

            public void set_resource(String _resource) {
                this._resource = _resource;
            }

            public AddressBean getAddress() {
                return address;
            }

            public void setAddress(AddressBean address) {
                this.address = address;
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

            public List<StatusChangesBean> getStatus_changes() {
                return status_changes;
            }

            public void setStatus_changes(List<StatusChangesBean> status_changes) {
                this.status_changes = status_changes;
            }

            public static class AddressBean implements Serializable{
                private String _resource;
                private String address;
                private String created_at;
                private int id;
                private boolean is_default;
                private String name;
                private String phone;
                private String url;
                private String zipcode;

                public String get_resource() {
                    return _resource;
                }

                public void set_resource(String _resource) {
                    this._resource = _resource;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
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

                public boolean isIs_default() {
                    return is_default;
                }

                public void setIs_default(boolean is_default) {
                    this.is_default = is_default;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getZipcode() {
                    return zipcode;
                }

                public void setZipcode(String zipcode) {
                    this.zipcode = zipcode;
                }
            }

            public static class StatusChangesBean implements Serializable{
                private String _resource;
                private String created_at;
                private String status;

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

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
        }

        public static class GameBean implements Serializable{
            private String _resource;
            private int batch_id;
            private String closed_at;
            private String finished_at;
            private int id;
            private int issue_id;

            public int getBatch_id() {
                return batch_id;
            }

            public void setBatch_id(int batch_id) {
                this.batch_id = batch_id;
            }

            private int left_shares;
            private String lucky_number;
            /**
             * _resource : type of the resource
             * created_at : 2016-10-08T01:41:31.255Z
             * id : 0
             * numbers : [0]
             * shares : 0
             * total_shares : string
             * user : {"_resource":"type of the resource","id":0,"profile":{"locale":"string","name":"string","picture":"string","picture_large":"string","social_link":"string","timezone":"string"}}
             */

            private LuckyOrderBean lucky_order;
            /**
             * _resource : type of the resource
             * id : 0
             * profile : {"locale":"string","name":"string","picture":"string","picture_large":"string","social_link":"string","timezone":"string"}
             */

            private LuckyUserBean lucky_user;
            private int num_orders;
            /**
             * _resource : type of the resource
             * detail : string
             * detail_image : string
             * id : 0
             * title : string
             * title_image : string
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

            public double getNum_orders() {
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

            public static class LuckyOrderBean implements Serializable{
                private String _resource;
                private String created_at;
                private int id;
                private int shares;
                private String total_shares;
                /**
                 * _resource : type of the resource
                 * id : 0
                 * profile : {"locale":"string","name":"string","picture":"string","picture_large":"string","social_link":"string","timezone":"string"}
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

                public String getTotal_shares() {
                    return total_shares;
                }

                public void setTotal_shares(String total_shares) {
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

                public static class UserBean implements Serializable{
                    private String _resource;
                    private int id;
                    /**
                     * locale : string
                     * name : string
                     * picture : string
                     * picture_large : string
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

                    public static class ProfileBean implements Serializable{
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

            public static class LuckyUserBean implements Serializable{
                private String _resource;
                private int id;
                /**
                 * locale : string
                 * name : string
                 * picture : string
                 * picture_large : string
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

                public static class ProfileBean implements Serializable{
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

            public static class ProductBean implements Serializable{
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
