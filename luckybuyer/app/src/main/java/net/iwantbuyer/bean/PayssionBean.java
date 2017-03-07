package net.iwantbuyer.bean;

/**
 * Created by admin on 2017/3/7.
 */
public class PayssionBean {
    String method;
    int topup_option_id;

    public PayssionBean(String method, int topup_option_id) {
        this.method = method;
        this.topup_option_id = topup_option_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getTopup_option_id() {
        return topup_option_id;
    }

    public void setTopup_option_id(int topup_option_id) {
        this.topup_option_id = topup_option_id;
    }

    @Override
    public String toString() {
        return "{" +
                "\"method\"" + ":\"" + method + "\"" +
                ", \"topup_option_id\"" + ":" + topup_option_id +
                '}';
    }
}
