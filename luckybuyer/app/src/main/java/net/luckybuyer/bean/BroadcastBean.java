package net.luckybuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/10/19.
 */
public class BroadcastBean {


    /**
     * content : Omar paid 200 coins for 2017 Google new work package number is limited to the first
     * created_at : 2016-11-02T03:51:35.087667+00:00
     * data : {"coins":"200","product":"2017 Google new work package number is limited to the first","user_icon":"https://s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/b-icons/d9ce8638944a4823adf76950a7ea4ea1.png","username":"Omar"}
     * game_id : 2816
     * template : {username} paid {coins} coins for {product}
     * type : purchase
     */

    private List<BroadBean> broad;

    public List<BroadBean> getBroad() {
        return broad;
    }

    public void setBroad(List<BroadBean> broad) {
        this.broad = broad;
    }

    public static class BroadBean {
        private String content;
        private String created_at;
        /**
         * coins : 200
         * product : 2017 Google new work package number is limited to the first
         * user_icon : https://s3-ap-southeast-1.amazonaws.com/static-staging-lucky/images/b-icons/d9ce8638944a4823adf76950a7ea4ea1.png
         * username : Omar
         */

        private DataBean data;
        private int game_id;
        private String template;
        private String type;

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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getGame_id() {
            return game_id;
        }

        public void setGame_id(int game_id) {
            this.game_id = game_id;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class DataBean {
            private String coins;
            private String product;
            private String user_icon;
            private String username;

            public String getCoins() {
                return coins;
            }

            public void setCoins(String coins) {
                this.coins = coins;
            }

            public String getProduct() {
                return product;
            }

            public void setProduct(String product) {
                this.product = product;
            }

            public String getUser_icon() {
                return user_icon;
            }

            public void setUser_icon(String user_icon) {
                this.user_icon = user_icon;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
