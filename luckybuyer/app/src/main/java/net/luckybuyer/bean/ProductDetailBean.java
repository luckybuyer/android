package net.luckybuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/9/18.
 */
public class ProductDetailBean {

    /**
     * _resource : Game
     * batch_id : 2
     * closed_at : null
     * finished_at : null
     * id : 51
     * issue_id : 28
     * left_shares : 2
     * lucky_number : null
     * lucky_order : null
     * lucky_user : null
     * num_orders : 0
     * product : {"_resource":"Product","detail":"She was born during the reign of James I, was a youngster when René Descartes set out his rules ","detail_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/750-3.jpg","id":2,"title":"DA Newbee Greenland shose","title_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/3.jpg"}
     * result_countdown : 60
     * share_price : 1.0
     * shares : 2
     * start_at : 2016-09-17T10:34:41.463079+00:00
     * status : running
     * url : https://api-staging.luckybuyer.net/v1/games/51
     */

    private List<ProductdetailBean> productdetail;

    public List<ProductdetailBean> getProductdetail() {
        return productdetail;
    }

    public void setProductdetail(List<ProductdetailBean> productdetail) {
        this.productdetail = productdetail;
    }

    public static class ProductdetailBean {
        private String _resource;
        private int batch_id;
        private Object closed_at;
        private Object finished_at;
        private int id;
        private int issue_id;
        private int left_shares;
        private Object lucky_number;
        private Object lucky_order;
        private Object lucky_user;
        private int num_orders;
        /**
         * _resource : Product
         * detail : She was born during the reign of James I, was a youngster when René Descartes set out his rules
         * detail_image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/750-3.jpg
         * id : 2
         * title : DA Newbee Greenland shose
         * title_image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/3.jpg
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

        public Object getClosed_at() {
            return closed_at;
        }

        public void setClosed_at(Object closed_at) {
            this.closed_at = closed_at;
        }

        public Object getFinished_at() {
            return finished_at;
        }

        public void setFinished_at(Object finished_at) {
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

        public Object getLucky_number() {
            return lucky_number;
        }

        public void setLucky_number(Object lucky_number) {
            this.lucky_number = lucky_number;
        }

        public Object getLucky_order() {
            return lucky_order;
        }

        public void setLucky_order(Object lucky_order) {
            this.lucky_order = lucky_order;
        }

        public Object getLucky_user() {
            return lucky_user;
        }

        public void setLucky_user(Object lucky_user) {
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
