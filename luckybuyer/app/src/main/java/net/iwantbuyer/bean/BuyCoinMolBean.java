package net.iwantbuyer.bean;

/**
 * Created by admin on 2017/3/24.
 */
public class BuyCoinMolBean {

    /**
     * _resource : MolPayment
     * amount : 50
     * currency : MYR
     * failure_url : failure
     * id : 89
     * method : MOLPoints(E-Wallet)
     * payment_url : https://sandbox-global.mol.com/PaymentWall/Checkout/index?token=qipDtsNLDSJF%2bpgWPJ90h%2fWDoatjrRZWsfIhA7ITDg8dcm5yuGZUYtPvdOxCbYmXKtTxsI7oyCCvkTG8B0onEEpwNC9WgZyHpTjmK3hB38EGJmw6uPtxG6R3Be9DLxz6gYSeS5bqopcPrbLIizqhooAFFh8AKFCSqWC%2bltzEte7SboXxcp1G6Tf7kslnx080pWCm8D1LVcCNzcU5dKaVtsZNm5VixwSnA38pWdNEJ8B4PUpXxW58k67AwxuU%2bB0s1dwnMg0NheqW5SOxLy36At5vnsQZ8j%2fq0eVSM%2fGp9ww%2beUQudk3G2J2lRA977jmB65BxXOFlXjQ%3d
     * reference_id : dfb27869ad0b4bc3b1348932fca91e06
     * status : pending
     * success_url : success
     */

    private String _resource;
    private int amount;
    private String currency;
    private String failure_url;
    private int id;
    private String method;
    private String payment_url;
    private String reference_id;
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

    public String getSuccess_url() {
        return success_url;
    }

    public void setSuccess_url(String success_url) {
        this.success_url = success_url;
    }
}
