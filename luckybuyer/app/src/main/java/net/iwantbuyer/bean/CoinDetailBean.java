package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/9/28.
 */
public class CoinDetailBean {

    /**
     * _resource : GoldRecord
     * amount : 200000
     * created_at : 2016-09-28T07:18:00.434414+00:00
     * id : 153
     * type : gift
     */

    private List<CoinBean> coin;

    public List<CoinBean> getCoin() {
        return coin;
    }

    public void setCoin(List<CoinBean> coin) {
        this.coin = coin;
    }

    public static class CoinBean {
        private String _resource;
        private int amount;
        private String created_at;
        private int id;
        private String type;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
