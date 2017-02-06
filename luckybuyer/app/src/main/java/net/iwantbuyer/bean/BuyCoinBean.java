package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/10/10.
 */
public class BuyCoinBean {

    /**
     * _resource : type of the resource
     * amount : 0
     * android_product_id : string
     * currency : string
     * description : string
     * gifts_first_topup : 0
     * id : 0
     * name : string
     * price : 0
     */

    private List<BuycoinsBean> buycoins;

    public List<BuycoinsBean> getBuycoins() {
        return buycoins;
    }

    public void setBuycoins(List<BuycoinsBean> buycoins) {
        this.buycoins = buycoins;
    }

    public static class BuycoinsBean {
        private boolean hovered;

        public boolean isHovered() {
            return hovered;
        }

        public void setHovered(boolean hovered) {
            this.hovered = hovered;
        }
        private String _resource;
        private int amount;
        private String android_product_id;
        private String currency;
        private String description;
        private int gifts_first_topup;
        private int id;
        private String name;
        private int price;

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

        public String getAndroid_product_id() {
            return android_product_id;
        }

        public void setAndroid_product_id(String android_product_id) {
            this.android_product_id = android_product_id;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getGifts_first_topup() {
            return gifts_first_topup;
        }

        public void setGifts_first_topup(int gifts_first_topup) {
            this.gifts_first_topup = gifts_first_topup;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
