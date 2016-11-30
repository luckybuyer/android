package net.iwantbuyer.bean;

/**
 * Created by admin on 2016/11/29.
 */
public class CashuBean {

    /**
     * _resource : CashuPayment
     * amount : 10
     * currency : USD
     * failure_url : http://net.luckybuyer.failure
     * id : 6
     * payment_url : https://sandbox.cashu.com/cgi-bin/payment/pcashu.cgi
     * status : pending
     * success_url : http://net.luckybuyer.success
     * transaction_code : d67ea646f7f73805942296af38c00b7918a9a69c
     */

    private String _resource;
    private int amount;
    private String currency;
    private String failure_url;
    private int id;
    private String payment_url;
    private String status;
    private String success_url;
    private String transaction_code;

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

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }
}
