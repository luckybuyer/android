package net.smartbuyer.bean;

/**
 * Created by admin on 2016/11/10.
 */
public class PaypalSuccessBean {

    /**
     * _resource : PaypalPayment
     * amount : 1
     * currency : USD
     * failure_url : null
     * id : 8
     * method : paypal
     * status : success
     * success_url : null
     * token : null
     */

    private String _resource;
    private int amount;
    private String currency;
    private Object failure_url;
    private int id;
    private String method;
    private String status;
    private Object success_url;
    private Object token;

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

    public Object getFailure_url() {
        return failure_url;
    }

    public void setFailure_url(Object failure_url) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getSuccess_url() {
        return success_url;
    }

    public void setSuccess_url(Object success_url) {
        this.success_url = success_url;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }
}
