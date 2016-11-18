package net.smartbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/10/21.
 */
public class DispatchGameBean {


    /**
     * _resource : GameOrder
     * amount : 59.0
     * delivery : {"_resource":"Delivery","address":{"address":"addressssssssssssssss","name":"hydraZty","phone":"18181818181818","zipcode":null},"created_at":"2016-10-20T14:41:15.351569+08:00","id":620,"status":"pending","status_changes":[],"url":"https://api-staging.luckybuyer.net/v1/deliveries/620"}
     * game : {"_resource":"Game","batch_id":15,"closed_at":"2016-10-20T14:41:15.338885+08:00","finished_at":"2016-10-20T14:42:15.338885+08:00","id":624,"issue_id":234,"left_shares":0,"lucky_number":"1000055","lucky_order":{"_resource":"WinnerGameOrder","created_at":"2016-10-20T13:44:39.599264+08:00","id":2457,"numbers":[1000002,1000003,1000004,1000006,1000007,1000008,1000009,1000010,1000013],"shares":9,"total_shares":9,"user":{"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}}},"lucky_user":{"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}},"num_orders":2,"product":{"_resource":"Product","detail":"<span style=\"display:inline-block;width:4px;height:12px;border-radius:2px;background-color:#EC5330;margin-left:-10px;margin-right:8px\"><\/span><span style=\"font-size:1.3em;color:#333;line-height:1.8em;\">Item Information:<\/span>\r\n<table style=\"color:#666;\">\r\n    <tbody>\r\n    <tr>\r\n        <td>Brand: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/td>\r\n        <td>Jaguar<\/td>\r\n    <\/tr>\r\n    <tr>\r\n        <td>Size: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/td>\r\n        <td>100 ml<\/td>\r\n    <\/tr>\r\n    <tr>\r\n        <td>Targeted Group: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/td>\r\n        <td>Men<\/td>\r\n    <\/tr>\r\n    <\/tbody>\r\n<\/table>\r\n\r\n<span style=\"display:inline-block;width:4px;height:12px;border-radius:2px;background-color:#EC5330;margin-left:-10px;margin-right:8px\"><\/span><span style=\"font-size:1.3em;color:#333;line-height:1.8em;\">How to:<\/span>\r\n<span style=\"line-height: 1.35em;color:#666;\">1. Play 1 coin, get one chance!<\/span>\r\n<span style=\"line-height: 1.35em;color:#666;\">2. Anybody can win an item when their number matches the lucky code.<\/span>\r\n<span style=\"line-height: 1.35em;color:#666;\">3. Buy coins , which are used to win the lucky items.<\/span>.","detail_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/fb8bfbde8681422b94106cce3934cdde","id":11,"title":"Classic Black by Jaguar for Men - Eau de Toilette, 100ml. Now it is just for $1 !","title_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/578783f6f0e34b63ab1a9144ff7349a4"},"result_countdown":60,"share_price":1,"shares":13,"start_at":"2016-10-20T13:36:08.349770+08:00","status":"finished","url":"https://api-staging.luckybuyer.net/v1/games/618"}
     * id : 2457
     * numbers : [1000002,1000003,1000004,1000006,1000007,1000008,1000009,1000010,1000013]
     * shares : 9
     * url : https://api-staging.luckybuyer.net/v1/game-orders/2457
     */

    private String _resource;
    private double amount;
    /**
     * _resource : Delivery
     * address : {"address":"addressssssssssssssss","name":"hydraZty","phone":"18181818181818","zipcode":null}
     * created_at : 2016-10-20T14:41:15.351569+08:00
     * id : 620
     * status : pending
     * status_changes : []
     * url : https://api-staging.luckybuyer.net/v1/deliveries/620
     */

