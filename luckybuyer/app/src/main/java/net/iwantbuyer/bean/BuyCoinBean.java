package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/10/10.
 */
public class BuyCoinBean {

    /**
     * _resource : type of the resource
     * amount : 0
     * currency : string
     * description : string
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
        private String currency;
        private String description;
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
