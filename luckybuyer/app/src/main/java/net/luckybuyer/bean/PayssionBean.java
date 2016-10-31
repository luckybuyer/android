package net.luckybuyer.bean;

/**
 * Created by admin on 2016/10/28.
 */
public class PayssionBean {

    /**
     * _resource : PayssionPayment
     * amount : 1
     * currency : CNY
     * failure_url : http://net.luckybuyer
     * id : 44
     * method : onecard
     * payment_url : https://www.payssion.com/pay/GA28412034381108
     * status : pending
     * success_url : http://net.luckybuyer
     */

    private String _resource;
    private int amount;
    private String currency;
    private String failure_url;
    private int id;
    private String method;
    private String payment_url;
    private String status;
    private String success_url;

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

    public String getFailure_url() {
        return failure_url;
    }

    public void setFailure_url(String failure_url) {
        this.failure_url = failure_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccess_url() {
        return success_url;
    }

    public void setSuccess_url(String success_url) {
        this.success_url = success_url;
    }
}
