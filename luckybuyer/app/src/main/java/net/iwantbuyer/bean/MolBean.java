package net.iwantbuyer.bean;

/**
 * Created by admin on 2017/3/10.
 */
public class MolBean {

    /**
     * _resource : MolPayment
     * amount : 5
     * currency : MYR
     * id : 3
     * method : null
     * reference_id : bcb613bb0f3d43aeb8ad6fa6b7ca797b
     * status : pending
     */

    private String _resource;
    private int amount;
    private String currency;
    private int id;
    private Object method;
    private String reference_id;
    private String status;

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

    public Object getMethod() {
        return method;
    }

    public void setMethod(Object method) {
        this.method = method;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
