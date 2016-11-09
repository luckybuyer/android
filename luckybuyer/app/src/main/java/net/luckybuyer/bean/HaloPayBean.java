package net.luckybuyer.bean;

/**
 * Created by admin on 2016/10/28.
 */
public class HaloPayBean {

    /**
     * _resource : type of the resource
     * amount : 0
     * currency : string
     * id : 0
     * method : string
     * status : pending
     * transaction_id : string
     */

    private String _resource;
    private int amount;
    private String currency;
    private int id;
    private String method;
    private String status;
    private String transaction_id;

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

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
}
