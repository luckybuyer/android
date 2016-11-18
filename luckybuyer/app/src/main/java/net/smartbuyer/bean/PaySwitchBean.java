package net.smartbuyer.bean;

import java.util.List;

/**
 * Created by admin on 2016/11/18.
 */
public class PaySwitchBean {

    /**
     * _resource : type of the resource
     * broadcasts_per_page : 0
     * broadcasts_refresh_frequency : 0
     * exchange_rate : 0
     * local_currency : string
     * payment_methods : [{"method":"string","vendor":"string"}]
     */

    private String _resource;
    private float broadcasts_per_page;
    private float broadcasts_refresh_frequency;
    private float exchange_rate;
    private String local_currency;
    /**
     * method : string
     * vendor : string
     */

    private List<PaymentMethodsBean> payment_methods;

    public String get_resource() {
        return _resource;
    }

    public void set_resource(String _resource) {
        this._resource = _resource;
    }

    public float getBroadcasts_per_page() {
        return broadcasts_per_page;
    }

    public void setBroadcasts_per_page(int broadcasts_per_page) {
        this.broadcasts_per_page = broadcasts_per_page;
    }

    public float getBroadcasts_refresh_frequency() {
        return broadcasts_refresh_frequency;
    }

    public void setBroadcasts_refresh_frequency(int broadcasts_refresh_frequency) {
        this.broadcasts_refresh_frequency = broadcasts_refresh_frequency;
    }

    public float getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(int exchange_rate) {
        this.exchange_rate = exchange_rate;
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
        private String method;
        private String vendor;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
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