    private DeliveryBean delivery;
    /**
     * _resource : Game
     * batch_id : 15
     * closed_at : 2016-10-20T14:41:15.338885+08:00
     * finished_at : 2016-10-20T14:42:15.338885+08:00
     * id : 624
     * issue_id : 234
     * left_shares : 0
     * lucky_number : 1000055
     * lucky_order : {"_resource":"WinnerGameOrder","created_at":"2016-10-20T13:44:39.599264+08:00","id":2457,"numbers":[1000002,1000003,1000004,1000006,1000007,1000008,1000009,1000010,1000013],"shares":9,"total_shares":9,"user":{"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}}}
     * lucky_user : {"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}}
     * num_orders : 2
     * product : {"_resource":"Product","detail":"<span style=\"display:inline-block;width:4px;height:12px;border-radius:2px;background-color:#EC5330;margin-left:-10px;margin-right:8px\"><\/span><span style=\"font-size:1.3em;color:#333;line-height:1.8em;\">Item Information:<\/span>\r\n<table style=\"color:#666;\">\r\n    <tbody>\r\n    <tr>\r\n        <td>Brand: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/td>\r\n        <td>Jaguar<\/td>\r\n    <\/tr>\r\n    <tr>\r\n        <td>Size: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/td>\r\n        <td>100 ml<\/td>\r\n    <\/tr>\r\n    <tr>\r\n        <td>Targeted Group: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/td>\r\n        <td>Men<\/td>\r\n    <\/tr>\r\n    <\/tbody>\r\n<\/table>\r\n\r\n<span style=\"display:inline-block;width:4px;height:12px;border-radius:2px;background-color:#EC5330;margin-left:-10px;margin-right:8px\"><\/span><span style=\"font-size:1.3em;color:#333;line-height:1.8em;\">How to:<\/span>\r\n<span style=\"line-height: 1.35em;color:#666;\">1. Play 1 coin, get one chance!<\/span>\r\n<span style=\"line-height: 1.35em;color:#666;\">2. Anybody can win an item when their number matches the lucky code.<\/span>\r\n<span style=\"line-height: 1.35em;color:#666;\">3. Buy coins , which are used to win the lucky items.<\/span>.","detail_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/fb8bfbde8681422b94106cce3934cdde","id":11,"title":"Classic Black by Jaguar for Men - Eau de Toilette, 100ml. Now it is just for $1 !","title_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/578783f6f0e34b63ab1a9144ff7349a4"}
     * result_countdown : 60
     * share_price : 1.0
     * shares : 13
     * start_at : 2016-10-20T13:36:08.349770+08:00
     * status : finished
     * url : https://api-staging.luckybuyer.net/v1/games/618
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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
        /**
         * address : addressssssssssssssss
         * name : hydraZty
         * phone : 18181818181818
         * zipcode : null
         */

        private AddressBean address;
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

        public List<?> getStatus_changes() {
            return status_changes;
        }

        public void setStatus_changes(List<?> status_changes) {
            this.status_changes = status_changes;
        }

        public static class AddressBean {
            private String address;
            private String name;
            private String phone;
            private Object zipcode;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
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

            public Object getZipcode() {
                return zipcode;
            }

            public void setZipcode(Object zipcode) {
                this.zipcode = zipcode;
            }
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
         * created_at : 2016-10-20T13:44:39.599264+08:00
         * id : 2457
         * numbers : [1000002,1000003,1000004,1000006,1000007,1000008,1000009,1000010,1000013]
         * shares : 9
         * total_shares : 9
         * user : {"_resource":"PublicUser","id":3,"profile":{"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}}
         */

        private LuckyOrderBean lucky_order;
        /**
         * _resource : PublicUser
         * id : 3
         * profile : {"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}
         */

        private LuckyUserBean lucky_user;
        private int num_orders;
        /**
         * _resource : Product
         * detail : <span style="display:inline-block;width:4px;height:12px;border-radius:2px;background-color:#EC5330;margin-left:-10px;margin-right:8px"></span><span style="font-size:1.3em;color:#333;line-height:1.8em;">Item Information:</span>
         <table style="color:#666;">
         <tbody>
         <tr>
         <td>Brand: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td>Jaguar</td>
         </tr>
         <tr>
         <td>Size: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td>100 ml</td>
         </tr>
         <tr>
         <td>Targeted Group: &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td>Men</td>
         </tr>
         </tbody>
         </table>

         <span style="display:inline-block;width:4px;height:12px;border-radius:2px;background-color:#EC5330;margin-left:-10px;margin-right:8px"></span><span style="font-size:1.3em;color:#333;line-height:1.8em;">How to:</span>
         <span style="line-height: 1.35em;color:#666;">1. Play 1 coin, get one chance!</span>
         <span style="line-height: 1.35em;color:#666;">2. Anybody can win an item when their number matches the lucky code.</span>
         <span style="line-height: 1.35em;color:#666;">3. Buy coins , which are used to win the lucky items.</span>.
         * detail_image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/fb8bfbde8681422b94106cce3934cdde
         * id : 11
         * title : Classic Black by Jaguar for Men - Eau de Toilette, 100ml. Now it is just for $1 !
         * title_image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/578783f6f0e34b63ab1a9144ff7349a4
         */

        private ProductBean product;
        private int result_countdown;
        private double share_price;
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

        public double getShare_price() {
            return share_price;
        }

        public void setShare_price(double share_price) {
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
             * profile : {"locale":"zh_CN","name":"杨树宇","picture":"https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F","social_link":"https://www.facebook.com/app_scoped_user_id/118093991983735/","timezone":"+08:00"}
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
                 * picture : https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F
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

        public static class LuckyUserBean {
            private String _resource;
            private int id;
            /**
             * locale : zh_CN
             * name : 杨树宇
             * picture : https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/10354686_10150004552801856_220367501106153455_n.jpg?oh=6c801f82cd5a32fd6e5a4258ce00a314&oe=589AAD2F
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
