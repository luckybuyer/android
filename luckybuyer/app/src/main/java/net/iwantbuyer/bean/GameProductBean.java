package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/9/14.
 */
public class GameProductBean {

    /**
     * _resource : Game
     * batch_id : 4
     * id : 16
     * issue_id : 4
     * left_shares : 2
     * num_orders : 0
     * product : {"_resource":"Product","detail":"iphone","detail_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/55f0e877N3c24faa3.jpg","id":3,"title":"iphone","title_image":"//s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/55f0e877N3c24faa3.jpg"}
     * result_countdown : 20
     * share_price : 1000000.0
     * shares : 2
     * start_at : 2016-09-13T06:29:30.213866+00:00
     * status : running
     * url : https://api-staging.luckybuyer.net/v1/games/16
     */

    private List<GameBean> game;

    public List<GameBean> getGame() {
        return game;
    }

    public void setGame(List<GameBean> game) {
        this.game = game;
    }

    public static class GameBean {
        private int shares_increment;

        public int getShares_increment() {
            return shares_increment;
        }

        public void setShares_increment(int shares_increment) {
            this.shares_increment = shares_increment;
        }

        private String _resource;
        private int batch_id;
        private int id;
        private int issue_id;
        private int left_shares;
        private int num_orders;
        /**
         * _resource : Product
         * detail : iphone
         * detail_image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/55f0e877N3c24faa3.jpg
         * id : 3
         * title : iphone
         * title_image : //s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/55f0e877N3c24faa3.jpg
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
