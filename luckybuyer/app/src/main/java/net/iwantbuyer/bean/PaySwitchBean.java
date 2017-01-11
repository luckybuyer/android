package net.iwantbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/11/18.
 */
public class PaySwitchBean {

    /**
     * _resource : Config
     * auth0_client_id : HmF3R6dz0qbzGQoYtTuorgSmzgu6Aua1
     * auth0_domain : staging-luckybuyer.auth0.com
     * broadcasts_per_page : 20
     * broadcasts_refresh_frequency : 10
     * exchange_rate : null
     * gifts_new_user : 3
     * local_currency : AED
     * payment_methods : [{"method":null,"vendor":"android-inapp"},{"method":null,"vendor":"paypal"},{"method":null,"vendor":"halopay"},{"method":null,"vendor":"visa"}]
     */

    private String _resource;
    private String auth0_client_id;
    private String auth0_domain;
    private int broadcasts_per_page;
    private int broadcasts_refresh_frequency;
    private Object exchange_rate;
    private int gifts_new_user;
    private String local_currency;
    /**
     * method : null
     * vendor : android-inapp
     */

    private List<PaymentMethodsBean> payment_methods;

    public String get_resource() {
        return _resource;
    }

    public void set_resource(String _resource) {
        this._resource = _resource;
    }

    public String getAuth0_client_id() {
        return auth0_client_id;
    }

    public void setAuth0_client_id(String auth0_client_id) {
        this.auth0_client_id = auth0_client_id;
    }

    public String getAuth0_domain() {
        return auth0_domain;
    }

    public void setAuth0_domain(String auth0_domain) {
        this.auth0_domain = auth0_domain;
    }

    public int getBroadcasts_per_page() {
        return broadcasts_per_page;
    }

    public void setBroadcasts_per_page(int broadcasts_per_page) {
        this.broadcasts_per_page = broadcasts_per_page;
    }

    public int getBroadcasts_refresh_frequency() {
        return broadcasts_refresh_frequency;
    }

    public void setBroadcasts_refresh_frequency(int broadcasts_refresh_frequency) {
        this.broadcasts_refresh_frequency = broadcasts_refresh_frequency;
    }

    public Object getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(Object exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public int getGifts_new_user() {
        return gifts_new_user;
    }

    public void setGifts_new_user(int gifts_new_user) {
        this.gifts_new_user = gifts_new_user;
    }

    public String getLocal_currency() {
        return local_currency;
    }

    public void setLocal_currency(String local_currency) {
        this.local_currency = local_currency;
    }

    public List<PaymentMethodsBean> getPayment_methods() {
        return payment_methods;
    }

    public void setPayment_methods(List<PaymentMethodsBean> payment_methods) {
        this.payment_methods = payment_methods;
    }

    public static class PaymentMethodsBean {
        private Object method;
        private String vendor;

        public Object getMethod() {
            return method;
        }

        public void setMethod(Object method) {
            this.method = method;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }
    }
}
